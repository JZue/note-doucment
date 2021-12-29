# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.24-log)
# Database: note-mysql
# Generation Time: 2019-08-15 09:56:12 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table optimistic_lock
# ------------------------------------------------------------

DROP TABLE IF EXISTS `optimistic_lock`;

CREATE TABLE `optimistic_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `count` int(11) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `optimistic_lock` WRITE;
/*!40000 ALTER TABLE `optimistic_lock` DISABLE KEYS */;

INSERT INTO `optimistic_lock` (`id`, `count`, `version`)
VALUES
	(1,1000,0);

/*!40000 ALTER TABLE `optimistic_lock` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table pessimistic_lock
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pessimistic_lock`;

CREATE TABLE `pessimistic_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `pessimistic_lock` WRITE;
/*!40000 ALTER TABLE `pessimistic_lock` DISABLE KEYS */;

INSERT INTO `pessimistic_lock` (`id`, `count`)
VALUES
	(1,1000);

/*!40000 ALTER TABLE `pessimistic_lock` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
