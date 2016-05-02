import rescala._
import rescala.events._
import makro.SignalMacro.{ SignalM => Signal }

object WeblabGradesREScala extends App {

  val mathAlice = new Submission()
  val examAlice = new Submission()
  val labAlice = new Submission()
  mathAlice.children() = List(examAlice, labAlice)
  examAlice.manualGrade() = Some(7.0)
  labAlice.manualGrade() = Some(8.0)

  println(mathAlice.grade.get)
  println(mathAlice.pass.get)
  println(mathAlice.childGrade.get)
  println(mathAlice.childPass.get)

}

class Submission {
  val children: VarSynt[List[Submission]] = Var(Nil)

  val manualGrade: VarSynt[Option[Double]] = Var(None)

  val childGrade: DependentSignal[Option[Double]] = Signal {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }
  
  val childPass: DependentSignal[Boolean] = Signal {
    val passes = children().map { _.pass() }
    Functions.conjunction(passes)
  }
  
  val grade: DependentSignal[Option[Double]] = Signal {
    manualGrade() match {
      case Some(g) => Some(g)
      case None =>
        if (childPass())
          childGrade()
        else
          None
    }
  }
  
  val pass: DependentSignal[Boolean] = Signal {
    grade() match {
      case None    => false
      case Some(g) => g >= 5.5
    }
  }
}

object Functions {
  def conjunction(input: List[Boolean]): Boolean = {
    input.forall(b => b)
  }
}

/*
The order of the attributes matters.
grade, pass, childGrade, childPass causes a null pointer exception add runtime, on instantiating mathAlice.

REScala does support definitions such as the one you mentioned.
It is however limited by the rules for `val`s in Scala.
In this case the problem is, that `grade` has a forward reference to `childPass` as well as `childGrade`.
At the time when `grade` is initialized, both forward references are still uninitialized, leading to the NPE.
You can add the `-Xcheckinit` to scalac to get runtime checks for these kinds of errors (which produces a better exception than just a NPE).
The solution in this case is to move both the definition of `childPass` and `childGrade` before the definition of `grade`.
*/
