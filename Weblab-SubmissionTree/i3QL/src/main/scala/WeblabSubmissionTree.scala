import java.util.{Calendar, Date}

import idb.syntax.iql._
import idb.syntax.iql.IR._
import idb.SetTable
import idb.functions.AVG

import scala.reflect.SourceContext
import scala.Some
import scala.virtualization.lms.common.{Base, BaseExp}

object WeblabSubmissionTree {
  type Id = Int
  type Assignment = (Id, String, Option[Id])
  type Student = (Id, String)
  // Submission = (Id, name, assignmentId, studentId)
  type Submission = (Id, String, Id, Id)

  val assignment = SetTable.empty[Assignment]
  val student = SetTable.empty[Student]
  val submission = SetTable.empty[Submission]

  val submissionParent: Relation[(Id, Id)] = (
    SELECT(
      (a1: Rep[Assignment], a2: Rep[Assignment], s1: Rep[Submission], s2: Rep[Submission]) => (
        s1._1,
        s2._1
        ))
      FROM(assignment, assignment, submission, submission)
      WHERE ((a1: Rep[Assignment], a2: Rep[Assignment], s1: Rep[Submission], s2: Rep[Submission]) =>
      (a2._3.isDefined AND
        a1._1 == a2._3.get AND
        s1._3 == a1._1 AND
        s2._3 == a2._1 AND
        s1._4 == s2._4)
      )
    )
  val submissionParentMaterialized = submissionParent.asMaterialized

  def main(args: Array[String]): Unit = {
    import Predef.println

    val alice = new Student(1, "Alice")
    val bob = new Student(2, "Bob")
    student += alice
    student += bob
    val math = new Assignment(1, "math", None)
    val exam = new Assignment(2, "exam", Some(1))
    val lab = new Assignment(3, "lab", Some(1))
    val lab1 = new Assignment(4, "lab1", Some(3))
    val lab2 = new Assignment(5, "lab2", Some(3))
    assignment += math
    assignment += exam
    assignment += lab
    assignment += lab1
    assignment += lab2
    val mathAlice = new Submission(1, "mathAlice", 1, 1)
    val examAlice = new Submission(2, "examAlice", 2, 1)
    val labAlice = new Submission(3, "labAlice", 3, 1)
    val lab1Alice = new Submission(4, "lab1Alice", 4, 1)
    val lab2Alice = new Submission(5, "lab2Alice", 5, 1)
    submission += mathAlice
    submission += examAlice
    submission += labAlice
    submission += lab1Alice
    submission += lab2Alice
    val mathBob = new Submission(6, "mathBob", 1, 2)
    val examBob = new Submission(7, "examBob", 2, 2)
    val labBob = new Submission(8, "labBob", 3, 2)
    val lab1Bob = new Submission(9, "lab1Bob", 4, 2)
    val lab2Bob = new Submission(10, "lab2Bob", 5, 2)
    submission += mathBob
    submission += examBob
    submission += labBob
    submission += lab1Bob
    submission += lab2Bob

    println()
    println("submission parent (id, parent):")
    submissionParentMaterialized.foreach(println(_))

  }
}

/*
To get the i3QL dependency working, one needs to locally build a certain version of LMS and a certain version of i3QL.

2. Download and build the LMS project

      $ git clone https://github.com/seba--/virtualization-lms-core.git

  Go to the root directory and install the project using SBT. You need to be on the branch "i3QL-compatible"

      $ cd virtualization-lms-core
      $ git checkout develop
      $ sbt publish-local

3. Download and build the i3Ql project.

      $ git clone https://github.com/seba--/i3QL.git

  Go to the root directory and install the project using SBT. Currently you need to be on the branch "scala-2.10.2".

      $ cd i3Ql
      $ git checkout master
      $ sbt publish-local

To run the code `sbt run` or in IntelliJ a sbt task `run` (running as application in IntelliJ doesn"t work).
 */
