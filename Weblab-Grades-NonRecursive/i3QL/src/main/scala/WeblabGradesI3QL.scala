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
  type Submission = (Id, Option[Id], Option[String], Option[Double])
  type SubmissionFull = (Id, Option[Id], Option[String], Option[Double], Option[Double], Boolean)
  type SubmissionGrade = (Id, Option[Double])
  type SubmissionPass = (Id, Boolean)

  val submission1 = SetTable.empty[Submission]
  val submission2 = SetTable.empty[Submission]
  val submission3 = SetTable.empty[Submission]

  val submission3grade: Relation[SubmissionGrade] = (
    SELECT(
      (s: Rep[Submission]) => (
        s._1,
        s._4
        ))
      FROM (submission3)
      WHERE ((s: Rep[Submission]) => true)
    )
  val submission3gradeMaterialized = submission3grade.asMaterialized

  val submission3pass: Relation[SubmissionPass] = (
    SELECT(
      (s: Rep[SubmissionGrade]) => (
        s._1,
        if (s._2.isDefined) s._2.get >= 5.5 else false
        ))
      FROM (submission3grade)
      WHERE (_ => true)
    )
  val submission3passMaterialized = submission3pass.asMaterialized

  // should be : Relation[SubmissionFull], but its not
  val submission3fullNested: Relation[(Submission, SubmissionGrade, SubmissionPass)] = (
    SELECT(*)
      FROM(submission3, submission3grade, submission3pass)
      WHERE ((s3: Rep[Submission], s3grade: Rep[SubmissionGrade], s3pass: Rep[SubmissionPass]) =>
      (s3._1 == s3grade._1 AND
        s3._1 == s3pass._1))
    )
  val submission3fullNestedMaterialized = submission3fullNested.asMaterialized

  // what is wrong with this relation? why does it not constitute a SubmissionFull?
  //  // should be : Relation[SubmissionFull], but its not
  //  val submission3full = (
  //    SELECT(
  //      (s3: Rep[Submission], s3grade: Rep[SubmissionGrade], s3pass: Rep[SubmissionPass]) => (
  //        s3._1,
  //        s3._2,
  //        s3._3,
  //        s3._4,
  //        s3grade._2,
  //        s3pass._2
  //        ))
  //      FROM(submission3, submission3grade, submission3pass)
  //      WHERE ((s3: Rep[Submission], s3grade: Rep[SubmissionGrade], s3pass: Rep[SubmissionPass]) =>
  //      (s3._1 == s3grade._1 AND
  //        s3._1 == s3pass._1))
  //    )
  //  val submission3fullMaterialized = submission3full.asMaterialized

  // should be : Relation[(Id, Option[Double])]
  // SUM of ints, as AVG and doubles are not available. Note that toInt also needs a fix in the i3QL lib
  // should use submission3full, once that works
  val submission2childGrade: Relation[(Id, Int)] = (
    SELECT((s: Rep[Option[Int]]) => s.get, SUM((s: Rep[Submission]) => s._4.get.toInt))
      FROM (submission3)
      WHERE ((s: Rep[Submission]) => s._4.isDefined)
      GROUP BY((s: Rep[Submission]) => s._2)
    )
  val submission2childGradeMaterialized = submission2childGrade.asMaterialized

  def main(args: Array[String]): Unit = {
    import Predef.println

    val mathAlice = new Submission(1, None, None, None)
    val examAlice = new Submission(2, Some(mathAlice._1), Some("Good"), Some(8.0))
    val labAlice = new Submission(3, Some(mathAlice._1), None, None)
    val lab1Alice = new Submission(4, Some(labAlice._1), Some("Perfect"), Some(10.0))
    val lab2Alice = new Submission(5, Some(labAlice._1), Some("Sufficient"), Some(9.0))
    submission1 += mathAlice
    submission2 += examAlice
    submission2 += labAlice
    submission3 += lab1Alice
    submission3 += lab2Alice
    val mathBob = new Submission(6, None, None, None)
    val examBob = new Submission(7, Some(mathBob._1), Some("Very Good"), Some(9.0))
    val labBob = new Submission(8, Some(mathBob._1), None, None)
    val lab1Bob = new Submission(9, Some(labBob._1), Some("Insufficient"), Some(3.0))
    val lab2Bob = new Submission(10, Some(labBob._1), Some("Perfect"), Some(10.0))
    submission1 += mathBob
    submission2 += examBob
    submission2 += labBob
    submission3 += lab1Bob
    submission3 += lab2Bob


    println()
    println("submission3 (id, grade):")
    submission3gradeMaterialized.foreach(println(_))

    println()
    println("submission3 (id, pass):")
    submission3passMaterialized.foreach(println(_))

    println()
    println("submission3 full:")
    submission3fullNestedMaterialized.foreach(println(_))

    //    println()
    //    println("submission3 full:")
    //    submission3fullMaterialized.foreach(println(_))

    println()
    println("submission2 (id, childGrade):")
    submission2childGradeMaterialized.foreach(println(_))

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
