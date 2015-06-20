SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `hybridserverdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `hybridserverdb` ;

DROP TABLE IF EXISTS `hybridserverdb`.`html` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`html` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `hybridserverdb`.`xml` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xml` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `hybridserverdb`.`xsd` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xsd` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `hybridserverdb`.`xslt` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xslt` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  `xsd` CHAR(36) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO dai@localhost;
 DROP USER dai@localhost;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'dai'@'localhost' IDENTIFIED BY 'daipassword';

GRANT ALL ON `hybridserverdb`.* TO 'dai'@'localhost';

COMMIT;

