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
-- Table structure for table `Submission1`
--

DROP TABLE IF EXISTS `Submission1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Submission1` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `answer` varchar(20) DEFAULT NULL,
  `manualGrade` float(4,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Submission1`
--

LOCK TABLES `Submission1` WRITE;
/*!40000 ALTER TABLE `Submission1` DISABLE KEYS */;
INSERT INTO `Submission1` VALUES (1,NULL,NULL),(2,NULL,NULL);
/*!40000 ALTER TABLE `Submission1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Submission2`
--

DROP TABLE IF EXISTS `Submission2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Submission2` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parent` int(10) NOT NULL,
  `answer` varchar(20) DEFAULT NULL,
  `manualGrade` float(4,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Submission2`
--

LOCK TABLES `Submission2` WRITE;
/*!40000 ALTER TABLE `Submission2` DISABLE KEYS */;
INSERT INTO `Submission2` VALUES (1,1,'Good',8.00),(2,1,NULL,NULL),(3,2,'Very Good',9.00),(4,2,NULL,NULL);
/*!40000 ALTER TABLE `Submission2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Submission3`
--

DROP TABLE IF EXISTS `Submission3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Submission3` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parent` int(10) NOT NULL,
  `answer` varchar(20) DEFAULT NULL,
  `manualGrade` float(4,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Submission3`
--

LOCK TABLES `Submission3` WRITE;
/*!40000 ALTER TABLE `Submission3` DISABLE KEYS */;
INSERT INTO `Submission3` VALUES (1,2,'Perfect',10.00),(2,2,'Sufficient',6.00),(3,4,'Insufficient',3.00),(4,4,'Perfect',10.00);
/*!40000 ALTER TABLE `Submission3` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `submission1childgrade`
--

DROP TABLE IF EXISTS `submission1childgrade`;
/*!50001 DROP VIEW IF EXISTS `submission1childgrade`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission1childgrade` AS SELECT 
 1 AS `id`,
 1 AS `childGrade`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission1childpass`
--

DROP TABLE IF EXISTS `submission1childpass`;
/*!50001 DROP VIEW IF EXISTS `submission1childpass`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission1childpass` AS SELECT 
 1 AS `id`,
 1 AS `childPass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission1full`
--

DROP TABLE IF EXISTS `submission1full`;
/*!50001 DROP VIEW IF EXISTS `submission1full`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission1full` AS SELECT 
 1 AS `id`,
 1 AS `answer`,
 1 AS `manualGrade`,
 1 AS `childGrade`,
 1 AS `childPass`,
 1 AS `grade`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission1grade`
--

DROP TABLE IF EXISTS `submission1grade`;
/*!50001 DROP VIEW IF EXISTS `submission1grade`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission1grade` AS SELECT 
 1 AS `id`,
 1 AS `grade`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission1pass`
--

DROP TABLE IF EXISTS `submission1pass`;
/*!50001 DROP VIEW IF EXISTS `submission1pass`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission1pass` AS SELECT 
 1 AS `id`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission2childgrade`
--

DROP TABLE IF EXISTS `submission2childgrade`;
/*!50001 DROP VIEW IF EXISTS `submission2childgrade`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission2childgrade` AS SELECT 
 1 AS `id`,
 1 AS `childGrade`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission2childpass`
--

DROP TABLE IF EXISTS `submission2childpass`;
/*!50001 DROP VIEW IF EXISTS `submission2childpass`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission2childpass` AS SELECT 
 1 AS `id`,
 1 AS `childPass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission2full`
--

DROP TABLE IF EXISTS `submission2full`;
/*!50001 DROP VIEW IF EXISTS `submission2full`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission2full` AS SELECT 
 1 AS `id`,
 1 AS `parent`,
 1 AS `answer`,
 1 AS `manualGrade`,
 1 AS `childGrade`,
 1 AS `childPass`,
 1 AS `grade`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission2grade`
--

DROP TABLE IF EXISTS `submission2grade`;
/*!50001 DROP VIEW IF EXISTS `submission2grade`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission2grade` AS SELECT 
 1 AS `id`,
 1 AS `grade`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission2pass`
--

DROP TABLE IF EXISTS `submission2pass`;
/*!50001 DROP VIEW IF EXISTS `submission2pass`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission2pass` AS SELECT 
 1 AS `id`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission3full`
--

DROP TABLE IF EXISTS `submission3full`;
/*!50001 DROP VIEW IF EXISTS `submission3full`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission3full` AS SELECT 
 1 AS `id`,
 1 AS `parent`,
 1 AS `answer`,
 1 AS `manualGrade`,
 1 AS `grade`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission3grade`
--

DROP TABLE IF EXISTS `submission3grade`;
/*!50001 DROP VIEW IF EXISTS `submission3grade`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission3grade` AS SELECT 
 1 AS `id`,
 1 AS `grade`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `submission3pass`
--

DROP TABLE IF EXISTS `submission3pass`;
/*!50001 DROP VIEW IF EXISTS `submission3pass`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `submission3pass` AS SELECT 
 1 AS `id`,
 1 AS `pass`*/;
SET character_set_client = @saved_cs_client;

--
-- Current Database: `DerivedData`
--

USE `DerivedData`;

--
-- Final view structure for view `submission1childgrade`
--

/*!50001 DROP VIEW IF EXISTS `submission1childgrade`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission1childgrade` AS select `submission2full`.`parent` AS `id`,avg(`submission2full`.`grade`) AS `childGrade` from `submission2full` group by `submission2full`.`parent` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission1childpass`
--

/*!50001 DROP VIEW IF EXISTS `submission1childpass`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission1childpass` AS select `submission2full`.`parent` AS `id`,bit_and(`submission2full`.`pass`) AS `childPass` from `submission2full` group by `submission2full`.`parent` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission1full`
--

/*!50001 DROP VIEW IF EXISTS `submission1full`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission1full` AS select `submission1`.`id` AS `id`,`submission1`.`answer` AS `answer`,`submission1`.`manualGrade` AS `manualGrade`,`submission1childgrade`.`childGrade` AS `childGrade`,`submission1childpass`.`childPass` AS `childPass`,`submission1grade`.`grade` AS `grade`,`submission1pass`.`pass` AS `pass` from ((((`submission1` join `submission1childgrade` on((`submission1`.`id` = `submission1childgrade`.`id`))) join `submission1childpass` on((`submission1`.`id` = `submission1childpass`.`id`))) join `submission1grade` on((`submission1`.`id` = `submission1grade`.`id`))) join `submission1pass` on((`submission1`.`id` = `submission1pass`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission1grade`
--

/*!50001 DROP VIEW IF EXISTS `submission1grade`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission1grade` AS select `submission1`.`id` AS `id`,if((`submission1`.`manualGrade` is not null),`submission1`.`manualGrade`,if(`submission1childpass`.`childPass`,`submission1childgrade`.`childGrade`,NULL)) AS `grade` from ((`submission1` join `submission1childgrade` on((`submission1`.`id` = `submission1childgrade`.`id`))) join `submission1childpass` on((`submission1`.`id` = `submission1childpass`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission1pass`
--

/*!50001 DROP VIEW IF EXISTS `submission1pass`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission1pass` AS select `submission1grade`.`id` AS `id`,if((`submission1grade`.`grade` is not null),(`submission1grade`.`grade` >= 5.5),0) AS `pass` from `submission1grade` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission2childgrade`
--

/*!50001 DROP VIEW IF EXISTS `submission2childgrade`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission2childgrade` AS select `submission3full`.`parent` AS `id`,avg(`submission3full`.`grade`) AS `childGrade` from `submission3full` group by `submission3full`.`parent` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission2childpass`
--

/*!50001 DROP VIEW IF EXISTS `submission2childpass`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission2childpass` AS select `submission3full`.`parent` AS `id`,bit_and(`submission3full`.`pass`) AS `childPass` from `submission3full` group by `submission3full`.`parent` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission2full`
--

/*!50001 DROP VIEW IF EXISTS `submission2full`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission2full` AS select `submission2`.`id` AS `id`,`submission2`.`parent` AS `parent`,`submission2`.`answer` AS `answer`,`submission2`.`manualGrade` AS `manualGrade`,`submission2childgrade`.`childGrade` AS `childGrade`,`submission2childpass`.`childPass` AS `childPass`,`submission2grade`.`grade` AS `grade`,`submission2pass`.`pass` AS `pass` from ((((`submission2` join `submission2childgrade` on((`submission2`.`id` = `submission2childgrade`.`id`))) join `submission2childpass` on((`submission2`.`id` = `submission2childpass`.`id`))) join `submission2grade` on((`submission2`.`id` = `submission2grade`.`id`))) join `submission2pass` on((`submission2`.`id` = `submission2pass`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission2grade`
--

/*!50001 DROP VIEW IF EXISTS `submission2grade`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission2grade` AS select `submission2`.`id` AS `id`,if((`submission2`.`manualGrade` is not null),`submission2`.`manualGrade`,if(`submission2childpass`.`childPass`,`submission2childgrade`.`childGrade`,NULL)) AS `grade` from ((`submission2` join `submission2childgrade` on((`submission2`.`id` = `submission2childgrade`.`id`))) join `submission2childpass` on((`submission2`.`id` = `submission2childpass`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission2pass`
--

/*!50001 DROP VIEW IF EXISTS `submission2pass`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission2pass` AS select `submission2grade`.`id` AS `id`,if((`submission2grade`.`grade` is not null),(`submission2grade`.`grade` >= 5.5),0) AS `pass` from `submission2grade` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission3full`
--

/*!50001 DROP VIEW IF EXISTS `submission3full`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission3full` AS select `submission3`.`id` AS `id`,`submission3`.`parent` AS `parent`,`submission3`.`answer` AS `answer`,`submission3`.`manualGrade` AS `manualGrade`,`submission3grade`.`grade` AS `grade`,`submission3pass`.`pass` AS `pass` from ((`submission3` join `submission3grade` on((`submission3`.`id` = `submission3grade`.`id`))) join `submission3pass` on((`submission3`.`id` = `submission3pass`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission3grade`
--

/*!50001 DROP VIEW IF EXISTS `submission3grade`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission3grade` AS select `submission3`.`id` AS `id`,`submission3`.`manualGrade` AS `grade` from `submission3` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `submission3pass`
--

/*!50001 DROP VIEW IF EXISTS `submission3pass`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `submission3pass` AS select `submission3grade`.`id` AS `id`,(`submission3grade`.`grade` >= 5.5) AS `pass` from `submission3grade` */;
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

-- Dump completed on 2016-05-10 16:52:11
