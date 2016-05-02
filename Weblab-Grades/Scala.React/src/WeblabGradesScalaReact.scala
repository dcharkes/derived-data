import scala.react._
import dom._

object WeblabGradesScalaReact extends App {
  
  testTurns(3) {
    val mathAlice = new Submission()
    val examAlice = new Submission()
    val labAlice = new Submission()
    mathAlice.children() = List(examAlice, labAlice)
    examAlice.manualGrade() = Some(7.0)
    labAlice.manualGrade() = Some(8.0)
    
    turn{
      println(mathAlice.grade.getValue)
      println(mathAlice.pass.getValue)
      println(mathAlice.childGrade.getValue)
      println(mathAlice.childPass.getValue)
    }
    
  }
}

class Submission {
  val children: Var[List[Submission]] = Var(Nil)
  
  val manualGrade : Var[Option[Double]] = Var(None)
  
  val grade : Signal[Option[Double]] = Strict {
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
    grade() match {
      case None    => false
      case Some(g) => g >= 5.5
    }
  }
  
  val childGrade: Signal[Option[Double]] = Strict {
    val grades = children().flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }
  
  val childPass: Signal[Boolean] = Strict {
    val passes = children().map { _.pass() }
    Functions.conjunction(passes)
  }
}

object Functions {
  def conjunction(input: List[Boolean]): Boolean = {
    input.forall(b => b)
  }
}
