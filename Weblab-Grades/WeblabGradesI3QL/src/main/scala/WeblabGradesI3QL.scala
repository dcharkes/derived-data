import java.util.{Calendar, Date}

import idb.syntax.iql._
import idb.syntax.iql.IR._
import idb.SetTable
import idb.functions.AVG

import scala.reflect.SourceContext
import scala.Some
import scala.virtualization.lms.common.{Base, BaseExp}

// make sure we don't use LMS Some's


object WeblabGradesI3QL {
  type Id = Int
  type Submission = (Id, Option[Id], Option[Double])

  val submission = SetTable.empty[Submission]

  val submissionParent: Relation[(Submission, Submission)] = (
    SELECT(*)
      FROM(submission, submission)
      WHERE ((sChild: Rep[Submission], sParent: Rep[Submission]) =>
      (sChild._2.isDefined AND
        sChild._2.get == sParent._1))
    )
  val submissionParentMaterialized = submissionParent.asMaterialized

  val submissionChildGrade: Relation[(Id, Int)] = (
    SELECT((s: Rep[Option[Int]]) => s.get, SUM((s: Rep[Submission]) => s._3.get.toInt)) //SUM of ints, as AVG and doubles are not available. Note that toInt also needs a fix in the i3QL lib
      FROM (submission)
      WHERE ((s: Rep[Submission]) => s._3.isDefined)
      GROUP BY((s: Rep[Submission]) => s._2)
    )
  val submissionChildGradeMaterialized = submissionChildGrade.asMaterialized

  //  def gradeToPass(g: Option[Double]): Boolean = { // how to lift this to Reps?
  //    g match {
  //      case None => false
  //      case Some(g) => g >= 5.5
  //    }
  //  }
  //
  //  val submissionPass = (
  //    SELECT((s: Rep[Submission]) => (s._1, gradeToPass(s._3)))
  //      FROM (submission)
  //      WHERE ((s: Rep[Submission]) => true)
  //    )
  //  val submissionPassMaterialized = submissionPass.asMaterialized

  def main(args: Array[String]): Unit = {
    import Predef.println

    val mathAlice = new Submission(1, None, None)
    val examAlice = new Submission(2, Some(1), Some(7.0))
    val labAlice = new Submission(3, Some(1), None)
    val lab1Alice = new Submission(4, Some(3), Some(8.0))
    val lab2Alice = new Submission(5, Some(3), Some(9.0))

    submission += mathAlice
    submission += examAlice
    submission += labAlice
    submission += lab1Alice
    submission += lab2Alice

    println()
    println("Submissions:")
    submission.foreach(println(_))

    println()
    println("Submission Parents:")
    submissionParent.foreach(println(_))

    println()
    println("Submissions Parents (2):")
    submissionParentMaterialized.foreach(println(_))

    println()
    println("Child Grades:")
    submissionChildGradeMaterialized.foreach(println(_))

    //    println()
    //    println("Pass:")
    //    submissionPassMaterialized.foreach(println(_))
  }
}

/*
To get the i3QL dependency working, one needs to locally build a certain version of LMS and a certain version of i3QL.

2. Download and build the LMS project

      $ git clone https://github.com/seba--/virtualization-lms-core.git

  Go to the root directory and install the project using SBT. You need to be on the branch 'i3QL-compatible'

      $ cd virtualization-lms-core
      $ git checkout develop
      $ sbt publish-local

3. Download and build the i3Ql project.

      $ git clone https://github.com/seba--/i3QL.git

  Go to the root directory and install the project using SBT. Currently you need to be on the branch 'scala-2.10.2'.

      $ cd i3Ql
      $ git checkout master
      $ sbt publish-local

To run the code `sbt run` or in IntelliJ a sbt task `run` (running as application in IntelliJ doesn't work).
 */
