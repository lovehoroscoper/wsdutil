SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `godtips` ;
CREATE SCHEMA IF NOT EXISTS `godtips` DEFAULT CHARACTER SET utf8 ;
USE `godtips` ;

-- -----------------------------------------------------
-- Table `godtips`.`s_indexmicroblog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godtips`.`s_indexmicroblog` ;

CREATE  TABLE IF NOT EXISTS `godtips`.`s_indexmicroblog` (
  `id` VARCHAR(20) NOT NULL COMMENT '所有文章使用同一的ID生成器' ,
  `userid` VARCHAR(30) NULL DEFAULT NULL ,
  `title` VARCHAR(100) NULL DEFAULT NULL ,
  `content` VARCHAR(300) NULL DEFAULT NULL ,
  `createdate` DATE NULL DEFAULT NULL ,
  `isoriginal` TINYINT(4) NULL DEFAULT NULL ,
  `reproducedurl` VARCHAR(300) NULL DEFAULT NULL ,
  `sourcetype` TINYINT(4) NULL DEFAULT NULL ,
  `reproducedauthor` VARCHAR(50) NULL DEFAULT NULL ,
  `apprstatus` TINYINT(4) NULL DEFAULT NULL ,
  `approver` VARCHAR(30) NULL DEFAULT NULL ,
  `apprtime` DATE NULL DEFAULT NULL ,
  `validstatus` TINYINT(4) NULL DEFAULT NULL COMMENT '是否公开等等' ,
  `validoptdate` DATE NULL DEFAULT NULL ,
  `articletype` VARCHAR(10) NULL DEFAULT NULL COMMENT '是微博、还是评论、还是其他的什么种类' ,
  `forumtype` VARCHAR(10) NULL DEFAULT NULL ,
  `priority` TINYINT(4) NULL DEFAULT NULL ,
  `usetype` TINYINT(4) NULL DEFAULT NULL ,
  `ismodify` SMALLINT(6) NULL DEFAULT NULL ,
  `lastdate` DATE NULL DEFAULT NULL ,
  `lastopter` VARCHAR(30) NULL DEFAULT NULL ,
  `isused` TINYINT(1) NULL DEFAULT NULL ,
  `useddate` DATE NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 's_系统主页内容微语录' ;


-- -----------------------------------------------------
-- Table `godtips`.`u_comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godtips`.`u_comment` ;

CREATE  TABLE IF NOT EXISTS `godtips`.`u_comment` (
  `id` VARCHAR(20) NOT NULL ,
  `articleid` VARCHAR(20) NULL DEFAULT NULL ,
  `validstatus` TINYINT(4) NULL DEFAULT NULL ,
  `userid` VARCHAR(30) NULL DEFAULT NULL ,
  `apprstatus` TINYINT(4) NULL DEFAULT NULL ,
  `approver` VARCHAR(30) NULL DEFAULT NULL ,
  `apprtime` DATE NULL DEFAULT NULL ,
  `comtent` VARCHAR(100) NULL DEFAULT NULL ,
  `supporttype` TINYINT(4) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8, 
COMMENT = '评论' ;


-- -----------------------------------------------------
-- Table `godtips`.`u_favorites`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godtips`.`u_favorites` ;

CREATE  TABLE IF NOT EXISTS `godtips`.`u_favorites` (
  `id` VARBINARY(20) NOT NULL ,
  `articleid` VARCHAR(20) NULL DEFAULT NULL ,
  `createdate` DATE NULL DEFAULT NULL ,
  `validstatus` TINYINT(4) NULL DEFAULT NULL ,
  `userid` VARCHAR(30) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8, 
COMMENT = '收藏夹,收入文章、评论等等' ;


-- -----------------------------------------------------
-- Table `godtips`.`u_microblogging`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godtips`.`u_microblogging` ;

CREATE  TABLE IF NOT EXISTS `godtips`.`u_microblogging` (
  `id` VARCHAR(20) NOT NULL COMMENT '所有文章使用同一的ID生成器' ,
  `userid` VARCHAR(30) NULL DEFAULT NULL ,
  `title` VARCHAR(100) NULL DEFAULT NULL ,
  `content` VARCHAR(300) NULL DEFAULT NULL ,
  `createdate` DATE NULL DEFAULT NULL ,
  `lastdate` DATE NULL DEFAULT NULL ,
  `isoriginal` TINYINT(4) NULL DEFAULT NULL ,
  `reproducedurl` VARCHAR(300) NULL DEFAULT NULL ,
  `sourcetype` TINYINT(4) NULL DEFAULT NULL ,
  `reproducedauthor` VARCHAR(50) NULL DEFAULT NULL ,
  `apprstatus` TINYINT(4) NULL DEFAULT NULL ,
  `approver` VARCHAR(30) NULL DEFAULT NULL ,
  `apprtime` DATE NULL DEFAULT NULL ,
  `validstatus` TINYINT(4) NULL DEFAULT NULL COMMENT '是否公开等等' ,
  `articletype` VARCHAR(10) NULL DEFAULT NULL COMMENT '是微博、还是评论、还是其他的什么种类' ,
  `forumtype` VARCHAR(10) NULL DEFAULT NULL ,
  `validoptdate` DATE NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8, 
COMMENT = '发表言论、微语录、微博' ;


-- -----------------------------------------------------
-- Table `godtips`.`u_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godtips`.`u_user` ;

CREATE  TABLE IF NOT EXISTS `godtips`.`u_user` (
  `userid` VARCHAR(30) NOT NULL ,
  `username` VARCHAR(30) NULL DEFAULT NULL ,
  `password` VARCHAR(30) NOT NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `phone` BIGINT(20) NULL ,
  `logintype` TINYINT(4) NULL DEFAULT NULL ,
  `statustype` TINYINT(4) NULL DEFAULT NULL ,
  `createdate` DATE NULL DEFAULT NULL ,
  `securitytip` TINYINT(4) NULL DEFAULT NULL ,
  `receivetype` TINYINT(4) NULL DEFAULT NULL ,
  `createip` VARCHAR(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`userid`) ,
  UNIQUE INDEX `ind_email` (`email` ASC) ,
  UNIQUE INDEX `userid_UNIQUE` (`userid` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8, 
COMMENT = '用户信息表' ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
