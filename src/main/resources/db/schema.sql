/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 03/05/2021 03:48:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_1
-- ----------------------------
DROP TABLE IF EXISTS `test_1`;
CREATE TABLE `test_1`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_int` int(255) NULL DEFAULT NULL COMMENT 'int类型',
  `type_string` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字符串类型',
  `type_double` double(16, 2) NULL DEFAULT NULL COMMENT 'double类型',
  `type_date` datetime NULL DEFAULT NULL COMMENT 'date类型',
  `type_long` bigint(255) NULL DEFAULT NULL COMMENT 'Long类型',
  `same_id` int(11) NULL DEFAULT NULL COMMENT '关联值id',
  `same_val` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联值val',
  `user_id` bigint(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_2
-- ----------------------------
DROP TABLE IF EXISTS `test_2`;
CREATE TABLE `test_2`  (
  `t_num` int(11) NOT NULL AUTO_INCREMENT,
  `t_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`t_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年级',
  `score` double(16, 2) NULL DEFAULT NULL COMMENT '分数',
  `sign_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `same_id` int(255) NULL DEFAULT NULL COMMENT '关联值id',
  `same_val` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联值val',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
