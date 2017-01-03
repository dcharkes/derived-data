import rescala._
import rescala.events._
import makro.SignalMacro.{SignalM => Signal}

object WeblabSubmissionTreeREScala extends App {

  val alice = new Student()
  val bob = new Student()

  val math = new Assignment()
  val lab = new Assignment()
  val exam = new Assignment()

  val mathAlice = new Submission()
  val examAlice = new Submission()
  val labAlice = new Submission()
  val mathBob = new Submission()
  val examBob = new Submission()
  val labBob = new Submission()

  alice.name() = "Alice"
  bob.name() = "Bob"
  math.name() = "Math"
  lab.name() = "Lab"
  exam.name() = "Exam"

  mathAlice.setStudent(alice)
  examAlice.setStudent(alice)
  labAlice.setStudent(alice)
  mathBob.setStudent(bob)
  examBob.setStudent(bob)
  labBob.setStudent(bob)

  mathAlice.setAssignment(math)
  mathBob.setAssignment(math)
  examAlice.setAssignment(exam)
  examBob.setAssignment(exam)
  labAlice.setAssignment(lab)
  labBob.setAssignment(lab)

  lab.setParent(math)
  exam.setParent(math)

  println("MathAlice child submissions: " + mathAlice.children.get)
  println("ExamAlice parent submission: " + examAlice.parent.get)
  println("LabAlice parent submission:  " + labAlice.parent.get)

  println("Alice fullname:  " + alice.fullName.get)
  alice.surName = "Bobson"
  println("Alice fullname:  " + alice.fullName.get)

}

class Assignment {
  val name: VarSynt[String] = Var("")
  val children: VarSynt[List[Assignment]] = Var(Nil)
  val parent: VarSynt[Option[Assignment]] = Var(None)
  val submissions: VarSynt[List[Submission]] = Var(Nil)

  def setParent(a: Assignment) = {
    val oldParent = this.parent.get
    oldParent.foreach { o =>
      o.children() = o.children.get.filter(_ != this)
    }
    this.parent() = Some(a)
    a.children() = this :: a.children.get
  }

  override def toString: String = name.get
}

class Submission {
  val assignment: VarSynt[Option[Assignment]] = Var(None)
  val student: VarSynt[Option[Student]] = Var(None)

  def setStudent(s: Student) = {
    val oldStudent = this.student.get
    oldStudent.foreach { o =>
      o.submissions() = o.submissions.get.filter(_ != this)
    }
    this.student() = Some(s)
    s.submissions() = this :: s.submissions.get
  }

  def setAssignment(a: Assignment) = {
    val oldAssignment = this.assignment.get
    oldAssignment.foreach { o =>
      o.submissions() = o.submissions.get.filter(_ != this)
    }
    this.assignment() = Some(a)
    a.submissions() = this :: a.submissions.get
  }

  val children: DependentSignal[List[Submission]] = Signal {
    assignment().map(_.children()).getOrElse(Nil).flatMap(_.submissions()).filter(_.student() == student())
  }

  val parent: VarSynt[Option[Submission]] = Var(None)

  var oldChildren: List[Submission] = Nil

  val e: Event[List[Submission]] = children.changed
  e += ((newChildren: List[Submission]) => {
    val addChildren = newChildren.filterNot(oldChildren.toSet)
    val removeChildren = oldChildren.filterNot(newChildren.toSet)
    removeChildren.foreach {
      _.parent() = None
    }
    addChildren.foreach {
      _.parent() = Some(this)
    }
    oldChildren = newChildren
  })

  val name: DependentSignal[String] = Signal {
    val aName = assignment().map(_.name()).getOrElse("")
    val sName = student().map(_.name()).getOrElse("")
    aName + sName
  }

  override def toString: String = name.get
}

class Student {
  val name: VarSynt[String] = Var("")
  val submissions: VarSynt[List[Submission]] = Var(Nil)

  var surName: String = ""
  val fullName: DependentSignal[String] = Signal {
    name() +  " " + surName
  }

  override def toString: String = name.get
}
