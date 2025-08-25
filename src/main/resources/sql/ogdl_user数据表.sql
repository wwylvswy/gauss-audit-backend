/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for og_datasource
-- ----------------------------
DROP TABLE IF EXISTS `og_datasource`;
CREATE TABLE `og_datasource`  (
                                  `id` bigint(0) NOT NULL AUTO_INCREMENT,
                                  `datasource_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `host` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `port` int(0) NOT NULL,
                                  `database_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
                                  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
                                  `is_delete` tinyint(0) NULL DEFAULT 0,
                                  `status` tinyint(1) NOT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_name`(`datasource_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of og_datasource
-- ----------------------------
INSERT INTO `og_datasource` VALUES (1, 'gauss_test_1', '192.168.12.13', 3308, 'department', 'icbc', '7777777', 'admin', '2025-08-19 00:42:27', '2025-08-18 16:46:35', 1, 1);
INSERT INTO `og_datasource` VALUES (2, 'gauss_test_2', '175.178.89.189', 5432, 'postgres', 'gaussdb', 'Pwd@1234', 'admin', '2025-08-19 01:16:56', '2025-08-21 08:27:01', 1, 1);
INSERT INTO `og_datasource` VALUES (3, 'gauss_db_test', '175.178.89.189', 5432, 'postgres', 'gaussdb', 'Pwd@1234', 'admin', '2025-08-21 16:31:36', '2025-08-21 08:31:38', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for review_rule
-- ----------------------------
DROP TABLE IF EXISTS `review_rule`;
CREATE TABLE `review_rule`  (
                                `rule_id` bigint(0) NOT NULL AUTO_INCREMENT,
                                `enabled` bit(1) NOT NULL,
                                `rule_description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                `rule_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review_rule
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父菜单ID',
                             `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由路径',
                             `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称',
                             `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单标题',
                             `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
                             `mrank` int(0) NULL DEFAULT 0 COMMENT '排序',
                             `is_hidden` tinyint(0) NULL DEFAULT 0 COMMENT '是否隐藏：1是',
                             `roles` json NULL COMMENT '角色数组，如 [\"admin\", \"common\"]',
                             `auths` json NULL COMMENT '按钮权限数组，如 [\"permission:btn:add\"]',
                             `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '/permission', NULL, 'Permission', '权限管理', 'ep:lollipop', 10, 0, '[\"admin\", \"common\"]', NULL, '2025-08-17 03:58:19');
INSERT INTO `sys_menu` VALUES (2, 1, '/permission/page/index', 'permission/page/index', 'PermissionPage', '页面权限', NULL, 1, 0, '[\"admin\", \"common\"]', NULL, '2025-08-17 03:58:19');
INSERT INTO `sys_menu` VALUES (3, 1, '/permission/button', NULL, 'PermissionButton', '按钮权限', NULL, 2, 0, '[\"admin\", \"common\"]', NULL, '2025-08-17 03:58:19');
INSERT INTO `sys_menu` VALUES (4, 3, '/permission/button/router', 'permission/button/index', 'PermissionButtonRouter', '路由返回按钮权限', NULL, 1, 0, '[\"admin\", \"common\"]', '[\"permission:btn:add\", \"permission:btn:edit\", \"permission:btn:delete\"]', '2025-08-17 03:58:19');
INSERT INTO `sys_menu` VALUES (5, 3, '/permission/button/login', 'permission/button/perms', 'PermissionButtonLogin', '登录接口返回按钮权限', NULL, 2, 0, '[\"admin\", \"common\"]', NULL, '2025-08-17 03:58:19');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:35:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
                             `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识（admin, common）',
                             `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态',
                             `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', 1, '2025-08-17 03:57:23');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'common', 1, '2025-08-17 03:57:23');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:35:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
                           `department` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属部门',
                           `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系电话',
                           `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电子邮箱',
                           `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
                           `enabled` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
                           `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                           `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
                           `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                           `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '账户密码',
                           `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
                           `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `account`(`account`) USING BTREE,
                           INDEX `idx_department`(`department`) USING BTREE,
                           INDEX `idx_create_time`(`create_time`) USING BTREE,
                           INDEX `idx_account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '12312', '123123', '3123213', '2312312', '23123', 1, '2025-08-15 08:31:29', '2025-08-16 02:48:05', 1, '', NULL, NULL);
INSERT INTO `t_user` VALUES (2, '23123', '3123', '13213', '1231231', 'admin', 1, '2025-08-16 01:26:55', '2025-08-16 02:49:10', 1, '', NULL, NULL);
INSERT INTO `t_user` VALUES (3, 'icbc1', '开发1部', '18888888888', '666666@icbc.com', 'admin', 1, '2025-08-16 02:27:34', '2025-08-18 03:08:40', 0, '666666', NULL, NULL);
INSERT INTO `t_user` VALUES (4, 'icbc2', '开发2部', '18888888887', '777777@icbc.com', 'admin', 1, '2025-08-16 02:30:30', '2025-08-18 03:06:40', 0, '666666', NULL, NULL);
INSERT INTO `t_user` VALUES (5, '', '', '', '', 'admin', 1, '2025-08-16 02:53:57', '2025-08-16 02:54:06', 1, '', NULL, NULL);
INSERT INTO `t_user` VALUES (6, 'icbc3', '服务支持部', '18888888886', '888888@icbc.com', 'admin', 1, '2025-08-17 09:23:08', '2025-08-18 03:08:28', 0, '111111', '张三', 'https://example.com/avatars/zhangsan.jpg');
INSERT INTO `t_user` VALUES (7, 'admin', '开发2部', '18888888888', '111111@icbc.com', 'admin', 1, '2025-08-17 09:28:45', '2025-08-20 16:34:47', 0, 'admin123', '管理员', 'https://ts1.tc.mm.bing.net/th/id/OIP-C.sfxCiawjc8Xqb3ehq3xtRAHaHa?rs=1&pid=ImgDetMain&o=7&rm=3');

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : gauss_audit

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:35:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (7, 1);

SET FOREIGN_KEY_CHECKS = 1;

