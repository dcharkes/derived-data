
object WeblabGradesScala extends App {

  val mathAlice = new Submission()
  val examAlice = new Submission()
  val labAlice = new Submission()
  val lab1Alice = new Submission()
  val lab2Alice = new Submission()
  mathAlice.children = List(examAlice, labAlice)
  labAlice.children = List(lab1Alice, lab2Alice)
  examAlice.answer = "Good"
  examAlice.manualGrade = Some(8.0)
  lab1Alice.answer = "Perfect"
  lab1Alice.manualGrade = Some(10.0)
  lab2Alice.answer = "Sufficient"
  lab2Alice.manualGrade = Some(6.0)
  val mathBob = new Submission()
  val examBob = new Submission()
  val labBob = new Submission()
  val lab1Bob = new Submission()
  val lab2Bob = new Submission()
  mathBob.children = List(examBob, labBob)
  labBob.children = List(lab1Bob, lab2Bob)
  examBob.answer = "Very Good"
  examBob.manualGrade = Some(9.0)
  lab1Bob.answer = "Insufficient"
  lab1Bob.manualGrade = Some(3.0)
  lab2Bob.answer = "Perfect"
  lab2Bob.manualGrade = Some(10.0)

  println("Alice")
  println(mathAlice.grade)
  println(mathAlice.pass)
  println()
  println("Bob")
  println(mathBob.grade)
  println(mathBob.pass)

}

class Submission {
  var children: List[Submission] = Nil

  var answer: String = ""

  var manualGrade: Option[Double] = None

  def childGrade(): Option[Double] = {
    val grades = children.flatMap(_.grade())
    if (grades.nonEmpty)
      Some(grades.sum / grades.length)
    else
      None
  }


  def childPass(): Boolean = {
    val passes = children.map {
      _.pass()
    }
    Functions.conjunction(passes)
  }


  def grade(): Option[Double] = {
    manualGrade match {
      case Some(g) => Some(g)
      case None =>
        if (childPass())
          childGrade()
        else
          None
    }
  }


  def pass(): Boolean = {
    val gradePass = grade() match {
      case None => false
      case Some(g) => g >= 5.5
    }
    gradePass && childPass()
  }
}

object Functions {
  def conjunction(input: List[Boolean]): Boolean = {
    input.forall(b => b)
  }
}

/*
 * Plain Scala: not incremental
 */
