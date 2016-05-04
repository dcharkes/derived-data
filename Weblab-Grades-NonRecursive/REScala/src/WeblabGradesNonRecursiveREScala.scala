import rescala._
import makro.SignalMacro.{SignalM => Signal}

object WeblabGradesNonRecursiveREScala extends App {

  val mathAlice = new Submission1()
  val examAlice = new Submission2()
  val labAlice = new Submission2()
  val lab1Alice = new Submission3()
  val lab2Alice = new Submission3()
  mathAlice.children() = List(examAlice, labAlice)
  labAlice.children() = List(lab1Alice, lab2Alice)
  examAlice.answer() = "Good"
  examAlice.manualGrade() = Some(8.0)
  lab1Alice.answer() = "Perfect"
  lab1Alice.manualGrade() = Some(10.0)
  lab2Alice.answer() = "Sufficient"
  lab2Alice.manualGrade() = Some(6.0)
  val mathBob = new Submission1()
  val examBob = new Submission2()
  val labBob = new Submission2()
  val lab1Bob = new Submission3()
  val lab2Bob = new Submission3()
  mathBob.children() = List(examBob, labBob)
  labBob.children() = List(lab1Bob, lab2Bob)
  examBob.answer() = "Very Good"
  examBob.manualGrade() = Some(9.0)
  lab1Bob.answer() = "Insufficient"
  lab1Bob.manualGrade() = Some(3.0)
  lab2Bob.answer() = "Perfect"
  lab2Bob.manualGrade() = Some(10.0)

  println("Alice")
  println(mathAlice.grade.get)
  println(mathAlice.pass.get)
  println()
  println("Bob")
  println(mathBob.grade.get)
  println(mathBob.pass.get)

}

class Submission1 {
  val children: VarSynt[List[Submission2]] = Var(Nil)

  val answer: VarSynt[String] = Var("")

  val manualGrade: VarSynt[Option[Double]] = Var(None)

  val childGrade: DependentSignal[Option[Double]] = Signal {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }

  val childPass: DependentSignal[Boolean] = Signal {
    val passes = children().map {
      _.pass()
    }
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
    val gradePass = grade() match {
      case None => false
      case Some(g) => g >= 5.5
    }
    gradePass && childPass()
  }
}

class Submission2 {
  val children: VarSynt[List[Submission3]] = Var(Nil)

  val answer: VarSynt[String] = Var("")

  val manualGrade: VarSynt[Option[Double]] = Var(None)

  val childGrade: DependentSignal[Option[Double]] = Signal {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }

  val childPass: DependentSignal[Boolean] = Signal {
    val passes = children().map {
      _.pass()
    }
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
    val gradePass = grade() match {
      case None => false
      case Some(g) => g >= 5.5
    }
    gradePass && childPass()
  }
}

class Submission3 {
  val answer: VarSynt[String] = Var("")

  val manualGrade: VarSynt[Option[Double]] = Var(None)

  val grade: DependentSignal[Option[Double]] = Signal {
    manualGrade()
  }

  val pass: DependentSignal[Boolean] = Signal {
    grade() match {
      case None => false
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
