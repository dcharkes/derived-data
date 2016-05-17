-- MySQL dump 10.13  Distrib 5.6.22, for osx10.10 (x86_64)
--
-- Host: localhost    Database: DerivedData
-- ------------------------------------------------------
-- Server version	5.6.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `DerivedData`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `DerivedData` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `DerivedData`;

--
-- Table structure for table `Assignment`
--

DROP TABLE IF EXISTS `Assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `question` varchar(20) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assignment`
--

LOCK TABLES `Assignment` WRITE;
/*!40000 ALTER TABLE `Assignment` DISABLE KEYS */;
INSERT INTO `Assignment` VALUES (1,NULL,NULL,'math'),(2,NULL,1,'exam'),(3,NULL,1,'lab'),(4,NULL,3,'lab1'),(5,NULL,3,'lab2');
/*!40000 ALTER TABLE `Assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Student`
--

DROP TABLE IF EXISTS `Student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Student` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Student`
--

LOCK TABLES `Student` WRITE;
/*!40000 ALTER TABLE `Student` DISABLE KEYS */;
INSERT INTO `Student` VALUES (1,'Alice'),(2,'Bob');
/*!40000 ALTER TABLE `Student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Submission`
--

DROP TABLE IF EXISTS `Submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Submission` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `answer` varchar(20) DEFAULT NULL,
  `manualGrade` float(4,2) DEFAULT NULL,
  `assignment` int(11) DEFAULT NULL,
  `student` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Submission`
--

LOCK TABLES `Submission` WRITE;
/*!40000 ALTER TABLE `Submission` DISABLE KEYS */;
INSERT INTO `Submission` VALUES (1,NULL,NULL,1,1),(2,'Good',8.00,2,1),(3,NULL,NULL,3,1),(4,'Perfect',10.00,4,1),(5,'Sufficient',6.00,5,1),(6,NULL,NULL,1,2),(7,'Very Good',9.00,2,2),(8,NULL,NULL,3,2),(9,'Insufficient',3.00,4,2),(10,'Perfect',10.00,5,2);
/*!40000 ALTER TABLE `Submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `submissionfull`
--

DROP TABLE IF EXISTS `submissionfull`;
/*!50001 DROP VIEW IF EXISTS `submissionfull`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submissionfull` AS SELECT 
 1 AS `id`,
 1 AS `answer`,
 1 AS `manualGrade`,
 1 AS `assignment`,
 1 AS `student`,
 1 AS `parent`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submissionparent`
--

DROP TABLE IF EXISTS `submissionparent`;
/*!50001 DROP VIEW IF EXISTS `submissionparent`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submissionparent` AS SELECT 
 1 AS `id`,
 1 AS `parent`*/;
SET character_set_client = @saved_cs_client;

--
-- Current Database: `DerivedData`
--

USE `DerivedData`;

--
-- Final view structure for view `submissionfull`
--

/*!50001 DROP VIEW IF EXISTS `submissionfull`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submissionfull` AS select `submission`.`id` AS `id`,`submission`.`answer` AS `answer`,`submission`.`manualGrade` AS `manualGrade`,`submission`.`assignment` AS `assignment`,`submission`.`student` AS `student`,`submissionparent`.`parent` AS `parent` from (`submission` left join `submissionparent` on((`submission`.`id` = `submissionparent`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submissionparent`
--

/*!50001 DROP VIEW IF EXISTS `submissionparent`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submissionparent` AS select `s2`.`id` AS `id`,`s1`.`id` AS `parent` from (((`assignment` `a1` join `assignment` `a2` on((`a2`.`parent` = `a1`.`id`))) join `submission` `s1` on((`s1`.`assignment` = `a1`.`id`))) join `submission` `s2` on((`s2`.`assignment` = `a2`.`id`))) where (`s1`.`student` = `s2`.`student`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-17 11:30:59
