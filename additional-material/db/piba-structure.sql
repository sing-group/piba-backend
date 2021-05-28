-- MySQL dump 10.13  Distrib 5.7.34, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: piba
-- ------------------------------------------------------
-- Server version	5.7.34-0ubuntu0.18.04.1

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
-- Table structure for table `exploration`
--

DROP TABLE IF EXISTS `exploration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exploration` (
  `id` varchar(255) NOT NULL,
  `confirmed` bit(1) DEFAULT b'0',
  `creation_date` datetime(3) DEFAULT NULL,
  `date` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `patient_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3k6y5t4lfwf0kjxaj85q0wfw9` (`title`),
  KEY `FKnjhxl3llmjfe4y0tr39jpoxyk` (`patient_id`),
  CONSTRAINT `FKnjhxl3llmjfe4y0tr39jpoxyk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gallery`
--

DROP TABLE IF EXISTS `gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gallery` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_i58ueicaylttcvuys7reb4nms` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `idspace`
--

DROP TABLE IF EXISTS `idspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idspace` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o3ytwwkm706hwu5cnmxwb61s6` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `is_removed` bit(1) DEFAULT NULL,
  `manually_selected` bit(1) DEFAULT b'0',
  `num_frame` int(11) DEFAULT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `observation_to_remove` varchar(255) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `gallery_id` varchar(255) DEFAULT NULL,
  `polyp_id` varchar(255) DEFAULT NULL,
  `video_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgmnxnktphh1jne2xvnv5mwaom` (`gallery_id`),
  KEY `FK5xe6y2bg1o8qv9pavwov13im2` (`polyp_id`),
  KEY `FKbn0q0jvgc44ueon5669d3a9re` (`video_id`),
  CONSTRAINT `FK5xe6y2bg1o8qv9pavwov13im2` FOREIGN KEY (`polyp_id`) REFERENCES `polyp` (`id`),
  CONSTRAINT `FKbn0q0jvgc44ueon5669d3a9re` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`),
  CONSTRAINT `FKgmnxnktphh1jne2xvnv5mwaom` FOREIGN KEY (`gallery_id`) REFERENCES `gallery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `modifier`
--

DROP TABLE IF EXISTS `modifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modifier` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sbrcslcmyf73s0mqph1fuuaia` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password_recovery`
--

DROP TABLE IF EXISTS `password_recovery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_recovery` (
  `login` varchar(100) NOT NULL,
  `date` datetime NOT NULL,
  `uuid` varchar(255) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `id` varchar(255) NOT NULL,
  `birthdate` datetime DEFAULT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `patientID` varchar(255) NOT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `idSpace_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsrp8fqmubr994csw6ask0d6le` (`patientID`,`idSpace_id`),
  KEY `FKbj5xg16iucovnpfwtvncc8en1` (`idSpace_id`),
  CONSTRAINT `FKbj5xg16iucovnpfwtvncc8en1` FOREIGN KEY (`idSpace_id`) REFERENCES `idspace` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polyp`
--

DROP TABLE IF EXISTS `polyp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polyp` (
  `id` varchar(255) NOT NULL,
  `confirmed` bit(1) DEFAULT b'0',
  `creation_date` datetime(3) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `lst` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `nice` varchar(255) DEFAULT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `paris_primary` varchar(255) DEFAULT NULL,
  `paris_secondary` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `wasp` varchar(255) DEFAULT NULL,
  `exploration_id` varchar(255) DEFAULT NULL,
  `histology_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKftme4iaq483pir4orguud9ogw` (`exploration_id`),
  KEY `FK9gkn9b6jytal22si0n5oex0mp` (`histology_id`),
  CONSTRAINT `FK9gkn9b6jytal22si0n5oex0mp` FOREIGN KEY (`histology_id`) REFERENCES `polyphistology` (`id`),
  CONSTRAINT `FKftme4iaq483pir4orguud9ogw` FOREIGN KEY (`exploration_id`) REFERENCES `exploration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polypdataset`
--

DROP TABLE IF EXISTS `polypdataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polypdataset` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `description` text NOT NULL,
  `title` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `defaultGallery_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpwnvix38d499x2gv2cfu6ukvd` (`defaultGallery_id`),
  CONSTRAINT `FKpwnvix38d499x2gv2cfu6ukvd` FOREIGN KEY (`defaultGallery_id`) REFERENCES `gallery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polyphistology`
--

DROP TABLE IF EXISTS `polyphistology`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polyphistology` (
  `DTYPE` varchar(31) NOT NULL,
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `ssadysplasia` varchar(255) DEFAULT NULL,
  `adenomadysplasing` varchar(255) DEFAULT NULL,
  `adenomatype` varchar(255) DEFAULT NULL,
  `tsadysplasia` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polyplocation`
--

DROP TABLE IF EXISTS `polyplocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polyplocation` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `height` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `image_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpsxmaocdia9tdghw0i48o3l5i` (`image_id`),
  CONSTRAINT `FKpsxmaocdia9tdghw0i48o3l5i` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polyprecording`
--

DROP TABLE IF EXISTS `polyprecording`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polyprecording` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `end` int(11) NOT NULL,
  `start` int(11) NOT NULL,
  `confirmed` bit(1) DEFAULT b'0',
  `creation_date` datetime(3) DEFAULT NULL,
  `polyp_id` varchar(255) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `video_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ajas2207mil7asskvt2gl2kw0` (`id`,`polyp_id`),
  UNIQUE KEY `UK3b5be1u27giv88biccgx1plgn` (`video_id`,`polyp_id`,`start`,`end`),
  KEY `FK3edmv3619hxs7ae76gtlmae0n` (`polyp_id`),
  CONSTRAINT `FK3edmv3619hxs7ae76gtlmae0n` FOREIGN KEY (`polyp_id`) REFERENCES `polyp` (`id`),
  CONSTRAINT `FKahq84qib3dgahokten4udjfie` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polypsindataset`
--

DROP TABLE IF EXISTS `polypsindataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polypsindataset` (
  `polypdataset_id` varchar(255) NOT NULL,
  `polyp_id` varchar(255) NOT NULL,
  PRIMARY KEY (`polypdataset_id`,`polyp_id`),
  KEY `FK8y6kkfdnr7q6h6m219n6wuh71` (`polyp_id`),
  CONSTRAINT `FK8y6kkfdnr7q6h6m219n6wuh71` FOREIGN KEY (`polyp_id`) REFERENCES `polyp` (`id`),
  CONSTRAINT `FKq58kvjdh187y3jtfonnvetmnl` FOREIGN KEY (`polypdataset_id`) REFERENCES `polypdataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reviewedpolyprecording`
--

DROP TABLE IF EXISTS `reviewedpolyprecording`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviewedpolyprecording` (
  `polypdataset_id` varchar(255) NOT NULL,
  `polyp_id` varchar(255) NOT NULL,
  `polyprecording_id` int(11) NOT NULL,
  PRIMARY KEY (`polypdataset_id`,`polyp_id`,`polyprecording_id`),
  KEY `FKsqgc9turahof2g8y68xoiuqcx` (`polyprecording_id`,`polyp_id`),
  CONSTRAINT `FK8natb394jea7twfi934a4spql` FOREIGN KEY (`polypdataset_id`, `polyp_id`) REFERENCES `polypsindataset` (`polypdataset_id`, `polyp_id`),
  CONSTRAINT `FKsqgc9turahof2g8y68xoiuqcx` FOREIGN KEY (`polyprecording_id`, `polyp_id`) REFERENCES `polyprecording` (`id`, `polyp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `login` varchar(100) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `email` varchar(120) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(255) NOT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `id` varchar(255) NOT NULL,
  `creation_date` datetime(3) DEFAULT NULL,
  `fps` int(11) DEFAULT NULL,
  `is_processing` bit(1) DEFAULT NULL,
  `observations` varchar(3000) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `with_text` bit(1) DEFAULT NULL,
  `exploration_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3yhlg93n9mtm4djhb67va6h5b` (`exploration_id`),
  CONSTRAINT `FK3yhlg93n9mtm4djhb67va6h5b` FOREIGN KEY (`exploration_id`) REFERENCES `exploration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `videomodification`
--

DROP TABLE IF EXISTS `videomodification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `videomodification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `end` int(11) NOT NULL,
  `start` int(11) NOT NULL,
  `confirmed` bit(1) DEFAULT b'0',
  `creation_date` datetime(3) DEFAULT NULL,
  `update_date` datetime(3) DEFAULT NULL,
  `modifier_id` varchar(255) DEFAULT NULL,
  `video_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaa10u3drw60wmiobh2d6dsv37` (`video_id`,`modifier_id`,`start`,`end`),
  KEY `FKtob22gaa4c18vaivyqutckh41` (`modifier_id`),
  CONSTRAINT `FKgu49tdj4htc2b47etvu705yam` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`),
  CONSTRAINT `FKtob22gaa4c18vaivyqutckh41` FOREIGN KEY (`modifier_id`) REFERENCES `modifier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-28 16:39:15
