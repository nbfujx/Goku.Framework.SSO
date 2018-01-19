/*
Navicat MySQL Data Transfer

Source Server         : mysql107
Source Server Version : 50720
Source Host           : 47.97.198.227:3306
Source Database       : goku_sso

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-01-18 15:58:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sso_auth
-- ----------------------------
DROP TABLE IF EXISTS `sso_auth`;
CREATE TABLE `sso_auth` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `system_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sso_auth
-- ----------------------------

-- ----------------------------
-- Table structure for sso_log
-- ----------------------------
DROP TABLE IF EXISTS `sso_log`;
CREATE TABLE `sso_log` (
  `id` varchar(32) NOT NULL,
  `system_code` varchar(100) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sso_log
-- ----------------------------

-- ----------------------------
-- Table structure for sso_system
-- ----------------------------
DROP TABLE IF EXISTS `sso_system`;
CREATE TABLE `sso_system` (
  `Id` varchar(32) NOT NULL,
  `code` varchar(100) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(100) NOT NULL,
  `login_url` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `if_show` varchar(1) DEFAULT '1' COMMENT '0 不启用 1 启用',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sso_system
-- ----------------------------

-- ----------------------------
-- Table structure for sso_user
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `id` varchar(32) NOT NULL,
  `sso_code` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `password` varchar(500) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `idcard` varchar(25) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `last_login_system_id` varchar(32) DEFAULT NULL,
  `last_login_ip` varchar(20) DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sso_user
-- ----------------------------
