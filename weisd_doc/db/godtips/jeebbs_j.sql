-- MySQL dump 10.13  Distrib 5.5.15, for Win32 (x86)
--
-- Host: localhost    Database: jeebbs_j
-- ------------------------------------------------------
-- Server version	5.5.15

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
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `attachment_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `file_path` varchar(100) DEFAULT NULL COMMENT '路径',
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `file_size` int(11) DEFAULT NULL COMMENT '大小',
  `is_pictrue` tinyint(1) DEFAULT '0' COMMENT '是否是图片',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`attachment_id`),
  KEY `FK_attachment_post` (`post_id`),
  CONSTRAINT `FK_attachment_post` FOREIGN KEY (`post_id`) REFERENCES `bbs_post` (`POST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_category`
--

DROP TABLE IF EXISTS `bbs_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_category` (
  `CATEGORY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SITE_ID` int(11) NOT NULL,
  `PATH` varchar(20) NOT NULL COMMENT '访问路径',
  `TITLE` varchar(100) NOT NULL COMMENT '标题',
  `PRIORITY` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `FORUM_COLS` int(11) NOT NULL DEFAULT '1' COMMENT '板块列数',
  `moderators` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CATEGORY_ID`),
  KEY `FK_BBS_CTG_SITE` (`SITE_ID`),
  CONSTRAINT `FK_BBS_CTG_SITE` FOREIGN KEY (`SITE_ID`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='论坛分区';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_category_user`
--

DROP TABLE IF EXISTS `bbs_category_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_category_user` (
  `CATEGORY_ID` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`CATEGORY_ID`,`user_id`),
  KEY `FK_BBS_CATEGORY_TO_USER` (`user_id`),
  CONSTRAINT `FK_BBS_CATEGORY_TO_USER` FOREIGN KEY (`user_id`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_USER_TO_CATEGORY` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `bbs_category` (`CATEGORY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分区版主';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_config`
--

DROP TABLE IF EXISTS `bbs_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_config` (
  `CONFIG_ID` bigint(20) NOT NULL,
  `DEF_AVATAR` varchar(100) NOT NULL DEFAULT '' COMMENT '默认会员头像',
  `AVATAR_WIDTH` int(11) NOT NULL DEFAULT '160' COMMENT '头像最大宽度',
  `AVATAR_HEIGHT` int(11) NOT NULL DEFAULT '160' COMMENT '头像最大高度',
  `TOPIC_COUNT_PER_PAGE` int(11) NOT NULL DEFAULT '20' COMMENT '每页主题数',
  `POST_COUNT_PER_PAGE` int(11) NOT NULL DEFAULT '10' COMMENT '每页帖子数',
  `KEYWORDS` varchar(255) NOT NULL DEFAULT '' COMMENT '首页关键字',
  `DESCRIPTION` varchar(255) NOT NULL DEFAULT '' COMMENT '首页描述',
  `REGISTER_STATUS` smallint(6) NOT NULL DEFAULT '1' COMMENT '注册状态（0：关闭，1：开放，2：邀请）',
  `REGISTER_GROUP_ID` int(11) NOT NULL DEFAULT '1' COMMENT '注册会员组',
  `REGISTER_RULE` longtext COMMENT '注册协议',
  `CACHE_POST_TODAY` int(11) NOT NULL DEFAULT '0' COMMENT '今日贴数',
  `CACHE_POST_YESTERDAY` int(11) NOT NULL DEFAULT '0' COMMENT '昨日帖数',
  `CACHE_POST_MAX` int(11) NOT NULL DEFAULT '0' COMMENT '最高帖数',
  `CACHE_POST_MAX_DATE` date NOT NULL COMMENT '最高帖数日',
  `CACHE_TOPIC_TOTAL` int(11) NOT NULL DEFAULT '0' COMMENT '总主题',
  `CACHE_POST_TOTAL` int(11) NOT NULL DEFAULT '0' COMMENT '总帖数',
  `CACHE_USER_TOTAL` int(11) NOT NULL DEFAULT '0' COMMENT '总会员',
  `last_user_id` int(11) DEFAULT NULL COMMENT '最新会员',
  `site_id` int(11) NOT NULL,
  `DEFAULT_GROUP_ID` bigint(20) NOT NULL DEFAULT '1' COMMENT '默认会员组',
  `TOPIC_HOT_COUNT` int(11) NOT NULL DEFAULT '10' COMMENT '热帖回复数量',
  `AUTO_REGISTER` tinyint(1) DEFAULT '1' COMMENT '是否自动注册',
  PRIMARY KEY (`CONFIG_ID`),
  KEY `FK_BBS_CONFIG_SITE` (`site_id`),
  CONSTRAINT `FK_BBS_CONFIG_SITE` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论坛配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_forum`
--

DROP TABLE IF EXISTS `bbs_forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_forum` (
  `FORUM_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CATEGORY_ID` int(11) NOT NULL COMMENT '分区ID',
  `SITE_ID` int(11) NOT NULL COMMENT '站点ID',
  `POST_ID` int(11) DEFAULT NULL COMMENT '最后回帖',
  `replyer_id` int(11) DEFAULT NULL COMMENT '最后回帖会员',
  `PATH` varchar(20) NOT NULL COMMENT '访问路径',
  `TITLE` varchar(150) NOT NULL COMMENT '标题',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '描述',
  `KEYWORDS` varchar(255) DEFAULT NULL COMMENT 'meta-keywords',
  `FORUM_RULE` varchar(255) DEFAULT NULL COMMENT '版规',
  `PRIORITY` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `TOPIC_TOTAL` int(11) NOT NULL DEFAULT '0' COMMENT '主题总数',
  `POST_TOTAL` int(11) NOT NULL DEFAULT '0' COMMENT '帖子总数',
  `POST_TODAY` int(11) NOT NULL DEFAULT '0' COMMENT '今日新帖',
  `OUTER_URL` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `POINT_TOPIC` int(11) NOT NULL DEFAULT '0' COMMENT '发贴加分',
  `POINT_REPLY` int(11) NOT NULL DEFAULT '0' COMMENT '回帖加分',
  `POINT_PRIME` int(11) NOT NULL DEFAULT '0' COMMENT '精华加分',
  `LAST_TIME` datetime DEFAULT NULL COMMENT '最后发贴时间',
  `TOPIC_LOCK_LIMIT` int(11) NOT NULL DEFAULT '0' COMMENT '锁定主题（天）',
  `moderators` varchar(100) DEFAULT NULL COMMENT '版主',
  `group_views` varchar(100) DEFAULT NULL COMMENT '访问会员组',
  `group_topics` varchar(100) DEFAULT NULL COMMENT '发帖会员组',
  `group_replies` varchar(100) DEFAULT NULL COMMENT '回复会员组',
  PRIMARY KEY (`FORUM_ID`),
  KEY `FK_BBS_FORUM_CTG` (`CATEGORY_ID`),
  KEY `FK_BBS_FORUM_USER` (`replyer_id`),
  KEY `FK_BBS_FORUM_POST` (`POST_ID`),
  KEY `FK_BBS_FORUM_WEBSITE` (`SITE_ID`),
  CONSTRAINT `FK_BBS_FORUM_CTG` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `bbs_category` (`CATEGORY_ID`),
  CONSTRAINT `FK_BBS_FORUM_POST` FOREIGN KEY (`POST_ID`) REFERENCES `bbs_post` (`POST_ID`),
  CONSTRAINT `FK_BBS_FORUM_USER` FOREIGN KEY (`replyer_id`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_FORUM_WEBSITE` FOREIGN KEY (`SITE_ID`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='论坛板块';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_forum_group_reply`
--

DROP TABLE IF EXISTS `bbs_forum_group_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_forum_group_reply` (
  `FORUM_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  PRIMARY KEY (`FORUM_ID`,`GROUP_ID`),
  KEY `FK_BBS_FORUM_GROUP_REPLY` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_FORUM_GROUP_REPLY` FOREIGN KEY (`GROUP_ID`) REFERENCES `bbs_user_group` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_GROUP_FORUM_REPLY` FOREIGN KEY (`FORUM_ID`) REFERENCES `bbs_forum` (`FORUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='回复权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_forum_group_topic`
--

DROP TABLE IF EXISTS `bbs_forum_group_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_forum_group_topic` (
  `FORUM_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  PRIMARY KEY (`FORUM_ID`,`GROUP_ID`),
  KEY `FK_BBS_FORUM_GROUP_TOPIC` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_FORUM_GROUP_TOPIC` FOREIGN KEY (`GROUP_ID`) REFERENCES `bbs_user_group` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_GROUP_FORUM_TOPIC` FOREIGN KEY (`FORUM_ID`) REFERENCES `bbs_forum` (`FORUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发贴权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_forum_group_view`
--

DROP TABLE IF EXISTS `bbs_forum_group_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_forum_group_view` (
  `GROUP_ID` int(11) NOT NULL,
  `FORUM_ID` int(11) NOT NULL,
  PRIMARY KEY (`GROUP_ID`,`FORUM_ID`),
  KEY `FK_BBS_GROUP_FORUM_VIEW` (`FORUM_ID`),
  CONSTRAINT `FK_BBS_FORUM_GROUP_VIEW` FOREIGN KEY (`GROUP_ID`) REFERENCES `bbs_user_group` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_GROUP_FORUM_VIEW` FOREIGN KEY (`FORUM_ID`) REFERENCES `bbs_forum` (`FORUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='浏览权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_forum_user`
--

DROP TABLE IF EXISTS `bbs_forum_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_forum_user` (
  `FORUM_ID` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`FORUM_ID`,`user_id`),
  KEY `FK_BBS_FORUM_TO_USER` (`user_id`),
  CONSTRAINT `FK_BBS_FORUM_TO_USER` FOREIGN KEY (`user_id`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_USER_TO_FORUM` FOREIGN KEY (`FORUM_ID`) REFERENCES `bbs_forum` (`FORUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='版块版主';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_grade`
--

DROP TABLE IF EXISTS `bbs_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_grade` (
  `GRADE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `POST_ID` int(11) NOT NULL,
  `SCORE` int(11) DEFAULT NULL,
  `REASON` varchar(100) DEFAULT NULL,
  `GRADE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`GRADE_ID`),
  KEY `FK_MEMBER_GRADE` (`user_id`),
  KEY `FK_POST_GRADE` (`POST_ID`),
  CONSTRAINT `FK_MEMBER_GRADE` FOREIGN KEY (`user_id`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_POST_GRADE` FOREIGN KEY (`POST_ID`) REFERENCES `bbs_post` (`POST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_operation`
--

DROP TABLE IF EXISTS `bbs_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_operation` (
  `OPERATOR_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SITE_ID` int(11) NOT NULL,
  `operater_id` int(11) NOT NULL COMMENT '操作会员',
  `REF_TYPE` char(4) NOT NULL COMMENT '引用类型',
  `REF_ID` int(11) NOT NULL DEFAULT '0' COMMENT '引用ID',
  `OPT_NAME` varchar(100) NOT NULL COMMENT '操作名称',
  `OPT_REASON` varchar(255) DEFAULT NULL COMMENT '操作原因',
  `OPT_TIME` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`OPERATOR_ID`),
  KEY `FK_BBS_OPEATTION` (`SITE_ID`),
  KEY `FK_BBS_OPERATION_USER` (`operater_id`),
  CONSTRAINT `FK_BBS_OPEATTION` FOREIGN KEY (`SITE_ID`) REFERENCES `jc_site` (`site_id`),
  CONSTRAINT `FK_BBS_OPERATION_USER` FOREIGN KEY (`operater_id`) REFERENCES `jb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='主题、帖子操作记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_permission`
--

DROP TABLE IF EXISTS `bbs_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_permission` (
  `GROUP_ID` int(11) NOT NULL,
  `PERM_KEY` varchar(20) NOT NULL COMMENT '权限key',
  `PERM_VALUE` varchar(255) DEFAULT NULL COMMENT '权限value',
  KEY `FK_BBS_PERMISSION_GROUP` (`GROUP_ID`),
  CONSTRAINT `FK_BBS_PERMISSION_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `bbs_user_group` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论坛权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_post`
--

DROP TABLE IF EXISTS `bbs_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_post` (
  `POST_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TOPIC_ID` int(11) NOT NULL COMMENT '主题',
  `SITE_ID` int(11) NOT NULL COMMENT '站点',
  `CONFIG_ID` int(11) NOT NULL,
  `EDITER_ID` int(11) DEFAULT NULL COMMENT '编辑器会员',
  `CREATER_ID` int(11) NOT NULL COMMENT '发贴会员',
  `CREATE_TIME` datetime NOT NULL COMMENT '发贴时间',
  `POSTER_IP` varchar(20) NOT NULL DEFAULT '' COMMENT '发贴IP',
  `EDIT_TIME` datetime DEFAULT NULL COMMENT '编辑时间',
  `EDITER_IP` varchar(20) DEFAULT '' COMMENT '编辑者IP',
  `EDIT_COUNT` int(11) NOT NULL DEFAULT '0' COMMENT '编辑次数',
  `INDEX_COUNT` int(11) NOT NULL DEFAULT '1' COMMENT '第几楼',
  `STATUS` smallint(6) NOT NULL DEFAULT '0' COMMENT '状态',
  `IS_AFFIX` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否上传附件',
  `IS_HIDDEN` tinyint(1) DEFAULT '0' COMMENT '是否有隐藏内容',
  PRIMARY KEY (`POST_ID`),
  KEY `FK_BBS_POST_CREATER` (`CREATER_ID`),
  KEY `FK_BBS_POST_EDITOR` (`EDITER_ID`),
  KEY `FK_BBS_POST_TOPIC` (`TOPIC_ID`),
  KEY `FK_BBS_POST_WEBSITE` (`SITE_ID`),
  CONSTRAINT `FK_BBS_POST_CREATER` FOREIGN KEY (`CREATER_ID`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_POST_EDITOR` FOREIGN KEY (`EDITER_ID`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_POST_TOPIC` FOREIGN KEY (`TOPIC_ID`) REFERENCES `bbs_topic` (`TOPIC_ID`),
  CONSTRAINT `FK_BBS_POST_WEBSITE` FOREIGN KEY (`SITE_ID`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='论坛帖子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_post_text`
--

DROP TABLE IF EXISTS `bbs_post_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_post_text` (
  `POST_ID` bigint(20) NOT NULL,
  `POST_TITLE` varchar(100) DEFAULT NULL COMMENT '帖子标题',
  `POST_CONTENT` longtext COMMENT '帖子内容',
  PRIMARY KEY (`POST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='论坛帖子内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_private_msg`
--

DROP TABLE IF EXISTS `bbs_private_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_private_msg` (
  `PRIVMSG_ID` bigint(20) NOT NULL,
  `TO_USER` bigint(20) NOT NULL COMMENT '收信人',
  `FROM_USER` bigint(20) NOT NULL COMMENT '发信人',
  `MSG_TYPE` smallint(6) NOT NULL DEFAULT '1' COMMENT '类型（2：已发，1：已阅，0：未阅）',
  `MSG_SUBJECT` varchar(255) DEFAULT NULL COMMENT '主题',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MSG_IP` varchar(20) NOT NULL DEFAULT '' COMMENT 'IP地址',
  PRIMARY KEY (`PRIVMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人短消息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_private_msg_text`
--

DROP TABLE IF EXISTS `bbs_private_msg_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_private_msg_text` (
  `PRIVMSG_ID` bigint(20) NOT NULL,
  `MSG_TEXT` longtext COMMENT '个人信息内容',
  PRIMARY KEY (`PRIVMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人消息内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_topic`
--

DROP TABLE IF EXISTS `bbs_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_topic` (
  `TOPIC_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FORUM_ID` int(11) NOT NULL COMMENT '板块',
  `LAST_POST_ID` int(11) DEFAULT NULL COMMENT '最后帖',
  `FIRST_POST_ID` int(11) DEFAULT NULL COMMENT '主题帖',
  `SITE_ID` int(11) NOT NULL COMMENT '站点',
  `CREATER_ID` int(11) NOT NULL COMMENT '发帖会员',
  `REPLYER_ID` int(11) NOT NULL COMMENT '最后回帖会员',
  `TITLE` varchar(100) NOT NULL COMMENT '标题',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `LAST_TIME` datetime NOT NULL COMMENT '最后回帖时间',
  `SORT_TIME` datetime NOT NULL COMMENT '用于排序',
  `VIEW_COUNT` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `REPLY_COUNT` int(11) NOT NULL DEFAULT '0' COMMENT '恢复次数',
  `TOP_LEVEL` smallint(6) NOT NULL DEFAULT '0' COMMENT '固定级别',
  `PRIME_LEVEL` smallint(6) NOT NULL DEFAULT '0' COMMENT '精华级别',
  `STATUS` smallint(6) NOT NULL DEFAULT '0' COMMENT '状态',
  `OUTER_URL` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `STYLE_BOLD` tinyint(1) NOT NULL DEFAULT '0' COMMENT '粗体',
  `STYLE_ITALIC` tinyint(1) NOT NULL DEFAULT '0' COMMENT '斜体',
  `STYLE_COLOR` char(6) DEFAULT NULL COMMENT '颜色',
  `STYLE_TIME` datetime DEFAULT NULL COMMENT '样式有效时间',
  `IS_AFFIX` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否上传附件',
  `HAVA_REPLY` longtext COMMENT '回复列表',
  `moderator_reply` tinyint(1) DEFAULT '0' COMMENT '版主是否回复',
  PRIMARY KEY (`TOPIC_ID`),
  KEY `BBS_SORT_TIME` (`SORT_TIME`),
  KEY `FK_BBS_FIRST_POST` (`FIRST_POST_ID`),
  KEY `FK_BBS_LAST_POST` (`LAST_POST_ID`),
  KEY `FK_BBS_TOPIC_FORUM` (`FORUM_ID`),
  KEY `FK_BBS_TOPIC_USER_CREATE` (`CREATER_ID`),
  KEY `FK_BBS_TOPIC_USER_LAST` (`REPLYER_ID`),
  KEY `FK_BBS_TOPIC_SITE` (`SITE_ID`),
  CONSTRAINT `FK_BBS_FIRST_POST` FOREIGN KEY (`FIRST_POST_ID`) REFERENCES `bbs_post` (`POST_ID`),
  CONSTRAINT `FK_BBS_LAST_POST` FOREIGN KEY (`LAST_POST_ID`) REFERENCES `bbs_post` (`POST_ID`),
  CONSTRAINT `FK_BBS_TOPIC_FORUM` FOREIGN KEY (`FORUM_ID`) REFERENCES `bbs_forum` (`FORUM_ID`),
  CONSTRAINT `FK_BBS_TOPIC_SITE` FOREIGN KEY (`SITE_ID`) REFERENCES `jc_site` (`site_id`),
  CONSTRAINT `FK_BBS_TOPIC_USER_CREATE` FOREIGN KEY (`CREATER_ID`) REFERENCES `jb_user` (`user_id`),
  CONSTRAINT `FK_BBS_TOPIC_USER_LAST` FOREIGN KEY (`REPLYER_ID`) REFERENCES `jb_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='论坛主题';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bbs_user_group`
--

DROP TABLE IF EXISTS `bbs_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bbs_user_group` (
  `GROUP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `NAME` varchar(20) NOT NULL COMMENT '头衔',
  `GROUP_TYPE` smallint(6) NOT NULL DEFAULT '0' COMMENT '组类别',
  `GROUP_IMG` varchar(100) DEFAULT NULL COMMENT '图片',
  `GROUP_POINT` int(11) NOT NULL DEFAULT '0' COMMENT '升级积分',
  `IS_DEFAULT` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认组',
  `GRADE_NUM` int(11) DEFAULT '0' COMMENT '评分',
  PRIMARY KEY (`GROUP_ID`),
  KEY `FK_BBS_GROUP_SITE` (`site_id`),
  CONSTRAINT `FK_BBS_GROUP_SITE` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='论坛会员组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jb_user`
--

DROP TABLE IF EXISTS `jb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jb_user` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `upload_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '上传总大小',
  `upload_size` int(11) NOT NULL DEFAULT '0' COMMENT '上传大小',
  `upload_date` date DEFAULT NULL COMMENT '上传日期',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `PROHIBIT_POST` smallint(6) NOT NULL DEFAULT '0' COMMENT '禁言(0:不禁言;1:永久禁言;2:定期禁言)',
  `PROHIBIT_TIME` datetime DEFAULT NULL COMMENT '解禁时间',
  `GRADE_TODAY` int(11) DEFAULT '0' COMMENT '今日评分',
  `UPLOAD_TODAY` int(11) DEFAULT '0' COMMENT '今日上传',
  `POINT` bigint(20) DEFAULT '0' COMMENT '积分',
  `INTRODUCTION` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `SIGNED` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `AVATAR` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `AVATAR_TYPE` smallint(6) DEFAULT '0' COMMENT '头像类型',
  `TOPIC_COUNT` int(11) DEFAULT '0' COMMENT '主题总数',
  `REPLY_COUNT` int(11) DEFAULT '0' COMMENT '回复总数',
  `PRIME_COUNT` int(11) DEFAULT '0' COMMENT '精华总数',
  `POST_TODAY` int(11) DEFAULT '0' COMMENT '今日发帖',
  `LAST_POST_TIME` datetime DEFAULT NULL COMMENT '最后回帖时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`),
  KEY `FK_BBS_MEMBER_MEMBERGROUP` (`group_id`),
  CONSTRAINT `FK_BBS_MEMBER_MEMBERGROUP` FOREIGN KEY (`group_id`) REFERENCES `bbs_user_group` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='BBS用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jb_user_ext`
--

DROP TABLE IF EXISTS `jb_user_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jb_user_ext` (
  `user_id` int(11) NOT NULL,
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `avatar` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `intro` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `comefrom` varchar(150) DEFAULT NULL COMMENT '来自',
  `qq` varchar(100) DEFAULT NULL COMMENT 'QQ',
  `msn` varchar(100) DEFAULT NULL COMMENT 'MSN',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `moble` varchar(50) DEFAULT NULL COMMENT '手机',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='BBS用户扩展信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jc_config`
--

DROP TABLE IF EXISTS `jc_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jc_config` (
  `config_id` int(11) NOT NULL,
  `context_path` varchar(20) DEFAULT '' COMMENT '部署路径',
  `servlet_point` varchar(20) DEFAULT NULL COMMENT 'Servlet挂载点',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `db_file_uri` varchar(50) NOT NULL DEFAULT '/dbfile.svl?n=' COMMENT '数据库附件访问地址',
  `is_upload_to_db` tinyint(1) NOT NULL DEFAULT '0' COMMENT '上传附件至数据库',
  `def_img` varchar(255) NOT NULL DEFAULT '/r/cms/www/blue/no_picture.gif' COMMENT '图片不存在时默认图片',
  `login_url` varchar(255) NOT NULL DEFAULT '/login.jspx' COMMENT '登录地址',
  `process_url` varchar(255) DEFAULT NULL COMMENT '登录后处理地址',
  `count_clear_time` date NOT NULL COMMENT '计数器清除时间',
  `count_copy_time` datetime NOT NULL COMMENT '计数器拷贝时间',
  `download_code` varchar(32) NOT NULL DEFAULT 'jeecms' COMMENT '下载防盗链md5混淆码',
  `download_time` int(11) NOT NULL DEFAULT '12' COMMENT '下载有效时间（小时）',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jc_site`
--

DROP TABLE IF EXISTS `jc_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jc_site` (
  `site_id` int(11) NOT NULL AUTO_INCREMENT,
  `config_id` int(11) NOT NULL COMMENT '配置ID',
  `ftp_upload_id` int(11) DEFAULT NULL COMMENT '上传ftp',
  `domain` varchar(50) NOT NULL COMMENT '域名',
  `site_path` varchar(20) NOT NULL COMMENT '路径',
  `site_name` varchar(100) NOT NULL COMMENT '网站名称',
  `short_name` varchar(100) NOT NULL COMMENT '简短名称',
  `protocol` varchar(20) NOT NULL DEFAULT 'http://' COMMENT '协议',
  `dynamic_suffix` varchar(10) NOT NULL DEFAULT '.jhtml' COMMENT '动态页后缀',
  `static_suffix` varchar(10) NOT NULL DEFAULT '.html' COMMENT '静态页后缀',
  `static_dir` varchar(50) DEFAULT NULL COMMENT '静态页存放目录',
  `is_index_to_root` char(1) NOT NULL DEFAULT '0' COMMENT '是否使用将首页放在根目录下',
  `is_static_index` char(1) NOT NULL DEFAULT '0' COMMENT '是否静态化首页',
  `locale_admin` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '后台本地化',
  `locale_front` varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '前台本地化',
  `tpl_solution` varchar(50) NOT NULL DEFAULT 'default' COMMENT '模板方案',
  `final_step` tinyint(4) NOT NULL DEFAULT '2' COMMENT '终审级别',
  `after_check` tinyint(4) NOT NULL DEFAULT '2' COMMENT '审核后(1:不能修改删除;2:修改后退回;3:修改后不变)',
  `is_relative_path` char(1) NOT NULL DEFAULT '1' COMMENT '是否使用相对路径',
  `is_recycle_on` char(1) NOT NULL DEFAULT '1' COMMENT '是否开启回收站',
  `domain_alias` varchar(255) DEFAULT NULL COMMENT '域名别名',
  `domain_redirect` varchar(255) DEFAULT NULL COMMENT '域名重定向',
  PRIMARY KEY (`site_id`),
  UNIQUE KEY `ak_domain` (`domain`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='站点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jo_authentication`
--

DROP TABLE IF EXISTS `jo_authentication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_authentication` (
  `authentication_id` char(32) NOT NULL COMMENT '认证ID',
  `userid` int(11) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) NOT NULL COMMENT '登录ip',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jo_ftp`
--

DROP TABLE IF EXISTS `jo_ftp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_ftp` (
  `ftp_id` int(11) NOT NULL AUTO_INCREMENT,
  `ftp_name` varchar(100) NOT NULL COMMENT '名称',
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `port` int(11) NOT NULL DEFAULT '21' COMMENT '端口号',
  `username` varchar(100) DEFAULT NULL COMMENT '登录名',
  `password` varchar(100) DEFAULT NULL COMMENT '登陆密码',
  `encoding` varchar(20) NOT NULL DEFAULT 'UTF-8' COMMENT '编码',
  `timeout` int(11) DEFAULT NULL COMMENT '超时时间',
  `ftp_path` varchar(255) DEFAULT NULL COMMENT '路径',
  `url` varchar(255) NOT NULL COMMENT '访问URL',
  PRIMARY KEY (`ftp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='FTP表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jo_template`
--

DROP TABLE IF EXISTS `jo_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_template` (
  `tpl_name` varchar(150) NOT NULL COMMENT '模板名称',
  `tpl_source` longtext COMMENT '模板内容',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `is_directory` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否目录',
  PRIMARY KEY (`tpl_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jo_upload`
--

DROP TABLE IF EXISTS `jo_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_upload` (
  `filename` varchar(150) NOT NULL COMMENT '文件名',
  `length` int(11) NOT NULL COMMENT '文件大小(字节)',
  `last_modified` bigint(20) NOT NULL COMMENT '最后修改时间',
  `content` longblob NOT NULL COMMENT '内容',
  PRIMARY KEY (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传附件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jo_user`
--

DROP TABLE IF EXISTS `jo_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jo_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `password` char(32) NOT NULL COMMENT '密码',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '注册IP',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) NOT NULL DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `reset_key` char(32) DEFAULT NULL COMMENT '重置密码KEY',
  `reset_pwd` varchar(10) DEFAULT NULL COMMENT '重置密码VALUE',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `ak_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-11-14 12:59:41
