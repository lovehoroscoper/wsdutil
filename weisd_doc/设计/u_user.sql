/*
Navicat MySQL Data Transfer

Source Server         : 11
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : godtips

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-07-08 22:26:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `u_user`
-- ----------------------------
DROP TABLE IF EXISTS `u_user`;
CREATE TABLE `u_user` (
  `userid` varchar(30) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` bigint(20) DEFAULT NULL,
  `logintype` tinyint(4) DEFAULT NULL,
  `statustype` tinyint(4) DEFAULT NULL,
  `createdate` date DEFAULT NULL,
  `securitytip` tinyint(4) DEFAULT NULL,
  `receivetype` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `ind_userid` (`userid`),
  UNIQUE KEY `ind_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of u_user
-- ----------------------------
