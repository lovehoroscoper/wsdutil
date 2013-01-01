/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : mysso

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2012-12-29 20:17:48
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `sso_acl_rolesite`
-- ----------------------------
DROP TABLE IF EXISTS `sso_acl_rolesite`;
CREATE TABLE `sso_acl_rolesite` (
  `id_acl_site` int(11) NOT NULL,
  `principal_type` varchar(10) DEFAULT NULL COMMENT '使用类型，是使用角色还是直接给用户等等',
  `principal_id` int(11) DEFAULT NULL COMMENT '主体ID',
  `id_site` int(11) DEFAULT NULL COMMENT '站点ID',
  `acl_state` int(11) DEFAULT NULL COMMENT '权限CRUD',
  `valid_type` tinyint(4) DEFAULT NULL COMMENT '有效类型，对于多种权限并存的如 角色权限与用户直接权限的并存',
  PRIMARY KEY (`id_acl_site`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限:给角色的权限或者给用户直接权限';

-- ----------------------------
-- Records of sso_acl_rolesite
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_role`
-- ----------------------------
DROP TABLE IF EXISTS `sso_role`;
CREATE TABLE `sso_role` (
  `id_role` int(11) NOT NULL,
  `role_name` varchar(45) NOT NULL COMMENT '名称',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `role_status` tinyint(4) DEFAULT NULL COMMENT '角色状态',
  PRIMARY KEY (`id_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sso_role
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_site`
-- ----------------------------
DROP TABLE IF EXISTS `sso_site`;
CREATE TABLE `sso_site` (
  `id_site` int(11) NOT NULL COMMENT '主键',
  `sitename` varchar(100) NOT NULL COMMENT '来源标志名称',
  `sitedesc` varchar(200) DEFAULT NULL COMMENT '名称',
  `sitestatus` tinyint(4) NOT NULL COMMENT '状态',
  `sitetype` int(11) NOT NULL COMMENT '站点类型',
  `parenttype` int(11) NOT NULL COMMENT '父亲类型',
  `isreply` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否允许回复，评论',
  `id_user` int(11) NOT NULL COMMENT 'owner该来源的所有者',
  `url1` varchar(300) DEFAULT NULL COMMENT '异常或者限制之后备用跳转URL',
  `descurl1` varchar(100) DEFAULT NULL COMMENT '异常或者限制之后备用跳转URL描述',
  `url2` varchar(300) DEFAULT NULL,
  `descurl2` varchar(100) DEFAULT NULL,
  `url3` varchar(300) DEFAULT NULL,
  `descurl3` varchar(100) DEFAULT NULL,
  `siteip` varchar(500) DEFAULT NULL COMMENT '站点IP',
  `sitedomain` varchar(500) DEFAULT NULL COMMENT '站点域名',
  `ipordomain` tinyint(4) DEFAULT NULL COMMENT '使用IP还是域名认证，限制',
  PRIMARY KEY (`id_site`),
  UNIQUE KEY `sitename_UNIQUE` (`sitename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站点资源表';

-- ----------------------------
-- Records of sso_site
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_sys_acl_sourcerole`
-- ----------------------------
DROP TABLE IF EXISTS `sso_sys_acl_sourcerole`;
CREATE TABLE `sso_sys_acl_sourcerole` (
  `id_acl` int(11) NOT NULL,
  `principal_type` varchar(10) DEFAULT NULL COMMENT '使用类型，是使用角色还是直接给用户等等',
  `principal_id` int(11) DEFAULT NULL COMMENT '主体ID，如id_source',
  `id_role` int(11) DEFAULT NULL COMMENT '角色ID',
  `acl_state` int(11) DEFAULT NULL COMMENT '权限CRUD',
  `valid_type` tinyint(4) DEFAULT NULL COMMENT '有效类型，对于多种权限并存的如 角色权限与用户直接权限的并存',
  PRIMARY KEY (`id_acl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限:给角色的权限或者给用户直接权限';

-- ----------------------------
-- Records of sso_sys_acl_sourcerole
-- ----------------------------
INSERT INTO `sso_sys_acl_sourcerole` VALUES ('1', '0', '31', '2', null, null);
INSERT INTO `sso_sys_acl_sourcerole` VALUES ('2', '0', '32', '2', null, null);

-- ----------------------------
-- Table structure for `sso_sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sso_sys_role`;
CREATE TABLE `sso_sys_role` (
  `id_role` int(11) NOT NULL,
  `role_key` varchar(45) NOT NULL COMMENT '名称',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `role_status` tinyint(4) DEFAULT NULL COMMENT '角色状态',
  `role_sort` int(10) NOT NULL DEFAULT '99' COMMENT '排序',
  PRIMARY KEY (`id_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sso_sys_role
-- ----------------------------
INSERT INTO `sso_sys_role` VALUES ('1', 'ROLE_DEFAULT_NULL', '未配置访问', '2012-08-01 20:35:35', '1', '99');
INSERT INTO `sso_sys_role` VALUES ('2', 'ROLE_SYS1_MG', '系统1管理员', '2012-10-30 17:01:00', '1', '99');

-- ----------------------------
-- Table structure for `sso_sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sso_sys_user_role`;
CREATE TABLE `sso_sys_user_role` (
  `id_user_role` int(11) NOT NULL COMMENT '用户角色',
  `id_user` int(11) DEFAULT NULL COMMENT '用户ID',
  `id_role` int(11) DEFAULT NULL COMMENT '角色ID',
  `orderno` int(11) DEFAULT NULL COMMENT '优先级',
  PRIMARY KEY (`id_user_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户--角色表';

-- ----------------------------
-- Records of sso_sys_user_role
-- ----------------------------
INSERT INTO `sso_sys_user_role` VALUES ('1', '1', '2', '1');
INSERT INTO `sso_sys_user_role` VALUES ('2', '1', '3', null);

-- ----------------------------
-- Table structure for `sso_type_encrypt`
-- ----------------------------
DROP TABLE IF EXISTS `sso_type_encrypt`;
CREATE TABLE `sso_type_encrypt` (
  `idsso_type_encrypt` int(11) NOT NULL,
  PRIMARY KEY (`idsso_type_encrypt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sso_type_encrypt
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_type_login`
-- ----------------------------
DROP TABLE IF EXISTS `sso_type_login`;
CREATE TABLE `sso_type_login` (
  `idsso_type_login` int(11) NOT NULL,
  PRIMARY KEY (`idsso_type_login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sso_type_login
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_user`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户登录用户名，与站点来源联合唯一.本地用户名：邮箱',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nikename` varchar(50) DEFAULT NULL COMMENT '昵称',
  `id_site` int(11) NOT NULL COMMENT '站点来源ID,与sso_sitesource的主键',
  `logintype` tinyint(4) DEFAULT NULL COMMENT '登录类型，sso_type_login',
  `validtype` tinyint(4) DEFAULT NULL COMMENT '是否有效，或者锁定等等',
  `createdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `encrypttype` tinyint(4) DEFAULT NULL COMMENT '加密类型',
  `thirdloginid` varchar(100) DEFAULT NULL COMMENT '第三方登录帐号',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_UNIQUE` (`id_user`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sso_user
-- ----------------------------
INSERT INTO `sso_user` VALUES ('1', 'admin', '25dc371ffab906626148050fd92ac28e979d4ec73635941bc72ceb06277b6e15ec06bfb9c0c98901', 'admin', '1', '1', '0', '2011-12-12 12:44:21', '1', '1', 'admin');

-- ----------------------------
-- Table structure for `sso_user_limittime`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_limittime`;
CREATE TABLE `sso_user_limittime` (
  `idsso_user_limittime` int(11) NOT NULL,
  PRIMARY KEY (`idsso_user_limittime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户限制时间，比如限制某些时间段无法访问';

-- ----------------------------
-- Records of sso_user_limittime
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_role`;
CREATE TABLE `sso_user_role` (
  `id_user_role` int(11) NOT NULL COMMENT '用户角色',
  `id_user` int(11) DEFAULT NULL COMMENT '用户ID',
  `id_role` int(11) DEFAULT NULL COMMENT '角色ID',
  `orderno` int(11) DEFAULT NULL COMMENT '优先级',
  PRIMARY KEY (`id_user_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户--角色表';

-- ----------------------------
-- Records of sso_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_user_site_exclude`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_site_exclude`;
CREATE TABLE `sso_user_site_exclude` (
  `id_exclude` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_site` int(11) NOT NULL,
  `validtype` tinyint(4) NOT NULL,
  `limittype` tinyint(4) NOT NULL COMMENT '限制类型。\n来源限制，目的地限制，中心限制',
  `lasttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最好一次修改时间',
  PRIMARY KEY (`id_exclude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户--站点--互斥表\n因为用户拥有角色-->权限-->站点资源，但是某些站点资源不能给该用户使用';

-- ----------------------------
-- Records of sso_user_site_exclude
-- ----------------------------

-- ----------------------------
-- Table structure for `sso_user_third`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_third`;
CREATE TABLE `sso_user_third` (
  `providertype` varchar(30) NOT NULL,
  `providerid` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `createdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sso_user_third
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `subsysid` tinyint(4) DEFAULT NULL COMMENT '子系统ID',
  `menuname` varchar(30) DEFAULT NULL COMMENT '菜单名称',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态：0 无线 1 有效 2 删除',
  `isfunction` tinyint(1) DEFAULT NULL COMMENT '是否功能菜单',
  `parentid` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `linkurl` varchar(500) DEFAULT NULL COMMENT '菜单连接地址',
  `levelflag` int(11) DEFAULT NULL COMMENT '菜单级别',
  `sort` int(11) DEFAULT NULL COMMENT '菜单排序',
  `createtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `opterid` varchar(100) DEFAULT NULL COMMENT '创建人',
  `systype` int(5) DEFAULT NULL COMMENT '系统类型：0:admin',
  `isadmin` tinyint(1) DEFAULT NULL COMMENT '是否是系统管理菜单，管理员只是访问系统管理菜单 0：否 1：是',
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('25', '1', '话费管理', '1', '0', '0', '', '1', '1', '2012-10-29 17:24:27', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('26', '1', '代理商管理', '1', '0', '0', '', '1', '2', '2012-10-29 17:24:34', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('27', '1', '报表管理', '1', '0', '0', '', '1', '3', '2012-10-29 17:24:43', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('28', '1', '对账管理', '1', '0', '0', '', '1', '3', '2012-10-29 17:24:48', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('29', '1', '订单管理', '1', '0', '25', '', '2', '1', '2012-10-29 17:59:14', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('30', '1', '货架管理', '1', '0', '25', '', '2', '2', '2012-10-29 17:59:30', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('31', '1', '添加菜单', '1', '1', '25', '/sysplat/menu/add.do', '3', '2', '2012-10-30 16:48:55', 'admin', '0', '0');
INSERT INTO `sys_menu` VALUES ('32', '1', 'sso', '1', '1', '25', '/services/manage.html', '3', '3', '2012-11-27 21:27:35', 'weisd', '0', '0');

-- ----------------------------
-- Table structure for `sys_menu_url`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_url`;
CREATE TABLE `sys_menu_url` (
  `menuid` int(11) NOT NULL COMMENT '菜单ID',
  `linkurl` varchar(500) NOT NULL COMMENT '菜单URL',
  `isenter` tinyint(1) DEFAULT '0' COMMENT '是否入库菜单 0 否，1 是'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu_url
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_subsystem`
-- ----------------------------
DROP TABLE IF EXISTS `sys_subsystem`;
CREATE TABLE `sys_subsystem` (
  `subsysid` int(11) NOT NULL AUTO_INCREMENT COMMENT '子系统ID',
  `subsysname` varchar(200) DEFAULT NULL COMMENT '子系统名称',
  `real_url` varchar(500) DEFAULT NULL COMMENT '正式环境URL',
  `test_url` varchar(500) DEFAULT NULL COMMENT '测试环境URL',
  `ismian` tinyint(1) DEFAULT NULL COMMENT '是否管理系统：0、是；1、否；',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态：0、正常;1、暂停;',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `pattern` varchar(50) DEFAULT NULL COMMENT '匹配的字符串',
  `matchtype` tinyint(4) DEFAULT NULL COMMENT '匹配类型：1:域名，2IP，3',
  `subsysip` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`subsysid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_subsystem
-- ----------------------------
INSERT INTO `sys_subsystem` VALUES ('1', '系统1', 'http://www.hao123.com/', 'http://www.hao123.com/', '1', '0', '1', null, null, null);
INSERT INTO `sys_subsystem` VALUES ('2', '系统2', 'http://www.hao123.com/', 'http://www.hao123.com/', '0', '0', '4', null, null, null);
INSERT INTO `sys_subsystem` VALUES ('3', '系统3', 'http://www.hao123.com/', 'http://www.hao123.com/', '0', '0', '3', null, null, null);

-- ----------------------------
-- Table structure for `sys_user_subsystem`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_subsystem`;
CREATE TABLE `sys_user_subsystem` (
  `userid` varchar(50) NOT NULL COMMENT '用户唯一标志',
  `subsysid` int(11) NOT NULL COMMENT '子系统ID',
  `isaccess` tinyint(2) DEFAULT '0' COMMENT '是否能访问：0、不行；1、可以'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_subsystem
-- ----------------------------
INSERT INTO `sys_user_subsystem` VALUES ('admin', '2', '1');
INSERT INTO `sys_user_subsystem` VALUES ('admin', '3', '1');
INSERT INTO `sys_user_subsystem` VALUES ('admin', '1', '1');

-- ----------------------------
-- Procedure structure for `MYCONVERT`
-- ----------------------------
DROP PROCEDURE IF EXISTS `MYCONVERT`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `MYCONVERT`(IN `p_str` varchar(4000),IN `p_delim` varchar(10))
BEGIN
  
    DECLARE str_len INT;
		DECLARE p       INT;

		drop temporary table if exists splittable;
    create TEMPORARY table splittable(
        value VARCHAR(100)
    ) ;

		SET str_len = func_get_split_string_total(p_str,p_delim);

    if  str_len > 0 AND LENGTH(p_delim) > 0 then

			SET p = 1;

			loop_split : LOOP 

				insert into splittable(`value`)values(func_get_split_string(p_str,p_delim,p));

				SET p = p + 1;

				IF p >= str_len THEN

					LEAVE loop_split;

				END IF;

			end LOOP loop_split;

    end if;


END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `splitString`
-- ----------------------------
DROP PROCEDURE IF EXISTS `splitString`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `splitString`(
	IN f_string VARCHAR(1000),
	IN f_delimiter VARCHAR(5)
)
BEGIN
	# 拆分结果 
	DECLARE
		cnt INT DEFAULT 0;

DECLARE
	i INT DEFAULT 0;


SET cnt = func_split_TotalLength(f_string, f_delimiter);

DROP TABLE
IF EXISTS `tmp_split`;

CREATE TEMPORARY TABLE `tmp_split`(
	`status` VARCHAR(128)NOT NULL
)DEFAULT CHARSET = utf8;


WHILE i < cnt DO

SET i = i + 1;

INSERT INTO tmp_split(`status`)
VALUES
	(
		func_split(f_string, f_delimiter, i)
	);


END
WHILE;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `SP_MENU_MANAGER`
-- ----------------------------
DROP PROCEDURE IF EXISTS `SP_MENU_MANAGER`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MENU_MANAGER`(IN `v_type` VARCHAR(2),
IN `v_menuid` VARCHAR(10),
IN `v_subsysid` VARCHAR(10),
IN `v_menuname` VARCHAR(50),
IN `v_status` VARCHAR(2),
IN `v_isfunction` VARCHAR(2),
IN `v_parentid` VARCHAR(10),
IN `v_linkurl` VARCHAR(500),
IN `v_levelflag` VARCHAR(2),
IN `v_sort` VARCHAR(10),
IN `v_systype` VARCHAR(10),
IN `v_isadmin` VARCHAR(2),
IN `v_tacklink` VARCHAR(2000),
IN `v_powerids` VARCHAR(2000),
IN `v_domainids` VARCHAR(2000),
IN `v_powers` VARCHAR(2000),
IN `v_opterid` VARCHAR(100),
OUT `v_dbreturn` VARCHAR(10),
OUT `v_msgreturn` VARCHAR(100))
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
    ROLLBACK;
		SET v_dbreturn='1111';
		SET v_msgreturn = '1111';
	END;

  SET v_dbreturn='0001';

IF v_type = 0 THEN

	INSERT INTO `sys_menu`(
		`subsysid`,
		`menuname`,
		`status`,
		`isfunction`,
		`parentid`,
		`linkurl`,
		`levelflag`,
		`sort`,
		`createtime`,
		`opterid`,
		`systype`,
		`isadmin`
	)
	VALUES
		(
			v_subsysid,
			v_menuname,
			v_status,
			v_isfunction,
			v_parentid,
			v_linkurl,
			v_levelflag,
			v_sort,
			SYSDATE(),
			v_opterid,
			v_systype,
			v_isadmin
		);

	SET v_dbreturn='0000';
  SET v_msgreturn = '0000';

ELSEIF v_type = 1 THEN

	SET v_dbreturn='0001';
SET v_msgreturn = '0001';

ELSEIF v_type = 2 THEN

	SET v_dbreturn='0001';
SET v_msgreturn = '0001';

END IF;





  COMMIT;


END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `SP_MENU_MANAGER2`
-- ----------------------------
DROP PROCEDURE IF EXISTS `SP_MENU_MANAGER2`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MENU_MANAGER2`()
BEGIN


	INSERT INTO `sys_menu`(
		`subsysid`,
		`menuname`
	)
	VALUES
		(
			2,
			'wwwww'
		);

COMMIT;


END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `TEST_ERROR`
-- ----------------------------
DROP PROCEDURE IF EXISTS `TEST_ERROR`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `TEST_ERROR`()
BEGIN
	#Routine body goes here...

  -- SET v_dbreturn='1111';

	DECLARE v_msgreturn VARCHAR(10);

	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET @v_msgreturn = '1111';




  COMMIT;




END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `func_get_split_string`
-- ----------------------------
DROP FUNCTION IF EXISTS `func_get_split_string`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `func_get_split_string`(
f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN
  declare result varchar(255) default '';
  set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
  return result;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `func_get_split_string_total`
-- ----------------------------
DROP FUNCTION IF EXISTS `func_get_split_string_total`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `func_get_split_string_total`(
f_string varchar(1000),f_delimiter varchar(5)
) RETURNS int(11)
BEGIN
  return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `func_split`
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `func_split`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN 
    # 拆分传入的字符串，返回拆分后的新字符串 
        declare result varchar(255) default ''; 
        set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1)); 
        return result; 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `func_split_TotalLength`
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split_TotalLength`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `func_split_TotalLength`(f_string varchar(1000),f_delimiter varchar(5)) RETURNS int(11)
BEGIN 
    # 计算传入字符串的总length 
    return 1+(length(f_string) - length(replace(f_string,f_delimiter,''))); 
END
;;
DELIMITER ;
