--liquibase formatted sql

--preconditions onFail:HALT onError:HALT

--changeset milos:1_main
--comment: Creating the tables.

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `student_helper` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `student_helper` ;

-- -----------------------------------------------------
-- Table `student_helper`.`authorities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`authorities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `authority` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 27
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_helper`.`user_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`user_details` (
  `id` BIGINT NOT NULL,
  `first_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT NULL,
  `birthday` TIMESTAMP(2) NULL DEFAULT NULL,
  `gender` VARCHAR(45) NULL DEFAULT NULL,
  `country` VARCHAR(45) NULL DEFAULT NULL,
  `create_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_helper`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `enabled` TINYINT(1) NOT NULL DEFAULT '1',
  `user_details_id` BIGINT NULL DEFAULT NULL,
  `create_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `user_details_id_UNIQUE` (`user_details_id` ASC) VISIBLE,
  INDEX `fk_user_details_idx` (`user_details_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_details`
    FOREIGN KEY (`user_details_id`)
    REFERENCES `student_helper`.`user_details` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_helper`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` TINYTEXT NULL DEFAULT NULL,
  `due` TIMESTAMP NULL DEFAULT NULL,
  `create_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `student_helper`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_helper`.`motivation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`motivation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `body` TEXT NULL DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  `create_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_motiv_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_motiv_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `student_helper`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_helper`.`user_authority`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_helper`.`user_authority` (
  `user_id` BIGINT NOT NULL,
  `authority_id` BIGINT NOT NULL,
  INDEX `user_fk_idx` (`user_id` ASC) INVISIBLE,
  INDEX `authority_fk_idx` (`authority_id` ASC) VISIBLE,
  CONSTRAINT `authority_fk`
    FOREIGN KEY (`authority_id`)
    REFERENCES `student_helper`.`authorities` (`id`),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `student_helper`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
