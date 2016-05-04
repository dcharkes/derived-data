import scala.react._
import dom._

object WeblabGradesScalaReact extends App {

  testTurns(3) {
    val mathAlice = new Submission()
    val examAlice = new Submission()
    val labAlice = new Submission()
    val lab1Alice = new Submission()
    val lab2Alice = new Submission()
    mathAlice.children() = List(examAlice, labAlice)
    labAlice.children() = List(lab1Alice, lab2Alice)
    examAlice.answer() = "Good"
    examAlice.manualGrade() = Some(8.0)
    lab1Alice.answer() = "Perfect"
    lab1Alice.manualGrade() = Some(10.0)
    lab2Alice.answer() = "Sufficient"
    lab2Alice.manualGrade() = Some(6.0)
    val mathBob = new Submission()
    val examBob = new Submission()
    val labBob = new Submission()
    val lab1Bob = new Submission()
    val lab2Bob = new Submission()
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

class Submission {
  val children: Var[List[Submission]] = Var(Nil)

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

object Functions {
  def conjunction(input: List[Boolean]): Boolean = {
    input.forall(b => b)
  }
}
