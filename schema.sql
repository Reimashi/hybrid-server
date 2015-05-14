SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema hybridserverdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hybridserverdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hybridserverdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `hybridserverdb` ;

-- -----------------------------------------------------
-- Table `hybridserverdb`.`html`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hybridserverdb`.`html` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`html` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  `size` BIGINT(20) NULL,
  `created` BIGINT(20) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hybridserverdb`.`xml`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hybridserverdb`.`xml` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xml` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  `size` BIGINT(20) NULL,
  `created` BIGINT(20) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hybridserverdb`.`xsd`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hybridserverdb`.`xsd` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xsd` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  `size` BIGINT(20) NULL,
  `created` BIGINT(20) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hybridserverdb`.`xslt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hybridserverdb`.`xslt` ;

CREATE TABLE IF NOT EXISTS `hybridserverdb`.`xslt` (
  `uuid` CHAR(36) NOT NULL,
  `content` TEXT NOT NULL,
  `xsd` CHAR(36) NULL,
  `size` BIGINT(20) NULL,
  `created` BIGINT(20) NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO dai@localhost;
 DROP USER dai@localhost;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'dai'@'localhost' IDENTIFIED BY 'daipassword';

GRANT SELECT, INSERT, TRIGGER, UPDATE, DELETE ON TABLE `hybridserverdb`.* TO 'dai'@'localhost';
GRANT SELECT, INSERT, TRIGGER ON TABLE `hybridserverdb`.* TO 'dai'@'localhost';
GRANT SELECT ON TABLE `hybridserverdb`.* TO 'dai'@'localhost';
GRANT ALL ON `hybridserverdb`.* TO 'dai'@'localhost';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `hybridserverdb`.`html`
-- -----------------------------------------------------
START TRANSACTION;
USE `hybridserverdb`;
INSERT INTO `hybridserverdb`.`html` (`uuid`, `content`, `size`, `created`) VALUES ('d8848424-98fd-11e4-b8aa-123b93f75cba', '<!DOCTYPE html><html><head><title>Test HTML</title></head><body>HTML test page.</body></html>', NULL, NULL);

COMMIT;

