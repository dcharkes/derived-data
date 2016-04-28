import rescala._
import rescala.events._
import makro.SignalMacro.{ SignalM => Signal }

object WeblabGradesREScala extends App {

  val mathAlice = new Submission() // throws Exception
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
  val childGrade: DependentSignal[Option[Double]] = Signal {
    val grades = children().map { _.grade() }.flatten
    if (grades.length > 0)
      Some(grades.sum / grades.length)
    else
      None
  }
  val childPass: DependentSignal[Boolean] = Signal {
    val passes = children().map { _.pass() }
    Functions.conjuction(passes)
  }
}

object Functions {

  def conjuction(input: List[Boolean]): Boolean = {
    input.foldRight(true)((a, b) => a && b)
  }
}
