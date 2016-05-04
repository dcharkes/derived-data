package makro

import scala.language.experimental.macros

import scala.collection.mutable.ListBuffer
import scala.reflect.macros.blackbox.Context

import rescala.DependentSignal
import rescala.Signal
import rescala.SignalSynt
import rescala.Var

object SignalMacro {

  def SignalM[A](expression: A): DependentSignal[A] = macro SignalMacro[A]

  def SignalMacro[A: c.WeakTypeTag](c: Context)(expression: c.Expr[A]):
      c.Expr[DependentSignal[A]] = {
    import c.universe._


//    val out = new java.io.FileWriter(
//        "/home/pascal/Desktop/debugfile.txt", true)

//    out.append(showRaw(expression.tree) + "\n\n\n\n")
//    out.append(showRaw(reify { { val a = 0; a } }) + "\n\n\n\n")

//    extracting sub trees with type Reactive[_]
//    val extractedReactives = expression.tree filter {
//      t => t.tpe <:< typeOf[Reactive]
//    }


    // all symbols that are defined within the macro expression
    val definedSymbols = (expression.tree collect {
      case defTree: DefTree => defTree.symbol -> defTree
    }).toMap

    // find inner macros that are not expanded
    // (note: should inner macros not be expanded first by the compiler?)
    // we need to take special care for nested signals
    val nestedUnexpandedMacros = (expression.tree collect {
      case tree if tree.symbol != null && tree.symbol.isMacro =>
        val makro = tree match {
          case Apply(TypeApply(Select(makro, _), _), _) => makro
          case TypeApply(Select(makro, _), _) => makro
          case Select(makro, _) => makro
          case _ => null
        }

        if (makro != null && makro.tpe =:= typeOf[this.type])
          tree :: (tree filter { _ => true })
        else
          List.empty
    }).flatten.toSet

    // collect expression annotated to be unchecked and do not issue warnings
    // for those (use the standard Scala unchecked annotation for that purpose)
    val uncheckedExpressions = (expression.tree collect {
      case tree @ Typed(_, _)
          if (tree.tpe match {
            case AnnotatedType(annotations, _) =>
              annotations exists { _.tree.tpe <:< typeOf[unchecked] }
            case _ => false
          }) =>
        def uncheckedSubExpressions(tree: Tree): List[Tree] = tree match {
          case Select(expr, _) => expr :: uncheckedSubExpressions(expr)
          case Apply(expr, _) => expr :: uncheckedSubExpressions(expr)
          case TypeApply(expr, _) => expr :: uncheckedSubExpressions(expr)
          case Typed(expr, _) => expr :: uncheckedSubExpressions(expr)
          case Block(_, expr) => expr :: Nil
          case _ => Nil
        }
        uncheckedSubExpressions(tree)
    }).flatten.toSet

    // generate warning for some common cases where called functions are
    // either unnecessary (if free of side effects) or have side effects
    def isMethodWithPotentialNonLocalSideEffects(tree: Tree) = tree match {
      case fun @ (TypeApply(_, _) | Apply(_, _) | Select(_, _))
          if !(uncheckedExpressions contains fun) =>
        val args = tree match {
          case TypeApply(_, args) => args
          case Apply(_, args) => args
          case _ => List.empty
        }

        val noFunctionInArgs = !(args exists {
          case tree
            if (tree.tpe match {
              case TypeRef(_, _, args) => args.nonEmpty
              case _ => false
            }) => true
          case _ => false
        })

        val noConstructorInFun = (fun exists {
          case Apply(fun, args) =>
            !(fun exists {
              case Select(_, termNames.CONSTRUCTOR) => true
              case _ => false
            })
          case _ => false
        })

        noFunctionInArgs && noConstructorInFun
      case _ => false
    }

    def potentialSideEffectWarning(pos: Position) =
      c.warning(pos,
          "Statement may either be unnecessary or have side effects. " +
          "Signal expressions should have no side effects.")

    expression.tree foreach {
      case Block(stats, _) =>
        stats foreach { stat =>
          if (isMethodWithPotentialNonLocalSideEffects(stat))
            potentialSideEffectWarning(stat.pos)
        }
      case tree =>
        if (isMethodWithPotentialNonLocalSideEffects(tree) &&
            tree.tpe =:= typeOf[Unit])
          potentialSideEffectWarning(tree.pos)
    }

    // the argument that is used by the SignalSynt class to assemble dynamic
    // dependencies
    // every Signal { ... } macro instance gets expanded into a SignalSynt
    val signalSyntArgName = TermName(c.freshName("s$"))
    val signalSyntArgIdent = Ident(signalSyntArgName)
    internal setType (signalSyntArgIdent, weakTypeOf[SignalSynt[A]])

    // the signal values that will be cut out of the Signal expression
    val signalValues = ListBuffer.empty[ValDef]

    object transformer extends Transformer {
      private def treeTypeNullWarning() =
        c.warning(c.enclosingPosition,
            "internal warning: tree type was null, " +
            "this should not happen but the signal may still work")

      private def potentialReactiveConstructionWarning(pos: Position) =
        c.warning(pos,
            "expression should not be placed inside a signal expression " +
            "since it potentially creates a new reactive every time the " +
            "signal is evaluated which can lead to unintentional behavior")

      private def isReactive(tree: Tree) =
        if (tree.tpe == null) { treeTypeNullWarning; false }
        else tree.tpe <:< typeOf[Signal[_]] || tree.tpe <:< typeOf[Var[_]]

      override def transform(tree: Tree) =
        tree match {
          // pass the SignalSynt argument to every reactive
          // to obtain dynamic dependencies
          //
          // for example, this step transforms
          //   Signal { a() + b() }
          // to
          //   SignalSynt { s => a(s) + b(s) }
          case tree @ Apply(Select(reactive, apply), List())
              if isReactive(reactive)
                 && apply.decodedName.toString == "apply"
                 && !(nestedUnexpandedMacros contains tree) =>
            val reactiveApply = Select(reactive, TermName("apply"))
            internal setType (reactiveApply, tree.tpe)
            Apply(super.transform(reactiveApply), List(signalSyntArgIdent))

          // cut signal values out of the signal expression, that could
          // potentially create a new signal object for every access
          // it is assumed that such functions are pure in the sense that they
          // will create an equivalent signal for each call with the same
          // arguments so the function value has to be calculated just once
          //
          // for example, this step transforms
          //   Signal { event.count() }
          // to
          //   Signal { s() }
          // and creates a signal value
          //   val s = event.count
          case reactive @ (TypeApply(_, _) | Apply(_, _) | Select(_, _))
            if isReactive(reactive) &&
              // make sure that the expression e to be cut out
              // - refers to a term that is not a val or var
              //   or an accessor for a field
              // - is not a reactive value resulting from a function that is
              //   itself called on a reactive value
              //   (so the expression does not contained "chained" reactives)
              // - does not reference definitions that are defined within the
              //   macro expression but not within e
              //   (note that such a case can lead to unintentional behavior)
              reactive.symbol.isTerm &&
              !reactive.symbol.asTerm.isVal &&
              !reactive.symbol.asTerm.isVar &&
              !reactive.symbol.asTerm.isAccessor &&
              (reactive filter { tree =>
                val critical = tree match {
                  // check if reactive results from a function that is
                  // itself called on a reactive value
                  case tree @ Apply(Select(chainedReactive, apply), List()) =>
                    isReactive(chainedReactive) &&
                       apply.decodedName.toString == "apply" &&
                       !(nestedUnexpandedMacros contains tree)

                  // check reference definitions that are defined within the
                  // macro expression but not within the reactive
                  case tree: SymTree =>
                    definedSymbols get tree.symbol match {
                      case Some(defTree) => !(reactive exists { _ == defTree })
                      case _ => false
                    }

                  // "uncritical" reactive that can be cut out
                  case _ => false
                }

                if (critical && !(uncheckedExpressions contains reactive)) {
                  def methodObjectType(method: Tree) = {
                    def methodObjectType(tree: Tree): Type =
                      if (tree.symbol != method.symbol)
                        tree.tpe
                      else if (tree.children.nonEmpty)
                        methodObjectType(tree.children.head)
                      else
                        NoType

                    methodObjectType(method) match {
                      // if we can access the type arguments of the type directly,
                      // return it
                      case tpe @ TypeRef(_, _, _) => tpe
                      // otherwise, find the type in the term symbol's type signature
                      // whose type arguments can be accessed
                      case tpe =>
                        tpe.termSymbol.typeSignature find {
                          case TypeRef(_, _, _) => true
                          case _ => false
                        } match {
                          case Some(tpe) => tpe
                          case _ => tpe
                        }
                    }
                  }

                  // issue no warning if the reactive is retrieved from a container
                  // determined by the generic type parameter
                  methodObjectType(reactive) match {
                    case TypeRef(_, _, args) =>
                      if (!(args contains reactive.tpe))
                        potentialReactiveConstructionWarning(reactive.pos)
                    case _ =>
                      potentialReactiveConstructionWarning(reactive.pos)
                  }
                }

                critical
              }).isEmpty =>

            // create the signal definition to be cut out of the
            // macro expression and its substitution variable
            val signalName = TermName(c.freshName("s$"))

            val signalDef = ValDef(Modifiers(), signalName, TypeTree(), reactive)
            signalValues += signalDef

            val ident = Ident(signalName)
            internal setType (ident, reactive.tpe)
            ident

          case _ =>
            super.transform(tree)
        }
    }

    val tree = transformer transform expression.tree

    // SignalSynt argument function
    val function =
      Function(
        List(
          ValDef(
            Modifiers(), signalSyntArgName,
            TypeTree(weakTypeOf[SignalSynt[A]]), EmptyTree)),
        tree)

    // create SignalSynt object
    // use fully-qualified name, so no extra import is needed
    val body =
      Apply(
        TypeApply(
          Select(
            Select(
              Select(
                Ident(termNames.ROOTPKG),
                TermName("rescala")),
              TermName("SignalSynt")),
            TermName("apply")),
          List(TypeTree(weakTypeOf[A]))),
        List(function))

    // assemble the SignalSynt object and the signal values that are accessed
    // by the object, but were cut out of the signal expression during the code
    // transformation
    val block =
      Typed(Block(signalValues.toList, body), TypeTree(weakTypeOf[DependentSignal[A]]))


//    out.append((c untypecheck block) + "\n\n")
//    out.close


    c.Expr[DependentSignal[A]](Typer(c) untypecheck block)
  }
}
