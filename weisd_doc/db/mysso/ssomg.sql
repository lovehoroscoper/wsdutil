/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : ssomg

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2013-01-11 17:45:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `sso_login_third`
-- ----------------------------
DROP TABLE IF EXISTS `sso_login_third`;
CREATE TABLE `sso_login_third` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT '第三方ID',
  `name` varchar(50) NOT NULL COMMENT '第三方名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '第三方状态',
  `ext0` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sso_login_third
-- ----------------------------
INSERT INTO `sso_login_third` VALUES ('1', 'GitHubProfile', '0', 'GitHubGitHubProvider');

-- ----------------------------
-- Table structure for `sso_sys_acl_menurole`
-- ----------------------------
DROP TABLE IF EXISTS `sso_sys_acl_menurole`;
CREATE TABLE `sso_sys_acl_menurole` (
  `id_acl` int(11) NOT NULL,
  `principal_type` varchar(10) DEFAULT NULL COMMENT '使用类型，是使用角色还是直接给用户等等',
  `principal_id` int(11) DEFAULT NULL COMMENT '主体ID，如id_source',
  `id_role` int(11) DEFAULT NULL COMMENT '角色ID',
  `acl_state` int(11) DEFAULT NULL COMMENT '权限CRUD',
  `valid_type` tinyint(4) DEFAULT NULL COMMENT '有效类型，对于多种权限并存的如 角色权限与用户直接权限的并存',
  PRIMARY KEY (`id_acl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限:给角色的权限或者给用户直接权限';

-- ----------------------------
-- Records of sso_sys_acl_menurole
-- ----------------------------
INSERT INTO `sso_sys_acl_menurole` VALUES ('1', '0', '31', '2', null, null);
INSERT INTO `sso_sys_acl_menurole` VALUES ('2', '0', '32', '2', null, null);
INSERT INTO `sso_sys_acl_menurole` VALUES ('3', '0', '33', '2', null, null);

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
-- Table structure for `sso_user`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `id_user` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户登录用户名，与站点来源联合唯一.本地用户名：邮箱',
  `password` varchar(300) NOT NULL COMMENT '密码',
  `nikename` varchar(50) DEFAULT NULL COMMENT '昵称',
  `id_site` int(11) NOT NULL COMMENT '站点来源ID,与sso_sitesource的主键',
  `logintype` tinyint(4) DEFAULT NULL COMMENT '登录类型，sso_type_login',
  `validtype` tinyint(4) DEFAULT NULL COMMENT '是否有效，或者锁定等等',
  `createdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `encrypttype` tinyint(4) DEFAULT NULL COMMENT '加密类型',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_UNIQUE` (`id_user`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sso_user
-- ----------------------------
INSERT INTO `sso_user` VALUES ('1', 'admin@163.com', '25dc371ffab906626148050fd92ac28e979d4ec73635941bc72ceb06277b6e15ec06bfb9c0c98901', 'admin', '1', '1', '0', '2011-12-12 12:44:21', '1', 'admin');
INSERT INTO `sso_user` VALUES ('14', 'xiyangdewuse@163.com', '25dc371ffab906626148050fd92ac28e979d4ec73635941bc72ceb06277b6e15ec06bfb9c0c98901', '啊的', '1', null, null, '2013-01-06 15:52:40', null, 'xiyangdewuse@163.com');

-- ----------------------------
-- Table structure for `sso_user_copy`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_copy`;
CREATE TABLE `sso_user_copy` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sso_user_copy
-- ----------------------------
INSERT INTO `sso_user_copy` VALUES ('1', 'admin', '25dc371ffab906626148050fd92ac28e979d4ec73635941bc72ceb06277b6e15ec06bfb9c0c98901', 'admin', '1', '1', '0', '2011-12-12 12:44:21', '1', '1', 'admin');

-- ----------------------------
-- Table structure for `sso_user_third`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_third`;
CREATE TABLE `sso_user_third` (
  `providerid` tinyint(4) NOT NULL,
  `thirduserid` varchar(100) NOT NULL,
  `iduser` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `createdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `UN_THIRD_PK` (`providerid`,`thirduserid`) USING BTREE,
  UNIQUE KEY `UN_THIRD_PK2` (`providerid`,`username`) USING BTREE,
  KEY `IDX_THIRD_INDEX3` (`providerid`,`thirduserid`,`username`) USING BTREE,
  KEY `IDX_THIRD_INDEX4` (`providerid`,`thirduserid`,`username`,`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sso_user_third
-- ----------------------------
INSERT INTO `sso_user_third` VALUES ('1', '973288', '1', 'admin@163.com', '2013-01-08 19:25:23');

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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

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
INSERT INTO `sys_menu` VALUES ('32', '0', 'sso', '1', '1', '25', '/services/manage.html', '3', '3', '2013-01-07 10:53:15', 'weisd', '0', '0');
INSERT INTO `sys_menu` VALUES ('33', '0', 'user', '1', '1', '25', '/user/index.do', '3', '4', '2013-01-11 13:26:35', 'weisd', '0', null);

-- ----------------------------
-- Procedure structure for `ADD_USER`
-- ----------------------------
DROP PROCEDURE IF EXISTS `ADD_USER`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ADD_USER`(IN `v_bindtype` int(2),IN `v_username` varchar(50),IN `v_password` varchar(300),IN `v_providerid` int(20),IN `v_thirduserid` varchar(100),IN `v_nikename` varchar(100),OUT `v_dbreturn` varchar(10),OUT `v_userid` varchar(20),OUT `v_info1` varchar(100))
BEGIN

	DECLARE ext_u_count INT DEFAULT 0;
	DECLARE ext_th_count INT DEFAULT 0;
	DECLARE ext_u_th_count INT DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SET v_dbreturn = '1111';
		SET v_userid = '0';
	END;


	SET v_dbreturn = '-1';

	SELECT
		count(1) INTO ext_u_count
	FROM
		sso_user u
	WHERE
		u.username = v_username;

	SELECT
		count(1) INTO ext_th_count
	FROM
		sso_user_third t
	WHERE
		t.providerid = v_providerid
	AND t.thirduserid = v_thirduserid;

	-- 如果我方账户存在,则检查是否绑定过该类型第三方的账户
	SELECT
		count(1) INTO ext_u_th_count
	FROM
		sso_user_third t
	WHERE
		t.providerid = v_providerid
	AND t.username = v_username;

	IF v_bindtype = 1 AND ext_u_count = 0 AND ext_th_count = 0 and ext_u_th_count = 0 THEN
			-- 双方都添加
			INSERT sso_user(
				username,
				PASSWORD,
				EMAIL,
				ID_SITE,
				NIKENAME,
				createdate
			)
			VALUES
				(
					v_username,
					v_password,
					v_username,
					v_providerid,
					v_nikename,
					SYSDATE()
				);

			SELECT
				LAST_INSERT_ID()INTO v_userid;

			INSERT sso_user_third(
				providerid,
				thirduserid,
				username,
				iduser,
				createdate
			)
			VALUES
				(
					v_providerid,
					v_thirduserid,
					v_username,
					v_userid,
					SYSDATE()
				);

			SET v_dbreturn = '0000';

	ELSEIF v_bindtype = 2 AND ext_u_count = 1 AND ext_th_count = 0 and ext_u_th_count = 0 THEN
			-- 第三添加
			SELECT 
				t.id_user INTO v_userid 
			FROM sso_user t WHERE t.username = v_username;

			INSERT sso_user_third(
				providerid,
				thirduserid,
				username,
				iduser,
				createdate
			)
			VALUES
				(
					v_providerid,
					v_thirduserid,
					v_username,
					v_userid,
					SYSDATE()
				);

			SET v_dbreturn = '0000';

	ELSE 
			SET v_dbreturn = '0100';
			SET v_info1 = CONCAT(v_bindtype,',',ext_u_count,',',ext_th_count);
	END IF;

	IF v_dbreturn = '0000' THEN
			COMMIT;
	ELSE
			ROLLBACK;
	END IF;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `CHECK_USER_UNIQUE`
-- ----------------------------
DROP PROCEDURE IF EXISTS `CHECK_USER_UNIQUE`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CHECK_USER_UNIQUE`(IN `v_checktype` int(4),IN `v_username` varchar(50),IN `v_password` varchar(300),IN `v_providerid` int(20),IN `v_thirduserid` varchar(100),IN `v_nikename` varchar(100),OUT `v_dbreturn` varchar(10),OUT `v_localexist` varchar(10),OUT `v_thirdexist` varchar(10),OUT `v_info1` varchar(300),OUT `v_info2` varchar(100))
    READS SQL DATA
BEGIN

	DECLARE ext_u_count INT DEFAULT 0;
	DECLARE ext_th_count INT DEFAULT 0;
	DECLARE ext_u_th_count INT DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SET v_dbreturn = '1111';
	END;
	
	SET v_dbreturn = '-1';
	SET v_localexist = '-1';
	SET v_thirdexist = '-1';
	SET v_password = '-1';

	IF v_checktype = 1 THEN
			
			-- 检查我方用户名  start
			SELECT
				count(1) INTO ext_u_count
			FROM
				sso_user u
			WHERE
				u.username = v_username;

			IF ext_u_count = 0 THEN
				SET v_localexist = 'CUN0000';
			ELSEIF ext_u_count = 1 THEN 
				
				-- 如果我方账户存在,则检查是否绑定过该类型第三方的账户
				SELECT
					count(1) INTO ext_u_th_count
				FROM
					sso_user_third t
				WHERE
					t.providerid = v_providerid
				AND t.username = v_username;

				IF ext_u_th_count = 0 THEN
						SELECT
							u.`password` INTO v_password
						FROM
							sso_user u
						WHERE
							u.username = v_username;

						SET v_localexist = 'CUN0001';
				ELSE
						SET v_localexist = 'CUN0003';
				END IF;

			ELSE 
				SET v_localexist = 'CUN_1111';
			END if;
			-- 检查我方用户名  end

			-- 检查第三方用户名 start
			SELECT
				count(1) INTO ext_th_count
			FROM
				sso_user_third t
			WHERE
				t.providerid = v_providerid
			AND t.thirduserid = v_thirduserid;

			IF ext_th_count = 0 THEN
				SET v_thirdexist = 'CUN0000';
			ELSEIF ext_th_count = 1 THEN
				SET v_thirdexist = 'CUN0001';
			ELSE 
				SET v_thirdexist = 'CUN_1111';
			END if;
			-- 检查第三方用户名  end
			
			SET v_dbreturn = '0000';

	ELSE
		SET v_dbreturn = '0100';
	END IF;

	ROLLBACK;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `myProc`
-- ----------------------------
DROP PROCEDURE IF EXISTS `myProc`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `myProc`()
outer_block: BEGIN
        DECLARE l_status int;
        SET l_status=1;
       inner_block: BEGIN
              IF (l_status=1) THEN 
												SELECT 'into inner_block l_status = 1';
		
                        -- LEAVE inner_block;
												LEAVE outer_block;
              END IF;
               SELECT 'This statement will never be executed';
        END inner_block;
        SELECT 'End of program';
END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `TestProc`
-- ----------------------------
DROP PROCEDURE IF EXISTS `TestProc`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `TestProc`(Value INT)
ThisSP:BEGIN

IF  Value is null or Value=0 then
Select ‘Invalid Value’;
LEAVE ThisSP;
END IF;

Select ‘Invalid Value’;

END
;;
DELIMITER ;
