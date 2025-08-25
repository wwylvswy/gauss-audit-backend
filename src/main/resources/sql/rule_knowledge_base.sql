/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : rule_knowledge_base

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:38:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_rule
-- ----------------------------
DROP TABLE IF EXISTS `business_rule`;
CREATE TABLE `business_rule`  (
                                  `rule_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '规则唯一标识',
                                  `business_domain` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务领域',
                                  `rule_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则分类',
                                  `rule_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则描述',
                                  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
                                  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                  PRIMARY KEY (`rule_id`) USING BTREE,
                                  INDEX `idx_business_domain`(`business_domain`) USING BTREE,
                                  INDEX `idx_rule_category`(`rule_category`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业务规则知识库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business_rule
-- ----------------------------
INSERT INTO `business_rule` VALUES (1, '个人存款', '风险管理', '个人活期存款账户单日最高转账限额≤50万元（含本行/跨行）', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (2, '个人存款', '产品定价', '定期存款提前支取时，利息按活期利率的80%计算（不满三个月不计息）', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (3, '个人存款', '风险管理', '大额转账（≥5万）必须进行人脸识别+短信双因子认证', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (4, '个人存款', '合规管理', '未成年人账户禁止开通网上银行，监护人操作需柜台面签', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (5, '个人存款', '合规管理', '存款账户变更记录需保存至独立审计库，保留期限≥10年', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (6, '企业贷款', '风险控制', '企业授信额度≤上年度营业收入的30%，制造业企业可上浮至50%', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (7, '企业贷款', '抵押担保', '不动产抵押率≤评估价值的70%，动产≤50%', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (8, '企业贷款', '客户服务', '贷款到期前30天自动发送还款提醒，逾期3天触发风险预警', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (9, '企业贷款', '合规管理', '贷款资金流向需匹配合同用途，监测到证券投资等异常流向立即冻结', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (10, '企业贷款', '风险控制', '连续2个季度资产负债率>80%的企业列为高风险客户', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (11, '信用卡', '风险控制', '学生群体信用额度≤1万元，需监护人连带担保', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (12, '信用卡', '产品定价', '分期手续费率年化≤18%，单笔分期期限≤36期', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (13, '信用卡', '风险管理', '单日异地消费≥3笔触发交易验证，单笔境外消费≥5万元需电话确认', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (14, '信用卡', '客户激励', '每消费1元累计1积分，房地产/医疗等特定商户不计积分', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (15, '信用卡', '客户权益', '挂失前72小时盗刷损失由银行承担，最高赔付10万元', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (16, '跨境支付', '合规管理', '个人年度购汇额度≤5万美元，需验证身份证+用途证明', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (17, '跨境支付', '产品设计', '实时汇率报价有效期为15秒，超时自动刷新', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (18, '跨境支付', '合规管理', '单日跨境汇款1万美元触发CTR报告，≥5万美元提交STR报告', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (19, '跨境支付', '客户服务', 'SWIFT汇款承诺T+1到账，延迟按日息0.05%补偿', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (20, '跨境支付', '产品定价', '跨境汇款手续费≤汇款金额0.1%（最低50元，最高500元）', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (21, '财富管理', '风险控制', 'R4以上风险产品需客户风险测评≥C4级，且金融资产≥300万元', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (22, '财富管理', '信息披露', '理财产品需明确标注\"业绩比较基准+预期收益\"，历史收益展示≥3年', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (23, '财富管理', '产品设计', '封闭式产品禁止提前赎回，开放式产品赎回资金T+3日内到账', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (24, '财富管理', '风险管理', '单只公募基金持仓≤理财产品净值的20%', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');
INSERT INTO `business_rule` VALUES (25, '财富管理', '客户服务', '产品净值回撤≥10%时自动触发客户风险提示', 1, '2025-08-15 08:42:58', '2025-08-15 08:42:58');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : rule_knowledge_base

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:38:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_rules
-- ----------------------------
DROP TABLE IF EXISTS `base_rules`;
CREATE TABLE `base_rules`  (
                               `rule_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '规则唯一标识',
                               `rule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则类型',
                               `rule_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则详细内容',
                               `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                               `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                               `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
                               PRIMARY KEY (`rule_id`) USING BTREE,
                               INDEX `idx_rule_type`(`rule_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_rules
-- ----------------------------
INSERT INTO `base_rules` VALUES (1, '命名规范', '所有数据库对象（库/表/字段/索引）命名必须使用小写字母开头，后续可跟小写字母或数字', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (2, '命名规范', '数据库对象名称长度不得超过32字节（含下划线），例如：customer_transaction_record超长需简化为 cust_txn_rec', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (3, '数据类型', '数据类型必须明确精度，禁止使用FLOAT/DOUBLE，金额字段必须用DECIMAL(precision, scale)', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (4, '数据类型', '日期时间类型统一使用TIMESTAMP(6)，避免时区混乱', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (5, '索引规范', '单表索引总数不超过5个，联合索引字段不超过3个', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (6, '索引规范', '索引命名必须包含表名缩写和字段名缩写，格式：idx_<表缩写>_<字段缩写>（如用户表邮箱索引：idx_usr_email）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (7, '约束规范', '主键必须使用无业务含义的自增字段（BIGINT GENERATED ALWAYS AS IDENTITY）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (8, '约束规范', '外键字段必须显式声明ON DELETE行为（CASCADE/RESTRICT/SET NULL）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (9, '分区规范', '单表数据量预计超1000万行时必须设计分区策略', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (10, '分区规范', '分区键必须选择高基数字段（如时间戳），禁止使用可更新字段', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (11, '安全规范', '敏感字段定义后必须添加COMMENT \'PII\'标记', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (12, '安全规范', '密码类字段必须采用HASH(sha256)存储', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (13, '命名规范', '禁止使用数据库保留字符为对象名（如 desc/group/order）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (14, '数据类型', '布尔类型必须使用 BOOLEAN，禁止用 CHAR(1)或 INT替代', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (15, '索引规范', '唯一索引必须显式声明 UNIQUE约束', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (16, '分区规范', '分区表必须有默认分区（PARTITION others VALUES LESS THAN (MAXVALUE)）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (17, '存储规范', 'TEXT/BLOB 大对象必须分离到独立扩展表', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (18, '注释规范', '所有字段必须添加 COMMENT说明业务含义（字符数 ≥ 10）', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (19, '兼容规范', '禁用数据库方言特性（如 LIMIT/TOP），使用标准 FETCH FIRST n ROMS', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);
INSERT INTO `base_rules` VALUES (20, '性能规范', '单行数据大小 ≤ 8KB，超限字段转外部存储', '2025-08-15 08:59:46', '2025-08-15 08:59:46', 1);

SET FOREIGN_KEY_CHECKS = 1;
