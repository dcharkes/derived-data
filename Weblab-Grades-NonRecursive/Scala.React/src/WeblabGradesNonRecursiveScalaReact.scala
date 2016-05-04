import scala.react._
import dom._

object WeblabGradesNonRecursiveScalaReact extends App {

  testTurns(3) {
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

    turn {
      println("Alice")
      println(mathAlice.grade.getValue)
      println(mathAlice.pass.getValue)
      println()
      println("Bob")
      println(mathBob.grade.getValue)
      println(mathBob.pass.getValue)
    }

  }
}

class Submission1 {
  val children: Var[List[Submission2]] = Var(Nil)

  val answer: Var[String] = Var("")

  val manualGrade: Var[Option[Double]] = Var(None)

  val grade: Signal[Option[Double]] = Strict {
    manualGrade() match {
      case Some(g) => Some(g)
      case None =>
        if (childPass())
          childGrade()
        else
          None
    }
  }

  val pass: Signal[Boolean] = Strict {
    val gradePass = grade() match {
      case None => false
      case Some(g) => g >= 5.5
    }
    gradePass && childPass()
  }

  val childGrade: Signal[Option[Double]] = Strict {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }

  val childPass: Signal[Boolean] = Strict {
    val passes = children().map {
      _.pass()
    }
    Functions.conjunction(passes)
  }
}

class Submission2 {
  val children: Var[List[Submission3]] = Var(Nil)

  val answer: Var[String] = Var("")

  val manualGrade: Var[Option[Double]] = Var(None)

  val grade: Signal[Option[Double]] = Strict {
    manualGrade() match {
      case Some(g) => Some(g)
      case None =>
        if (childPass())
          childGrade()
        else
          None
    }
  }

  val pass: Signal[Boolean] = Strict {
    val gradePass = grade() match {
      case None => false
      case Some(g) => g >= 5.5
    }
    gradePass && childPass()
  }

  val childGrade: Signal[Option[Double]] = Strict {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }

  val childPass: Signal[Boolean] = Strict {
    val passes = children().map {
      _.pass()
    }
    Functions.conjunction(passes)
  }
}

class Submission3 {
  val answer: Var[String] = Var("")

  val manualGrade: Var[Option[Double]] = Var(None)

  val grade: Signal[Option[Double]] = Strict {
    manualGrade()
  }

  val pass: Signal[Boolean] = Strict {
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
