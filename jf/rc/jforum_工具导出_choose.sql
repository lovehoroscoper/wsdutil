/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : jforum

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2011-08-12 17:32:53
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `jforum_api`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_api`;
CREATE TABLE `jforum_api` (
  `api_id` int(11) NOT NULL AUTO_INCREMENT,
  `api_key` varchar(32) NOT NULL,
  `api_validity` datetime NOT NULL,
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_api
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_attach`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_attach`;
CREATE TABLE `jforum_attach` (
  `attach_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) DEFAULT NULL,
  `privmsgs_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`attach_id`),
  KEY `idx_att_post` (`post_id`),
  KEY `idx_att_priv` (`privmsgs_id`),
  KEY `idx_att_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_attach
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_attach_desc`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_attach_desc`;
CREATE TABLE `jforum_attach_desc` (
  `attach_desc_id` int(11) NOT NULL AUTO_INCREMENT,
  `attach_id` int(11) NOT NULL,
  `physical_filename` varchar(255) NOT NULL,
  `real_filename` varchar(255) NOT NULL,
  `download_count` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `mimetype` varchar(50) DEFAULT NULL,
  `filesize` int(11) DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `thumb` tinyint(1) DEFAULT '0',
  `extension_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`attach_desc_id`),
  KEY `idx_att_d_att` (`attach_id`),
  KEY `idx_att_d_ext` (`extension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_attach_desc
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_attach_quota`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_attach_quota`;
CREATE TABLE `jforum_attach_quota` (
  `attach_quota_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `quota_limit_id` int(11) NOT NULL,
  PRIMARY KEY (`attach_quota_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_attach_quota
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_banlist`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_banlist`;
CREATE TABLE `jforum_banlist` (
  `banlist_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `banlist_ip` varchar(15) DEFAULT NULL,
  `banlist_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`banlist_id`),
  KEY `idx_user` (`user_id`),
  KEY `banlist_ip` (`banlist_ip`),
  KEY `banlist_email` (`banlist_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_banlist
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_banner`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_banner`;
CREATE TABLE `jforum_banner` (
  `banner_id` int(11) NOT NULL AUTO_INCREMENT,
  `banner_name` varchar(90) DEFAULT NULL,
  `banner_placement` int(11) NOT NULL DEFAULT '0',
  `banner_description` varchar(250) DEFAULT NULL,
  `banner_clicks` int(11) NOT NULL DEFAULT '0',
  `banner_views` int(11) NOT NULL DEFAULT '0',
  `banner_url` varchar(250) DEFAULT NULL,
  `banner_weight` tinyint(1) NOT NULL DEFAULT '50',
  `banner_active` tinyint(1) NOT NULL DEFAULT '0',
  `banner_comment` varchar(250) DEFAULT NULL,
  `banner_type` int(11) NOT NULL DEFAULT '0',
  `banner_width` int(11) NOT NULL DEFAULT '0',
  `banner_height` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`banner_id`),
  KEY `banner_id` (`banner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_banner
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_bookmarks`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_bookmarks`;
CREATE TABLE `jforum_bookmarks` (
  `bookmark_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `relation_id` int(11) NOT NULL,
  `relation_type` int(11) NOT NULL,
  `public_visible` int(11) DEFAULT '1',
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bookmark_id`),
  KEY `book_idx_relation` (`relation_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_bookmarks
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_categories`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_categories`;
CREATE TABLE `jforum_categories` (
  `categories_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '',
  `display_order` int(11) NOT NULL DEFAULT '0',
  `moderated` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`categories_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_categories
-- ----------------------------
INSERT INTO `jforum_categories` VALUES ('1', 'Category Test', '1', '0');

-- ----------------------------
-- Table structure for `jforum_config`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_config`;
CREATE TABLE `jforum_config` (
  `config_name` varchar(255) NOT NULL DEFAULT '',
  `config_value` varchar(255) NOT NULL DEFAULT '',
  `config_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_config
-- ----------------------------
INSERT INTO `jforum_config` VALUES ('most.users.ever.online', '1', '1');
INSERT INTO `jforum_config` VALUES ('most.users.ever.online.date', '1313140171781', '2');

-- ----------------------------
-- Table structure for `jforum_extensions`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_extensions`;
CREATE TABLE `jforum_extensions` (
  `extension_id` int(11) NOT NULL AUTO_INCREMENT,
  `extension_group_id` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `upload_icon` varchar(100) DEFAULT NULL,
  `extension` varchar(10) DEFAULT NULL,
  `allow` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`extension_id`),
  KEY `extension_group_id` (`extension_group_id`),
  KEY `extension` (`extension`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_extensions
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_extension_groups`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_extension_groups`;
CREATE TABLE `jforum_extension_groups` (
  `extension_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `allow` tinyint(1) DEFAULT '1',
  `upload_icon` varchar(100) DEFAULT NULL,
  `download_mode` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`extension_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_extension_groups
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_forums`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_forums`;
CREATE TABLE `jforum_forums` (
  `forum_id` int(11) NOT NULL AUTO_INCREMENT,
  `categories_id` int(11) NOT NULL DEFAULT '1',
  `forum_name` varchar(150) NOT NULL DEFAULT '',
  `forum_desc` varchar(255) DEFAULT NULL,
  `forum_order` int(11) DEFAULT '1',
  `forum_topics` int(11) NOT NULL DEFAULT '0',
  `forum_last_post_id` int(11) NOT NULL DEFAULT '0',
  `moderated` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`forum_id`),
  KEY `categories_id` (`categories_id`),
  KEY `idx_forums_cats` (`categories_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_forums
-- ----------------------------
INSERT INTO `jforum_forums` VALUES ('1', '1', 'Test Forum', 'This is a test forum', '1', '2', '2', '0');

-- ----------------------------
-- Table structure for `jforum_forums_watch`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_forums_watch`;
CREATE TABLE `jforum_forums_watch` (
  `forum_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  KEY `idx_fw_forum` (`forum_id`),
  KEY `idx_fw_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_forums_watch
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_groups`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_groups`;
CREATE TABLE `jforum_groups` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(40) NOT NULL DEFAULT '',
  `group_description` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT '0',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_groups
-- ----------------------------
INSERT INTO `jforum_groups` VALUES ('1', 'General', 'General Users', '0');
INSERT INTO `jforum_groups` VALUES ('2', 'Administration', 'Admin Users', '0');

-- ----------------------------
-- Table structure for `jforum_karma`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_karma`;
CREATE TABLE `jforum_karma` (
  `karma_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `post_user_id` int(11) NOT NULL,
  `from_user_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `rate_date` datetime DEFAULT NULL,
  PRIMARY KEY (`karma_id`),
  KEY `post_id` (`post_id`),
  KEY `topic_id` (`topic_id`),
  KEY `post_user_id` (`post_user_id`),
  KEY `from_user_id` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_karma
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_mail_integration`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_mail_integration`;
CREATE TABLE `jforum_mail_integration` (
  `forum_id` int(11) NOT NULL,
  `forum_email` varchar(100) NOT NULL,
  `pop_username` varchar(100) NOT NULL,
  `pop_password` varchar(100) NOT NULL,
  `pop_host` varchar(100) NOT NULL,
  `pop_port` int(11) DEFAULT '110',
  `pop_ssl` tinyint(4) DEFAULT '0',
  KEY `forum_id` (`forum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_mail_integration
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_moderation_log`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_moderation_log`;
CREATE TABLE `jforum_moderation_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `log_description` text NOT NULL,
  `log_original_message` text,
  `log_date` datetime NOT NULL,
  `log_type` tinyint(4) DEFAULT '0',
  `post_id` int(11) DEFAULT '0',
  `topic_id` int(11) DEFAULT '0',
  `post_user_id` int(11) DEFAULT '0',
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  KEY `post_user_id` (`post_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_moderation_log
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_posts`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_posts`;
CREATE TABLE `jforum_posts` (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `forum_id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL DEFAULT '0',
  `post_time` datetime DEFAULT NULL,
  `poster_ip` varchar(15) DEFAULT NULL,
  `enable_bbcode` tinyint(1) NOT NULL DEFAULT '1',
  `enable_html` tinyint(1) NOT NULL DEFAULT '1',
  `enable_smilies` tinyint(1) NOT NULL DEFAULT '1',
  `enable_sig` tinyint(1) NOT NULL DEFAULT '1',
  `post_edit_time` datetime DEFAULT NULL,
  `post_edit_count` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) DEFAULT '1',
  `attach` tinyint(1) DEFAULT '0',
  `need_moderate` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`post_id`),
  KEY `user_id` (`user_id`),
  KEY `topic_id` (`topic_id`),
  KEY `forum_id` (`forum_id`),
  KEY `post_time` (`post_time`),
  KEY `need_moderate` (`need_moderate`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_posts
-- ----------------------------
INSERT INTO `jforum_posts` VALUES ('1', '1', '1', '2', '2005-01-04 16:59:54', '127.0.0.1', '1', '0', '1', '1', null, '0', '1', '0', '0');
INSERT INTO `jforum_posts` VALUES ('2', '2', '1', '2', '2011-08-12 17:09:18', '127.0.0.1', '1', '1', '1', '1', '2011-08-12 17:09:18', '0', '1', '0', '0');

-- ----------------------------
-- Table structure for `jforum_posts_text`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_posts_text`;
CREATE TABLE `jforum_posts_text` (
  `post_id` int(11) NOT NULL,
  `post_text` text,
  `post_subject` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_posts_text
-- ----------------------------
INSERT INTO `jforum_posts_text` VALUES ('1', '[b][color=blue][size=18]Congratulations :!: [/size][/color][/b]\nYou have completed the installation, and JForum is up and running. \n\nTo start administering the board, login as [i]Admin / <the password you supplied in the installer>[/i] and access the [b][url=/admBase/login.page]Admin Control Panel[/url][/b] using the link that shows up in the bottom of the page. There you will be able to create Categories, Forums and much more  :D  \n\nFor more information and support, please refer to the following pages:\n\n:arrow: Community forum: http://www.jforum.net/community.jsp\n:arrow: Documentation: http://www.jforum.net/doc\n\nThank you for choosing JForum.\n\n[url=http://www.jforum.net/doc/Team]The JForum Team[/url]\n\n', 'Welcome to JForum');
INSERT INTO `jforum_posts_text` VALUES ('2', '[color=#3AA315][size=18][b]Support JForum - Help the project[/b][/size][/color]<hr>This project is Open Source, and maintained by at least one full time Senior Developer, [i]which costs US$ 3,000.00 / month[/i]. If it helped you, please consider helping this project - especially with some [b][url=http://www.jforum.net/contribute.jsp]donation[/url][/b].\n\n[color=#137C9F][size=14][b]Why supporting this project is a good thing[/b][/size][/color]<hr>The JForum Project started four years ago as a completely free and Open Source program, initially entirely developed on my (Rafael Steil) free time. Today, with the help of some very valuable people, I can spend more time on JForum, to improve it and implement new features (lots of things, requested either on the [url=http://www.jforum.net/forums/list.page]forums[/url] or registered in the [url=http://www.jforum.net/jira]bug tracker[/url]).\nThat\'s why I\'m asking you to financially support this work. I love Open Source. I love to use good products without having to pay for it too. But when I see some program that is valuable to my work, that helps me making money, I think it\'s a good idea to support this project.\n\n[b]Some reasons to support open projects[/b]:<ul><li>Because Open Source is cool? Yes<li>To thank for a great tool? Yes<li>To help the project evolve because this will help my work and my earnings? Yes</ul>Also, as the project grows more and more, it would be great to, sometimes, reward some of the great people who help JForum.\n\nSo, that\'s what I\'m asking you: if JForum helps your work, saves your time (time is money, remember?) and increase your earnings, support this project. The simpler way is to make [url=http://www.jforum.net/contribute.jsp]any donation[/url] via PayPal.\n\nJForum has grown a lot every day, since four years ago, which is a great thing, and initially it wasn\'t my intention to fully work on this tool. Lately, I\'m spending a lot of time on it, specially to make JForum 3 a reality, to help users, to improve the program, to research about better solutions. So, your support is very welcome!\n\nThanks!\n\n:arrow: [size=16][b][url=http://www.jforum.net/contribute.jsp]Click here[/url][/b] to go to the [i][b][url=http://www.jforum.net/contribute.jsp]\"Support JForum\"[/url][/b][/i] page.[/size]\n\n', 'Support JForum - Please read');

-- ----------------------------
-- Table structure for `jforum_privmsgs`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_privmsgs`;
CREATE TABLE `jforum_privmsgs` (
  `privmsgs_id` int(11) NOT NULL AUTO_INCREMENT,
  `privmsgs_type` tinyint(4) NOT NULL DEFAULT '0',
  `privmsgs_subject` varchar(255) NOT NULL DEFAULT '',
  `privmsgs_from_userid` int(11) NOT NULL DEFAULT '0',
  `privmsgs_to_userid` int(11) NOT NULL DEFAULT '0',
  `privmsgs_date` datetime DEFAULT NULL,
  `privmsgs_ip` varchar(15) NOT NULL DEFAULT '',
  `privmsgs_enable_bbcode` tinyint(1) NOT NULL DEFAULT '1',
  `privmsgs_enable_html` tinyint(1) NOT NULL DEFAULT '0',
  `privmsgs_enable_smilies` tinyint(1) NOT NULL DEFAULT '1',
  `privmsgs_attach_sig` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`privmsgs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_privmsgs
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_privmsgs_text`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_privmsgs_text`;
CREATE TABLE `jforum_privmsgs_text` (
  `privmsgs_id` int(11) NOT NULL,
  `privmsgs_text` text,
  PRIMARY KEY (`privmsgs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_privmsgs_text
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_quota_limit`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_quota_limit`;
CREATE TABLE `jforum_quota_limit` (
  `quota_limit_id` int(11) NOT NULL AUTO_INCREMENT,
  `quota_desc` varchar(50) NOT NULL,
  `quota_limit` int(11) NOT NULL,
  `quota_type` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`quota_limit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_quota_limit
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_ranks`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_ranks`;
CREATE TABLE `jforum_ranks` (
  `rank_id` int(11) NOT NULL AUTO_INCREMENT,
  `rank_title` varchar(50) NOT NULL DEFAULT '',
  `rank_min` int(11) NOT NULL DEFAULT '0',
  `rank_special` tinyint(1) DEFAULT NULL,
  `rank_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_ranks
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_roles`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_roles`;
CREATE TABLE `jforum_roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT '0',
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `idx_group` (`group_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_roles
-- ----------------------------
INSERT INTO `jforum_roles` VALUES ('1', '1', 'perm_vote');
INSERT INTO `jforum_roles` VALUES ('2', '1', 'perm_karma_enabled');
INSERT INTO `jforum_roles` VALUES ('3', '1', 'perm_anonymous_post');
INSERT INTO `jforum_roles` VALUES ('4', '1', 'perm_create_poll');
INSERT INTO `jforum_roles` VALUES ('5', '1', 'perm_bookmarks_enabled');
INSERT INTO `jforum_roles` VALUES ('6', '1', 'perm_attachments_download');
INSERT INTO `jforum_roles` VALUES ('7', '1', 'perm_create_sticky_announcement_topics');
INSERT INTO `jforum_roles` VALUES ('8', '1', 'perm_moderation_log');
INSERT INTO `jforum_roles` VALUES ('9', '2', 'perm_administration');
INSERT INTO `jforum_roles` VALUES ('10', '2', 'perm_moderation');
INSERT INTO `jforum_roles` VALUES ('11', '2', 'perm_moderation_post_remove');
INSERT INTO `jforum_roles` VALUES ('12', '2', 'perm_moderation_post_edit');
INSERT INTO `jforum_roles` VALUES ('13', '2', 'perm_moderation_topic_move');
INSERT INTO `jforum_roles` VALUES ('14', '2', 'perm_moderation_topic_lockUnlock');
INSERT INTO `jforum_roles` VALUES ('15', '2', 'perm_moderation_approve_messages');
INSERT INTO `jforum_roles` VALUES ('16', '2', 'perm_create_sticky_announcement_topics');
INSERT INTO `jforum_roles` VALUES ('17', '2', 'perm_vote');
INSERT INTO `jforum_roles` VALUES ('18', '2', 'perm_create_poll');
INSERT INTO `jforum_roles` VALUES ('19', '2', 'perm_karma_enabled');
INSERT INTO `jforum_roles` VALUES ('20', '2', 'perm_bookmarks_enabled');
INSERT INTO `jforum_roles` VALUES ('21', '2', 'perm_attachments_download');
INSERT INTO `jforum_roles` VALUES ('22', '2', 'perm_moderation_log');
INSERT INTO `jforum_roles` VALUES ('23', '2', 'perm_full_moderation_log');
INSERT INTO `jforum_roles` VALUES ('24', '1', 'perm_forum');
INSERT INTO `jforum_roles` VALUES ('25', '2', 'perm_forum');
INSERT INTO `jforum_roles` VALUES ('26', '1', 'perm_anonymous_post');
INSERT INTO `jforum_roles` VALUES ('27', '2', 'perm_anonymous_post');
INSERT INTO `jforum_roles` VALUES ('28', '1', 'perm_category');
INSERT INTO `jforum_roles` VALUES ('29', '2', 'perm_category');
INSERT INTO `jforum_roles` VALUES ('30', '1', 'perm_read_only_forums');
INSERT INTO `jforum_roles` VALUES ('31', '2', 'perm_read_only_forums');
INSERT INTO `jforum_roles` VALUES ('32', '1', 'perm_html_disabled');
INSERT INTO `jforum_roles` VALUES ('33', '2', 'perm_html_disabled');
INSERT INTO `jforum_roles` VALUES ('34', '1', 'perm_attachments_enabled');
INSERT INTO `jforum_roles` VALUES ('35', '2', 'perm_attachments_enabled');
INSERT INTO `jforum_roles` VALUES ('36', '1', 'perm_reply_only');
INSERT INTO `jforum_roles` VALUES ('37', '2', 'perm_reply_only');
INSERT INTO `jforum_roles` VALUES ('38', '1', 'perm_reply_without_moderation');
INSERT INTO `jforum_roles` VALUES ('39', '2', 'perm_reply_without_moderation');
INSERT INTO `jforum_roles` VALUES ('40', '2', 'perm_moderation_forums');

-- ----------------------------
-- Table structure for `jforum_role_values`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_role_values`;
CREATE TABLE `jforum_role_values` (
  `role_id` int(11) NOT NULL,
  `role_value` varchar(255) DEFAULT NULL,
  KEY `idx_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_role_values
-- ----------------------------
INSERT INTO `jforum_role_values` VALUES ('24', '1');
INSERT INTO `jforum_role_values` VALUES ('25', '1');
INSERT INTO `jforum_role_values` VALUES ('26', '1');
INSERT INTO `jforum_role_values` VALUES ('27', '1');
INSERT INTO `jforum_role_values` VALUES ('28', '1');
INSERT INTO `jforum_role_values` VALUES ('29', '1');
INSERT INTO `jforum_role_values` VALUES ('30', '1');
INSERT INTO `jforum_role_values` VALUES ('31', '1');
INSERT INTO `jforum_role_values` VALUES ('32', '1');
INSERT INTO `jforum_role_values` VALUES ('33', '1');
INSERT INTO `jforum_role_values` VALUES ('34', '1');
INSERT INTO `jforum_role_values` VALUES ('35', '1');
INSERT INTO `jforum_role_values` VALUES ('36', '1');
INSERT INTO `jforum_role_values` VALUES ('37', '1');
INSERT INTO `jforum_role_values` VALUES ('38', '1');
INSERT INTO `jforum_role_values` VALUES ('39', '1');
INSERT INTO `jforum_role_values` VALUES ('40', '1');

-- ----------------------------
-- Table structure for `jforum_sessions`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_sessions`;
CREATE TABLE `jforum_sessions` (
  `session_id` varchar(150) NOT NULL DEFAULT '',
  `session_user_id` int(11) NOT NULL DEFAULT '0',
  `session_start` datetime DEFAULT NULL,
  `session_time` bigint(20) DEFAULT '0',
  `session_ip` varchar(15) NOT NULL DEFAULT '',
  `session_page` int(11) NOT NULL DEFAULT '0',
  `session_logged_int` tinyint(1) DEFAULT NULL,
  KEY `idx_sessions_users` (`session_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_sessions
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_smilies`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_smilies`;
CREATE TABLE `jforum_smilies` (
  `smilie_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL DEFAULT '',
  `url` varchar(100) DEFAULT NULL,
  `disk_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`smilie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_smilies
-- ----------------------------
INSERT INTO `jforum_smilies` VALUES ('1', ':)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\" />', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO `jforum_smilies` VALUES ('2', ':-)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\"/>', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO `jforum_smilies` VALUES ('3', ':D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO `jforum_smilies` VALUES ('4', ':-D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO `jforum_smilies` VALUES ('5', ':(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\" />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO `jforum_smilies` VALUES ('6', ':mrgreen:', '<img src=\"#CONTEXT#/images/smilies/ed515dbff23a0ee3241dcc0a601c9ed6.gif\" />', 'ed515dbff23a0ee3241dcc0a601c9ed6.gif');
INSERT INTO `jforum_smilies` VALUES ('7', ':-o', '<img src=\"#CONTEXT#/images/smilies/47941865eb7bbc2a777305b46cc059a2.gif\"  />', '47941865eb7bbc2a777305b46cc059a2.gif');
INSERT INTO `jforum_smilies` VALUES ('8', ':shock:', '<img src=\"#CONTEXT#/images/smilies/385970365b8ed7503b4294502a458efa.gif\" />', '385970365b8ed7503b4294502a458efa.gif');
INSERT INTO `jforum_smilies` VALUES ('9', ':?:', '<img src=\"#CONTEXT#/images/smilies/0a4d7238daa496a758252d0a2b1a1384.gif\" />', '0a4d7238daa496a758252d0a2b1a1384.gif');
INSERT INTO `jforum_smilies` VALUES ('10', '8)', '<img src=\"#CONTEXT#/images/smilies/b2eb59423fbf5fa39342041237025880.gif\"  />', 'b2eb59423fbf5fa39342041237025880.gif');
INSERT INTO `jforum_smilies` VALUES ('11', ':lol:', '<img src=\"#CONTEXT#/images/smilies/97ada74b88049a6d50a6ed40898a03d7.gif\" />', '97ada74b88049a6d50a6ed40898a03d7.gif');
INSERT INTO `jforum_smilies` VALUES ('12', ':x', '<img src=\"#CONTEXT#/images/smilies/1069449046bcd664c21db15b1dfedaee.gif\"  />', '1069449046bcd664c21db15b1dfedaee.gif');
INSERT INTO `jforum_smilies` VALUES ('13', ':P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO `jforum_smilies` VALUES ('14', ':-P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO `jforum_smilies` VALUES ('15', ':oops:', '<img src=\"#CONTEXT#/images/smilies/499fd50bc713bfcdf2ab5a23c00c2d62.gif\" />', '499fd50bc713bfcdf2ab5a23c00c2d62.gif');
INSERT INTO `jforum_smilies` VALUES ('16', ':cry:', '<img src=\"#CONTEXT#/images/smilies/c30b4198e0907b23b8246bdd52aa1c3c.gif\" />', 'c30b4198e0907b23b8246bdd52aa1c3c.gif');
INSERT INTO `jforum_smilies` VALUES ('17', ':evil:', '<img src=\"#CONTEXT#/images/smilies/2e207fad049d4d292f60607f80f05768.gif\" />', '2e207fad049d4d292f60607f80f05768.gif');
INSERT INTO `jforum_smilies` VALUES ('18', ':twisted:', '<img src=\"#CONTEXT#/images/smilies/908627bbe5e9f6a080977db8c365caff.gif\" />', '908627bbe5e9f6a080977db8c365caff.gif');
INSERT INTO `jforum_smilies` VALUES ('19', ':roll:', '<img src=\"#CONTEXT#/images/smilies/2786c5c8e1a8be796fb2f726cca5a0fe.gif\" />', '2786c5c8e1a8be796fb2f726cca5a0fe.gif');
INSERT INTO `jforum_smilies` VALUES ('20', ':wink:', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO `jforum_smilies` VALUES ('21', ';)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO `jforum_smilies` VALUES ('22', ';-)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO `jforum_smilies` VALUES ('23', ':!:', '<img src=\"#CONTEXT#/images/smilies/9293feeb0183c67ea1ea8c52f0dbaf8c.gif\" />', '9293feeb0183c67ea1ea8c52f0dbaf8c.gif');
INSERT INTO `jforum_smilies` VALUES ('24', ':?', '<img src=\"#CONTEXT#/images/smilies/136dd33cba83140c7ce38db096d05aed.gif\" />', '136dd33cba83140c7ce38db096d05aed.gif');
INSERT INTO `jforum_smilies` VALUES ('25', ':idea:', '<img src=\"#CONTEXT#/images/smilies/8f7fb9dd46fb8ef86f81154a4feaada9.gif\" />', '8f7fb9dd46fb8ef86f81154a4feaada9.gif');
INSERT INTO `jforum_smilies` VALUES ('26', ':arrow:', '<img src=\"#CONTEXT#/images/smilies/d6741711aa045b812616853b5507fd2a.gif\" />', 'd6741711aa045b812616853b5507fd2a.gif');
INSERT INTO `jforum_smilies` VALUES ('27', ':hunf:', '<img src=\"#CONTEXT#/images/smilies/0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif\" />', '0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif');
INSERT INTO `jforum_smilies` VALUES ('28', ':-(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\"  />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO `jforum_smilies` VALUES ('29', ':XD:', '<img src=\"#CONTEXT#/images/smilies/49869fe8223507d7223db3451e5321aa.gif\" />', '49869fe8223507d7223db3451e5321aa.gif');
INSERT INTO `jforum_smilies` VALUES ('30', ':thumbup:', '<img src=\"#CONTEXT#/images/smilies/e8a506dc4ad763aca51bec4ca7dc8560.gif\" />', 'e8a506dc4ad763aca51bec4ca7dc8560.gif');
INSERT INTO `jforum_smilies` VALUES ('31', ':thumbdown:', '<img src=\"#CONTEXT#/images/smilies/e78feac27fa924c4d0ad6cf5819f3554.gif\" />', 'e78feac27fa924c4d0ad6cf5819f3554.gif');
INSERT INTO `jforum_smilies` VALUES ('32', ':|', '<img src=\"#CONTEXT#/images/smilies/1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif\" />', '1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif');

-- ----------------------------
-- Table structure for `jforum_themes`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_themes`;
CREATE TABLE `jforum_themes` (
  `themes_id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(30) NOT NULL DEFAULT '',
  `style_name` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`themes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_themes
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_topics`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_topics`;
CREATE TABLE `jforum_topics` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `forum_id` int(11) NOT NULL DEFAULT '0',
  `topic_title` varchar(100) NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  `topic_time` datetime DEFAULT NULL,
  `topic_views` int(11) DEFAULT '1',
  `topic_replies` int(11) DEFAULT '0',
  `topic_status` tinyint(3) DEFAULT '0',
  `topic_vote_id` int(11) NOT NULL DEFAULT '0',
  `topic_type` tinyint(3) DEFAULT '0',
  `topic_first_post_id` int(11) DEFAULT '0',
  `topic_last_post_id` int(11) NOT NULL DEFAULT '0',
  `topic_moved_id` int(11) DEFAULT '0',
  `moderated` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`topic_id`),
  KEY `forum_id` (`forum_id`),
  KEY `user_id` (`user_id`),
  KEY `topic_first_post_id` (`topic_first_post_id`),
  KEY `topic_last_post_id` (`topic_last_post_id`),
  KEY `topic_moved_id` (`topic_moved_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_topics
-- ----------------------------
INSERT INTO `jforum_topics` VALUES ('1', '1', 'Welcome to JForum', '2', '2005-01-04 16:59:54', '1', '0', '0', '0', '0', '1', '1', '0', '0');
INSERT INTO `jforum_topics` VALUES ('2', '1', 'Support JForum - Please read', '2', '2011-08-12 17:09:18', '1', '0', '0', '0', '2', '2', '2', '0', '0');

-- ----------------------------
-- Table structure for `jforum_topics_watch`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_topics_watch`;
CREATE TABLE `jforum_topics_watch` (
  `topic_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `is_read` tinyint(1) DEFAULT '1',
  KEY `idx_topic` (`topic_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_topics_watch
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_users`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_users`;
CREATE TABLE `jforum_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_active` tinyint(1) DEFAULT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `user_password` varchar(32) NOT NULL DEFAULT '',
  `user_session_time` bigint(20) DEFAULT '0',
  `user_session_page` int(11) NOT NULL DEFAULT '0',
  `user_lastvisit` datetime DEFAULT NULL,
  `user_regdate` datetime DEFAULT NULL,
  `user_level` tinyint(4) DEFAULT NULL,
  `user_posts` int(11) NOT NULL DEFAULT '0',
  `user_timezone` varchar(5) NOT NULL DEFAULT '',
  `user_style` tinyint(4) DEFAULT NULL,
  `user_lang` varchar(255) NOT NULL DEFAULT '',
  `user_dateformat` varchar(20) NOT NULL DEFAULT '%d/%M/%Y %H:%i',
  `user_new_privmsg` int(11) NOT NULL DEFAULT '0',
  `user_unread_privmsg` int(11) NOT NULL DEFAULT '0',
  `user_last_privmsg` datetime DEFAULT NULL,
  `user_emailtime` datetime DEFAULT NULL,
  `user_viewemail` tinyint(1) DEFAULT '0',
  `user_attachsig` tinyint(1) DEFAULT '1',
  `user_allowhtml` tinyint(1) DEFAULT '0',
  `user_allowbbcode` tinyint(1) DEFAULT '1',
  `user_allowsmilies` tinyint(1) DEFAULT '1',
  `user_allowavatar` tinyint(1) DEFAULT '1',
  `user_allow_pm` tinyint(1) DEFAULT '1',
  `user_allow_viewonline` tinyint(1) DEFAULT '1',
  `user_notify` tinyint(1) DEFAULT '1',
  `user_notify_always` tinyint(1) DEFAULT '0',
  `user_notify_text` tinyint(1) DEFAULT '0',
  `user_notify_pm` tinyint(1) DEFAULT '1',
  `user_popup_pm` tinyint(1) DEFAULT '1',
  `rank_id` int(11) DEFAULT '0',
  `user_avatar` varchar(100) DEFAULT NULL,
  `user_avatar_type` tinyint(4) NOT NULL DEFAULT '0',
  `user_email` varchar(255) NOT NULL DEFAULT '',
  `user_icq` varchar(15) DEFAULT NULL,
  `user_website` varchar(255) DEFAULT NULL,
  `user_from` varchar(100) DEFAULT NULL,
  `user_sig` text,
  `user_sig_bbcode_uid` varchar(10) DEFAULT NULL,
  `user_aim` varchar(255) DEFAULT NULL,
  `user_yim` varchar(255) DEFAULT NULL,
  `user_msnm` varchar(255) DEFAULT NULL,
  `user_occ` varchar(100) DEFAULT NULL,
  `user_interests` varchar(255) DEFAULT NULL,
  `user_biography` text,
  `user_actkey` varchar(32) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `themes_id` int(11) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `user_viewonline` tinyint(1) DEFAULT '1',
  `security_hash` varchar(32) DEFAULT NULL,
  `user_karma` double DEFAULT NULL,
  `user_authhash` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_users
-- ----------------------------
INSERT INTO `jforum_users` VALUES ('1', null, 'Anonymous', 'nopass', '0', '0', null, '2011-08-12 17:09:18', null, '0', '', null, '', '%d/%M/%Y %H:%i', '0', '0', null, null, '0', '1', '0', '1', '1', '1', '1', '1', '1', '0', '0', '1', '1', '0', null, '0', '', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '1', null, null, null);
INSERT INTO `jforum_users` VALUES ('2', null, 'Admin', '21232f297a57a5a743894a0e4a801fc3', '0', '0', null, '2011-08-12 17:09:18', null, '1', '', null, '', '%d/%M/%Y %H:%i', '0', '0', null, null, '0', '1', '0', '1', '1', '1', '1', '1', '1', '0', '0', '1', '1', '0', null, '0', '', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '1', null, null, null);

-- ----------------------------
-- Table structure for `jforum_user_groups`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_user_groups`;
CREATE TABLE `jforum_user_groups` (
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  KEY `idx_group` (`group_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_user_groups
-- ----------------------------
INSERT INTO `jforum_user_groups` VALUES ('1', '1');
INSERT INTO `jforum_user_groups` VALUES ('2', '2');

-- ----------------------------
-- Table structure for `jforum_vote_desc`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_vote_desc`;
CREATE TABLE `jforum_vote_desc` (
  `vote_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `vote_text` varchar(255) NOT NULL DEFAULT '',
  `vote_start` datetime NOT NULL,
  `vote_length` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`vote_id`),
  KEY `topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_vote_desc
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_vote_results`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_vote_results`;
CREATE TABLE `jforum_vote_results` (
  `vote_id` int(11) NOT NULL DEFAULT '0',
  `vote_option_id` tinyint(4) NOT NULL DEFAULT '0',
  `vote_option_text` varchar(255) NOT NULL DEFAULT '',
  `vote_result` int(11) NOT NULL DEFAULT '0',
  KEY `vote_id` (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_vote_results
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_vote_voters`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_vote_voters`;
CREATE TABLE `jforum_vote_voters` (
  `vote_id` int(11) NOT NULL DEFAULT '0',
  `vote_user_id` int(11) NOT NULL DEFAULT '0',
  `vote_user_ip` varchar(15) NOT NULL DEFAULT '',
  KEY `vote_id` (`vote_id`),
  KEY `vote_user_id` (`vote_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_vote_voters
-- ----------------------------

-- ----------------------------
-- Table structure for `jforum_words`
-- ----------------------------
DROP TABLE IF EXISTS `jforum_words`;
CREATE TABLE `jforum_words` (
  `word_id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(100) NOT NULL DEFAULT '',
  `replacement` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jforum_words
-- ----------------------------
