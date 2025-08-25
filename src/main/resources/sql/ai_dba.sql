/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:37:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_rule
-- ----------------------------
DROP TABLE IF EXISTS `tb_rule`;
CREATE TABLE `tb_rule`  (
                            `id` bigint(0) NOT NULL,
                            `type` int(0) NULL DEFAULT NULL,
                            `title_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题（可以自动生成）',
                            `rule_prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'AI自动生成的prompt',
                            `overview` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '概述',
                            `db_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库类型',
                            `add_time` datetime(0) NULL DEFAULT NULL,
                            `rule_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_rule
-- ----------------------------
INSERT INTO `tb_rule` VALUES (710569555996741, NULL, '命名规范2', '数据库对象名称长度不得超过32字节（含下划线），例如：customer_transaction_record超长需简化为 cust_txn_rec', '名称长度', NULL, '2025-08-19 22:54:58', '基础规则');
INSERT INTO `tb_rule` VALUES (710569589895237, NULL, '数据类型', '数值类型必须明确精度，禁止使用FLOAT/DOUBLE，金额字段必须用DECIMAL(precision, scale)', '数据精度', NULL, '2025-08-19 22:55:06', '基础规则');
INSERT INTO `tb_rule` VALUES (710569624596549, NULL, '数据类型', '日期时间类型统一使用TIMESTAMP(6)，避免时区混乱', '日期标准化', NULL, '2025-08-19 22:55:14', '基础规则');
INSERT INTO `tb_rule` VALUES (710718397730885, NULL, '命名规范1', '所有数据库对象（库/表/字段/索引）命名必须使用小写字母开头，后续可跟小写字母或数字', '大小写规范', NULL, '2025-08-20 09:00:36', '基础规则');
INSERT INTO `tb_rule` VALUES (710719001727045, NULL, '约束规范', '主键必须使用无业务含义的自增字段（BIGINT GENERATED ALWAYS AS IDENTITY）', '主键自增标准', NULL, '2025-08-20 09:03:03', '基础规则');
INSERT INTO `tb_rule` VALUES (711163751567429, NULL, 'test', '1', '1', NULL, '2025-08-21 15:12:45', '基础规则');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_rule_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_rule_type`;
CREATE TABLE `tb_rule_type`  (
                                 `id` bigint(0) NOT NULL,
                                 `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型名称',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则类型库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_rule_type
-- ----------------------------
INSERT INTO `tb_rule_type` VALUES (710565534326853, '风险管理');
INSERT INTO `tb_rule_type` VALUES (710565576831045, '产品定价');
INSERT INTO `tb_rule_type` VALUES (710565700726853, '风险管理');
INSERT INTO `tb_rule_type` VALUES (710565732773957, '合规管理');
INSERT INTO `tb_rule_type` VALUES (710565818101829, '客户服务');
INSERT INTO `tb_rule_type` VALUES (710718228303941, '基础规则');
INSERT INTO `tb_rule_type` VALUES (711164086669381, 'test');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_ultra_rule
-- ----------------------------
DROP TABLE IF EXISTS `tb_ultra_rule`;
CREATE TABLE `tb_ultra_rule`  (
                                  `id` bigint(0) NOT NULL,
                                  `title_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题（可以自动生成）',
                                  `rule_prompt` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'AI自动生成的prompt',
                                  `overview` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '概述',
                                  `add_time` datetime(0) NULL DEFAULT NULL,
                                  `from_file_id` bigint(0) NULL DEFAULT NULL COMMENT '由哪个文件生成',
                                  `rule_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_ultra_rule
-- ----------------------------
INSERT INTO `tb_ultra_rule` VALUES (710566627696709, '单日转账限额', '验证个人活期存款账户的单日累计转账金额（包括本行和跨行转账）是否小于或等于500,000元。如果单日转账总额超过此限额，则数据不符合规范，必须拒绝。', '个人活期存款账户单日最高转限额为50万元，确保资金安全。', '2025-08-19 22:43:03', 710566003646533, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710566628085829, '定期存款提前支取利息', '当定期存款提前支取时，计算利息是否严格按照活期利率的80%执行；如果存款不满三个月，则利息必须为零。验证利息计算结果是否符合此规则，否则数据无效。', '定期存款提前支取利息按活期利率80%计算，不满三个月不计息。', '2025-08-19 22:43:03', 710566003646533, '产品定价');
INSERT INTO `tb_ultra_rule` VALUES (710566628876357, '未成年人网银限制', '检查未成年人账户是否被禁止开通网上银行功能；对于监护人操作，验证是否通过柜台面签完成。如果未成年人账户开通网银或监护人操作未柜台面签，则数据无效。', '未成年人账户禁止开通网银，监护人操作需柜台面签，确保合规。', '2025-08-19 22:43:03', 710566003646533, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710566629240901, '企业授信额度上限', '验证企业贷款授信额度是否小于或等于该企业上年度营业收入的30%。如果授信额度超过此比例，则数据不符合规范，必须调整或拒绝。', '企业授信额度不得超过上年度营业收入的30%，控制信贷风险。', '2025-08-19 22:43:03', 710566003646533, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710566629621829, '抵押率控制', '对于不动产抵押，检查抵押率（贷款金额/评估价）是否≤70%；对于动产抵押，检查抵押率是否≤50%。如果任一抵押率超标，则数据无效，必须修正。', '不动产抵押率≤评估价的70%，动产抵押率≤50%，管理担保风险。', '2025-08-19 22:43:03', 710566003646533, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710566629978181, '学生信用卡额度限制', '针对学生群体的信用卡申请，检查信用额度是否≤10,000元，并且是否有监护人连带担保记录。如果额度超标或担保缺失，则数据不符合规范。', '学生信用卡额度不超过1万元，需监护人连带担保，降低违约风险。', '2025-08-19 22:43:03', 710566003646533, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710566630334533, '个人购汇额度和验证', '验证个人年度购汇额度是否≤50,000美元，并且是否提供了有效身份证件和用途证明文件。如果额度超标或证明文件不全，则数据无效。', '个人年度购汇额度不超过5万美元，需身份证和用途证明，符合外汇监管。', '2025-08-19 22:43:03', 710566003646533, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710566630686789, '高风险产品准入条件', '对于R4及以上风险等级的财富管理产品，检查客户风险测评等级是否≥C4级，并且客户金融资产是否≥3,000,000元。如果任一条件不满足，则禁止销售，数据标记为无效。', 'R4以上风险产品需客户风险测评≥C4级且金融资产≥300万元，确保适当性管理。', '2025-08-19 22:43:03', 710566003646533, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943785312325, '个人存款单日转账限额', '检查个人活期存款账户的单日转账金额（含本行和跨行）是否不超过50万元。如果金额超过50万元，则视为违规。', '确保个人活期存款账户的单日转账限额符合风险管理要求。', '2025-08-21 00:17:42', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943785672773, '定期存款提前支取利息', '验证定期存款提前支取时，利息是否按活期利率的80%计算，且不满三个月的支取不计息。如果计算方式不符，则视为违规。', '定期存款提前支取利息计算规则，基于产品定价原则。', '2025-08-21 00:17:42', 710943268581445, '产品定价');
INSERT INTO `tb_ultra_rule` VALUES (710943786004549, '大额转账双因子认证', '对于金额≥5万元的大额转账，必须进行人脸识别和短信双因子认证。如果认证缺失或不完整，则视为违规。', '大额转账需双因子认证以管理风险。', '2025-08-21 00:17:42', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943786356805, '未成年人账户网银限制', '检查未成年人账户是否禁止开通网上银行，且监护人操作需在柜台面签。如果违反，则视为违规。', '未成年人账户的合规管理措施，防止未授权操作。', '2025-08-21 00:17:42', 710943268581445, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710943786696773, '企业授信额度限制', '验证企业授信额度是否不超过其上年度营业收入的30%。如果超过，则视为违规。', '企业贷款授信额度风险管理规则，基于收入比例。', '2025-08-21 00:17:42', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943787049029, '不动产抵押率上限', '检查不动产抵押率是否不超过评估价的70%。如果超过，则视为违规。', '不动产抵押贷款的风险控制要求。', '2025-08-21 00:17:43', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943787384901, '动产抵押率上限', '验证动产抵押率是否不超过评估价的50%。如果超过，则视为违规。', '动产抵押贷款的风险管理规则。', '2025-08-21 00:17:43', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943787749445, '学生信用卡额度限制', '检查学生群体信用额度是否不超过1万元，且需监护人连带担保。如果额度超限或担保缺失，则视为违规。', '学生信用卡风险控制措施，包括额度和担保要求。', '2025-08-21 00:17:43', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943788097605, '个人购汇额度验证', '验证个人年度购汇额度是否不超过5万美元，且需提供身份证和用途证明。如果额度超限或证明缺失，则视为违规。', '跨境支付购汇的合规管理要求。', '2025-08-21 00:17:43', 710943268581445, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710943788441669, '高风险产品客户资质', '检查风险等级R4及以上产品是否要求客户风险测评等级≥C4级，且金融资产≥300万元。如果资质不符，则视为违规。', '财富管理高风险产品的客户风险管理规则。', '2025-08-21 00:17:43', 710943268581445, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710943886741573, '测试专用', 'test', 'test', '2025-08-21 00:18:07', 710566003646533, '客户服务');
INSERT INTO `tb_ultra_rule` VALUES (710944393859141, '个人存款转限额规则', '验证个人活期存款账户的单日转账金额是否不超过50万元人民币（包括本行和跨行转账）。如果金额超过此限额，则视为违反规则。', '限制个人活期账户单日转账金额以管理操作风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710944394199109, '定期存款提前支取规则', '当定期存款提前支取时，计算利息：如果存款不满三个月，则不计息；如果满三个月或以上，则利息按活期利率的80%计算。验证是否符合此计算逻辑。', '规定定期存款提前支取的利息计算方式，确保定价合规。', '2025-08-21 00:20:11', 710943710883909, '产品定价');
INSERT INTO `tb_ultra_rule` VALUES (710944394539077, '大额转账认证规则', '对于转账金额达到或超过5万元人民币的交易，必须进行人脸识别和短信双因子认证。验证交易记录中是否有相应的认证记录。', '对大额转账实施双因子认证以防范欺诈风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710944394870853, '未成年人账户管理规则', '禁止为未成年人开通网上银行服务。如果监护人需要操作账户，必须通过柜台面签完成。验证账户开户信息及操作记录是否符合此要求。', '确保未成年人账户操作符合监管合规要求。', '2025-08-21 00:20:11', 710943710883909, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710944395255877, '企业授信额度规则', '企业贷款授信额度不得超过其上年度营业收入的30%。验证授信额度数据是否基于营业收入比例计算且未超过上限。', '控制企业贷款额度基于营业收入比例以降低信用风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710944395595845, '抵押率控制规则', '对于不动产抵押，抵押率（贷款金额与评估价的比例）不得超过70%；对于动产抵押，不得超过50%。验证抵押物评估报告和贷款数据是否符合此比例。', '设置抵押物价值上限以管理担保风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710944395935813, '学生信用卡额度规则', '向学生群体发放的信用卡信用额度不得超过1万元人民币，且需要监护人提供连带担保。验证信用额度设置和担保文件是否齐全。', '限制学生信用卡额度并要求担保以控制违约风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (710944396316741, '个人购汇额度规则', '个人年度购汇额度不得超过5万美元。办理购汇时，必须验证身份证件和提供用途证明。验证购汇交易记录和身份验证文件是否符合要求。', '遵守外汇管理规定，限制个人购汇额度并确保交易合规。', '2025-08-21 00:20:11', 710943710883909, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (710944396673093, '高风险产品准入规则', '对于风险等级R4及以上的财富管理产品，客户必须完成风险测评且等级达到C4级或以上，同时金融资产不低于300万元人民币。验证客户风险测评报告和资产证明是否符合标准。', '确保高风险产品只售给合格投资者以管理投资风险。', '2025-08-21 00:20:11', 710943710883909, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164200792133, '个人存款转限额', '验证个人活期存款账户的单日转帐总金额（包括本行和跨行）是否不超过500,000元人民币。如果金额超过此限值，标记为违规并记录详情。', '确保个人存款账户单日转帐上限为50万元，以控制资金风险。', '2025-08-21 15:14:34', 711163812151365, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164201418821, '定期存款提前支取利息', '检查定期存款提前支取时的利息计算：若存款期不满三个月，则不计息；否则，利息必须按当前活期存款利率的80%计算。验证计算逻辑和结果是否符合此规则。', '定期存款提前支取利息按活期利率80%计算，不满三个月无息。', '2025-08-21 15:14:35', 711163812151365, '产品定价');
INSERT INTO `tb_ultra_rule` VALUES (711164201984069, '大额转账双因子认证', '对于个人贷款账户，任何单笔转账金额达到或超过50,000元人民币时，必须强制进行人脸识别和短信验证码双因子认证。验证认证记录是否完整且已执行。', '大额转账需双因子认证以防范欺诈风险。', '2025-08-21 15:14:35', 711163812151365, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164202606661, '未成年人网银限制', '禁止为未成年人（年龄小于18岁）开通网上银行服务。若监护人操作，必须通过银行柜台面签完成。验证账户年龄信息和面签文档是否存在。', '未成年人账户不得开通网银，监护人操作需柜台面签确保合规。', '2025-08-21 15:14:35', 711163812151365, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (711164203184197, '企业授信额度限制', '企业授信额度不得超过其上年度营业收入的30%。基于企业提供的财务数据（如年度报表），计算并验证额度是否合规，否则标记异常。', '企业贷款授信额度基于营收比例设置上限，控制信贷风险。', '2025-08-21 15:14:35', 711163812151365, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164203753541, '贷款抵押率限制', '企业贷款抵押中，不动产抵押率（贷款额/评估价）不超过70%，动产抵押率不超过50%。验证抵押物评估报告和贷款比例数据是否符合此限值。', '抵押贷款中不动产和动产的抵押率上限，保障资产安全。', '2025-08-21 15:14:35', 711163812151365, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164204281925, '学生信用卡额度', '学生群体信用卡信用额度不得超过10,000元人民币，且必须提供监护人连带担保文件。验证额度设置和担保文档是否齐全有效。', '学生信用卡额度上限为1万元，需监护人担保以降低违约风险。', '2025-08-21 15:14:35', 711163812151365, '风险管理');
INSERT INTO `tb_ultra_rule` VALUES (711164204843077, '个人购汇额度验证', '个人年度购汇额度不超过50,000美元。每次购汇交易需验证身份证件和用途证明文件（如旅游或教育证明）。检查额度累计和文档完整性，否则拒绝交易。', '个人购汇有年度上限和身份验证要求，确保外汇合规。', '2025-08-21 15:14:35', 711163812151365, '合规管理');
INSERT INTO `tb_ultra_rule` VALUES (711164205379653, '高风险产品客户资格', '销售R4及以上风险等级财富管理产品时，客户风险测评等级必须达到C4或更高，且客户金融资产不低于3,000,000元人民币。验证风险测评报告和资产证明数据是否达标。', '高风険产品仅限符合资格的高净值客户购买，控制投资风险。', '2025-08-21 15:14:36', 711163812151365, '风险管理');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_upload_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_upload_file`;
CREATE TABLE `tb_upload_file`  (
                                   `id` bigint(0) NOT NULL,
                                   `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件名',
                                   `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件类型',
                                   `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本号',
                                   `file_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '文件内容',
                                   `llm_progress` int(0) NOT NULL DEFAULT 0 COMMENT 'llm推理进度',
                                   `file_pages` int(0) NOT NULL COMMENT '文档页数',
                                   `add_time` datetime(0) NULL DEFAULT NULL,
                                   `llm_starttime` datetime(0) NULL DEFAULT NULL COMMENT '推理开始时间',
                                   `llm_endtime` datetime(0) NULL DEFAULT NULL COMMENT '推理结束时间',
                                   `llm_error_msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '推理失败原因',
                                   `llm_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '推理结果',
                                   `keywords` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '关键字',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '上传文件，供LLM推理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_upload_file
-- ----------------------------
INSERT INTO `tb_upload_file` VALUES (710566003646533, '规则测试1.docx', 'docx', NULL, '\n[Content_Types].xml\n             \n\n\n\n_rels/.rels\n    \n\n\n\nword/document.xml\n          以下是根据表格内容生成的一段话：        在数据库的设计规范中，涵盖了多种业务场景及相应的规则类型。在个人存款方面，包括风险管理规则，如个人活期存款账户单日最高转限额≤50万元（含本行/跨行），以及产品定价规则，如定期存款提前支取时，利息按活期利率的80%计算（不满三个月不计息）等；在个人贷款方面，有风险管理规则，像大额转账（≥5万）必须进行人脸识别+短信双因子认证等，还有合规管理规则，例如未成年人账户禁止开通网上银行，监护人操作需柜台面签等；企业贷款涉及风险控制、抵押担保、客户服务、合规管理等规则，如企业授信额度≤上年度营业收入的30%，不动产抵押率≤评估价的70%，动产≤50%等；信用卡业务包括风险控制、产品定价、客户激励、客户权益等规则，像学生群体信用额度≤1万元，需监护人连带担保等；跨境支付有合规管理、产品设计、产品定价、客户服务等规则，例如个人年度购汇额度≤5万美元，需验身份证+用途证明等；财富管理也涵盖了风险管理、信息披露、产品设计、客户服务等规则，如R4以上风险产品需客户风险测评≥C4级，且金融资产≥300万元等。这些规则从不同维度确保各项业务在合理规范的框架内运行，保障业务的安全性和合规性。     \n\n\n\nword/_rels/document.xml.rels\n        \n\n\n\nword/footnotes.xml\n                 \n\n\n\nword/endnotes.xml\n                 \n\n\n\nword/theme/theme1.xml\n                                                                                                                                                                                                                                   \n\n\n\nword/settings.xml\n                                                              \n\n\n\nword/styles.xml\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        \n\n\n\nword/webSettings.xml\n   \n\n\n\nword/fontTable.xml\n                    \n\n\n\ndocProps/core.xml\n    博文 李   博文 李 2 2025-08-15T13:48:00Z 2025-08-15T13:48:00Z\n\n\n\ndocProps/app.xml\n  Normal.dotm 0 1 75 433 Microsoft Office Word 0 3 1 false  false 507 false false 16.0000\n\n\n', 3, 4, '2025-08-19 22:40:30', '2025-08-19 22:40:40', '2025-08-19 22:43:02', NULL, '{\n    \"keyword\": \"风险管理;产品定价;合规管理;客户服务;安全;合规\",\n    \"rules\": [\n        {\n            \"titleName\": \"单日转账限额\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"验证个人活期存款账户的单日累计转账金额（包括本行和跨行转账）是否小于或等于500,000元。如果单日转账总额超过此限额，则数据不符合规范，必须拒绝。\",\n            \"overview\": \"个人活期存款账户单日最高转限额为50万元，确保资金安全。\"\n        },\n        {\n            \"titleName\": \"定期存款提前支取利息\",\n            \"ruleType\": \"产品定价\",\n            \"rulePrompt\": \"当定期存款提前支取时，计算利息是否严格按照活期利率的80%执行；如果存款不满三个月，则利息必须为零。验证利息计算结果是否符合此规则，否则数据无效。\",\n            \"overview\": \"定期存款提前支取利息按活期利率80%计算，不满三个月不计息。\"\n        },\n        {\n            \"titleName\": \"大额转账双因子认证\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"对于个人贷款相关的大额转账（金额≥50,000元），检查是否已完成人脸识别和短信验证的双因子认证。如果认证缺失或未完成，则数据不符合规范，必须标记为失败。\",\n            \"overview\": \"大额转账（≥5万）需进行人脸识别+短信双因子认证，以防范风险。\"\n        },\n        {\n            \"titleName\": \"未成年人网银限制\",\n            \"ruleType\": \"合规管理\",\n            \"rulePrompt\": \"检查未成年人账户是否被禁止开通网上银行功能；对于监护人操作，验证是否通过柜台面签完成。如果未成年人账户开通网银或监护人操作未柜台面签，则数据无效。\",\n            \"overview\": \"未成年人账户禁止开通网银，监护人操作需柜台面签，确保合规。\"\n        },\n        {\n            \"titleName\": \"企业授信额度上限\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"验证企业贷款授信额度是否小于或等于该企业上年度营业收入的30%。如果授信额度超过此比例，则数据不符合规范，必须调整或拒绝。\",\n            \"overview\": \"企业授信额度不得超过上年度营业收入的30%，控制信贷风险。\"\n        },\n        {\n            \"titleName\": \"抵押率控制\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"对于不动产抵押，检查抵押率（贷款金额/评估价）是否≤70%；对于动产抵押，检查抵押率是否≤50%。如果任一抵押率超标，则数据无效，必须修正。\",\n            \"overview\": \"不动产抵押率≤评估价的70%，动产抵押率≤50%，管理担保风险。\"\n        },\n        {\n            \"titleName\": \"学生信用卡额度限制\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"针对学生群体的信用卡申请，检查信用额度是否≤10,000元，并且是否有监护人连带担保记录。如果额度超标或担保缺失，则数据不符合规范。\",\n            \"overview\": \"学生信用卡额度不超过1万元，需监护人连带担保，降低违约风险。\"\n        },\n        {\n            \"titleName\": \"个人购汇额度和验证\",\n            \"ruleType\": \"合规管理\",\n            \"rulePrompt\": \"验证个人年度购汇额度是否≤50,000美元，并且是否提供了有效身份证件和用途证明文件。如果额度超标或证明文件不全，则数据无效。\",\n            \"overview\": \"个人年度购汇额度不超过5万美元，需身份证和用途证明，符合外汇监管。\"\n        },\n        {\n            \"titleName\": \"高风险产品准入条件\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"对于R4及以上风险等级的财富管理产品，检查客户风险测评等级是否≥C4级，并且客户金融资产是否≥3,000,000元。如果任一条件不满足，则禁止销售，数据标记为无效。\",\n            \"overview\": \"R4以上风险产品需客户风险测评≥C4级且金融资产≥300万元，确保适当性管理。\"\n        }\n    ]\n}', '风险管理;产品定价;合规管理;客户服务;安全;合规');
INSERT INTO `tb_upload_file` VALUES (711163812151365, '规则测试2.docx', 'docx', NULL, '\n[Content_Types].xml\n             \n\n\n\n_rels/.rels\n    \n\n\n\nword/document.xml\n          以下是根据表格内容生成的一段话：        在数据库的设计规范中，涵盖了多种业务场景及相应的规则类型。在个人存款方面，包括风险管理规则，如个人活期存款账户单日最高转限额≤50万元（含本行/跨行），以及产品定价规则，如定期存款提前支取时，利息按活期利率的80%计算（不满三个月不计息）等；在个人贷款方面，有风险管理规则，像大额转账（≥5万）必须进行人脸识别+短信双因子认证等，还有合规管理规则，例如未成年人账户禁止开通网上银行，监护人操作需柜台面签等；企业贷款涉及风险控制、抵押担保、客户服务、合规管理等规则，如企业授信额度≤上年度营业收入的30%，不动产抵押率≤评估价的70%，动产≤50%等；信用卡业务包括风险控制、产品定价、客户激励、客户权益等规则，像学生群体信用额度≤1万元，需监护人连带担保等；跨境支付有合规管理、产品设计、产品定价、客户服务等规则，例如个人年度购汇额度≤5万美元，需验身份证+用途证明等；财富管理也涵盖了风险管理、信息披露、产品设计、客户服务等规则，如R4以上风险产品需客户风险测评≥C4级，且金融资产≥300万元等。这些规则从不同维度确保各项业务在合理规范的框架内运行，保障业务的安全性和合规性。     \n\n\n\nword/_rels/document.xml.rels\n        \n\n\n\nword/footnotes.xml\n                 \n\n\n\nword/endnotes.xml\n                 \n\n\n\nword/theme/theme1.xml\n                                                                                                                                                                                                                                   \n\n\n\nword/settings.xml\n                                                              \n\n\n\nword/styles.xml\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        \n\n\n\nword/webSettings.xml\n   \n\n\n\nword/fontTable.xml\n                    \n\n\n\ndocProps/core.xml\n    博文 李   博文 李 2 2025-08-15T13:48:00Z 2025-08-15T13:48:00Z\n\n\n\ndocProps/app.xml\n  Normal.dotm 0 1 75 433 Microsoft Office Word 0 3 1 false  false 507 false false 16.0000\n\n\n', 3, 4, '2025-08-21 15:13:00', '2025-08-21 15:13:00', '2025-08-21 15:14:34', NULL, '{\n    \"keyword\": \"数据库设计;业务规则;风险管理;产品定价;合规管理;客户服务;数据安全\",\n    \"rules\": [\n        {\n            \"titleName\": \"个人存款转限额\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"验证个人活期存款账户的单日转帐总金额（包括本行和跨行）是否不超过500,000元人民币。如果金额超过此限值，标记为违规并记录详情。\",\n            \"overview\": \"确保个人存款账户单日转帐上限为50万元，以控制资金风险。\"\n        },\n        {\n            \"titleName\": \"定期存款提前支取利息\",\n            \"ruleType\": \"产品定价\",\n            \"rulePrompt\": \"检查定期存款提前支取时的利息计算：若存款期不满三个月，则不计息；否则，利息必须按当前活期存款利率的80%计算。验证计算逻辑和结果是否符合此规则。\",\n            \"overview\": \"定期存款提前支取利息按活期利率80%计算，不满三个月无息。\"\n        },\n        {\n            \"titleName\": \"大额转账双因子认证\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"对于个人贷款账户，任何单笔转账金额达到或超过50,000元人民币时，必须强制进行人脸识别和短信验证码双因子认证。验证认证记录是否完整且已执行。\",\n            \"overview\": \"大额转账需双因子认证以防范欺诈风险。\"\n        },\n        {\n            \"titleName\": \"未成年人网银限制\",\n            \"ruleType\": \"合规管理\",\n            \"rulePrompt\": \"禁止为未成年人（年龄小于18岁）开通网上银行服务。若监护人操作，必须通过银行柜台面签完成。验证账户年龄信息和面签文档是否存在。\",\n            \"overview\": \"未成年人账户不得开通网银，监护人操作需柜台面签确保合规。\"\n        },\n        {\n            \"titleName\": \"企业授信额度限制\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"企业授信额度不得超过其上年度营业收入的30%。基于企业提供的财务数据（如年度报表），计算并验证额度是否合规，否则标记异常。\",\n            \"overview\": \"企业贷款授信额度基于营收比例设置上限，控制信贷风险。\"\n        },\n        {\n            \"titleName\": \"贷款抵押率限制\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"企业贷款抵押中，不动产抵押率（贷款额/评估价）不超过70%，动产抵押率不超过50%。验证抵押物评估报告和贷款比例数据是否符合此限值。\",\n            \"overview\": \"抵押贷款中不动产和动产的抵押率上限，保障资产安全。\"\n        },\n        {\n            \"titleName\": \"学生信用卡额度\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"学生群体信用卡信用额度不得超过10,000元人民币，且必须提供监护人连带担保文件。验证额度设置和担保文档是否齐全有效。\",\n            \"overview\": \"学生信用卡额度上限为1万元，需监护人担保以降低违约风险。\"\n        },\n        {\n            \"titleName\": \"个人购汇额度验证\",\n            \"ruleType\": \"合规管理\",\n            \"rulePrompt\": \"个人年度购汇额度不超过50,000美元。每次购汇交易需验证身份证件和用途证明文件（如旅游或教育证明）。检查额度累计和文档完整性，否则拒绝交易。\",\n            \"overview\": \"个人购汇有年度上限和身份验证要求，确保外汇合规。\"\n        },\n        {\n            \"titleName\": \"高风险产品客户资格\",\n            \"ruleType\": \"风险管理\",\n            \"rulePrompt\": \"销售R4及以上风险等级财富管理产品时，客户风险测评等级必须达到C4或更高，且客户金融资产不低于3,000,000元人民币。验证风险测评报告和资产证明数据是否达标。\",\n            \"overview\": \"高风険产品仅限符合资格的高净值客户购买，控制投资风险。\"\n        }\n    ]\n}', '数据库设计;业务规则;风险管理;产品定价;合规管理;客户服务;数据安全');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
                            `id` bigint(0) NOT NULL AUTO_INCREMENT,
                            `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
                            `nick` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '昵称',
                            `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
                            `create_time` datetime(0) NULL DEFAULT NULL,
                            `role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 709188324065350 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', '管理员', 'aaa42296669b958c3cee6c0475c8093e', '2025-08-13 15:55:31', 'admin');
INSERT INTO `tb_user` VALUES (709188201549893, '李博闻', '管理员', '53535123', '2025-08-16 01:14:12', 'user');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_version
-- ----------------------------
DROP TABLE IF EXISTS `tb_version`;
CREATE TABLE `tb_version`  (
                               `id` bigint(0) NOT NULL,
                               `version_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                               `beta` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为测试版',
                               `part` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '适用角色',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则库版本管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_version
-- ----------------------------
INSERT INTO `tb_version` VALUES (710033138700357, '用户DDL', 0, 'DDL');
INSERT INTO `tb_version` VALUES (710036285673541, '用户DML', 0, 'DML');
INSERT INTO `tb_version` VALUES (710036449919045, '测试DDL', 1, 'DDL');
INSERT INTO `tb_version` VALUES (710939868917829, '测试DML', 1, 'DML');

SET FOREIGN_KEY_CHECKS = 1;


/*
 Navicat Premium Data Transfer

 Source Server         : demand-tencent
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : 175.178.89.189:3306
 Source Schema         : ai_dba

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 09:36:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_version_rule
-- ----------------------------
DROP TABLE IF EXISTS `tb_version_rule`;
CREATE TABLE `tb_version_rule`  (
                                    `version_id` bigint(0) NOT NULL,
                                    `rule_id` bigint(0) NULL DEFAULT NULL,
                                    `ultra_rule_id` bigint(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则版本和具体规则的链接，注意：rule_id和ultra_rule_id不能同时有值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_version_rule
-- ----------------------------
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142006485061, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709141874630725, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142137491525, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142255063109, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142388133957, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142504468549, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142604501061, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142786003013, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709142900301893, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 709143001681989, 0);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138218754117);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138219139141);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138219511877);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138219900997);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138220314693);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138220691525);
INSERT INTO `tb_version_rule` VALUES (709145315790917, 0, 709138221068357);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 709142504468549, 0);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 709142604501061, 0);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 709142786003013, 0);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 709142900301893, 0);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 709143001681989, 0);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 0, 709138219139141);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 0, 709138219900997);
INSERT INTO `tb_version_rule` VALUES (709145880858693, 0, 709138220691525);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 710569555996741, 0);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 710569589895237, 0);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 710569624596549, 0);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 710718397730885, 0);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 710719001727045, 0);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566627696709);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566628085829);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566628876357);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566629240901);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566629621829);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566629978181);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566630334533);
INSERT INTO `tb_version_rule` VALUES (710036449919045, 0, 710566630686789);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 710569555996741, 0);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 710569589895237, 0);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 710569624596549, 0);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566627696709);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566628085829);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566628876357);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566629240901);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566629621829);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566630334533);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566629978181);
INSERT INTO `tb_version_rule` VALUES (710036285673541, 0, 710566630686789);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 710569624596549, 0);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 710569589895237, 0);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 710569555996741, 0);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 710718397730885, 0);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 710719001727045, 0);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566627696709);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566628085829);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566628876357);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566629240901);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566629621829);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710566630334533);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710943786004549);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710943786356805);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710943787384901);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710943786696773);
INSERT INTO `tb_version_rule` VALUES (710939868917829, 0, 710943787049029);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 710569589895237, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 710569555996741, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 710569624596549, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 710718397730885, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 710719001727045, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 711163751567429, 0);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566627696709);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566628085829);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566629621829);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566629978181);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566630334533);
INSERT INTO `tb_version_rule` VALUES (710033138700357, 0, 710566630686789);

SET FOREIGN_KEY_CHECKS = 1;
