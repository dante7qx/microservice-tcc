DROP DATABASE IF EXISTS et_member;
DROP DATABASE IF EXISTS et_storage;
DROP DATABASE IF EXISTS et_order;
DROP DATABASE IF EXISTS et_account_translog;
DROP DATABASE IF EXISTS et_storage_translog;
DROP DATABASE IF EXISTS et_order_translog;

-- --------------------------------
-- 账户表
-- --------------------------------
DROP DATABASE IF EXISTS et_account;
CREATE DATABASE et_account default charset utf8 COLLATE utf8_general_ci;
use et_account;
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `amount` decimal(10,0) DEFAULT '0.00' COMMENT '用户余额',
  `frozen` decimal(10,0) DEFAULT '0.00' COMMENT '冻结金额，扣款暂存余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `t_account` VALUES ('1', 'dante', '4000.00', '0.00');

-- 用于记录业务发起方的最终业务有没有执行
-- p_开头的，代表本事务对应的父事务id
-- select for update查询时，若事务ID对应的记录不存在则事务一定失败了
-- 记录存在，但status为0表示事务成功,为1表示事务失败（包含父事务和本事务）
-- 记录存在，但status为2表示本方法存在父事务，且父事务的最终状态未知
-- 父事务的状态将由发起方通过 优先同步告知 失败则 消息形式告知
DROP TABLE IF EXISTS `executed_trans`;
CREATE TABLE `executed_trans` (
`app_id` smallint(5) unsigned NOT NULL,
`bus_code` smallint(5) unsigned NOT NULL,
`trx_id` bigint(20) unsigned NOT NULL,
`p_app_id` smallint(5) unsigned DEFAULT NULL,
`p_bus_code` smallint(5) unsigned DEFAULT NULL,
`p_trx_id` bigint(20) unsigned DEFAULT NULL,
`status` tinyint(1) NOT NULL,
PRIMARY KEY (`app_id`,`bus_code`,`trx_id`),
KEY `parent` (`p_app_id`,`p_bus_code`,`p_trx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--  幂等性
DROP TABLE IF EXISTS `idempotent`;
CREATE TABLE `idempotent` (
  `src_app_id` smallint(5) unsigned NOT NULL COMMENT '来源AppID',
  `src_bus_code` smallint(5) unsigned NOT NULL COMMENT '来源业务类型',
  `src_trx_id` bigint(20) unsigned NOT NULL COMMENT '来源交易ID',
  `app_id` smallint(5) NOT NULL COMMENT '调用APPID',
  `bus_code` smallint(5) NOT NULL COMMENT '调用的业务代码',
  `call_seq` smallint(5) NOT NULL COMMENT '同一事务同一方法内调用的次数',
  `handler` smallint(5) NOT NULL COMMENT '处理者appid',
  `called_methods` varchar(64) NOT NULL COMMENT '被调用过的方法名',
  `md5` binary(16) NOT NULL COMMENT '参数摘要',
  `sync_method_result` blob COMMENT '同步方法的返回结果',
  `create_time` datetime NOT NULL COMMENT '执行时间',
  `update_time` datetime NOT NULL,
  `lock_version` smallint(32) NOT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`src_app_id`,`src_bus_code`,`src_trx_id`,`app_id`,`bus_code`,`call_seq`,`handler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------
-- 账户事务日志表
-- --------------------------------
et
CREATE DATABASE `et_account_translog` default charset utf8 COLLATE utf8_general_ci;;
USE `et_account_translog`;

CREATE TABLE `trans_log_detail` (
  `log_detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `trans_log_id` binary(12) NOT NULL,
  `log_detail` blob,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`log_detail_id`),
  KEY `app_id` (`trans_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `trans_log_unfinished` (
  `trans_log_id` binary(12) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`trans_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------
-- 库存表
-- --------------------------------
DROP DATABASE IF EXISTS et_storage;
CREATE DATABASE et_storage default charset utf8 COLLATE utf8_general_ci;

use et_storage;
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE `t_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) NOT NULL,
  `name` varchar(255)  NOT NULL,
  `total_count` int(11) DEFAULT '0' NOT NULL COMMENT '总库存',
  `lock_count` int(11) DEFAULT '0' NOT NULL COMMENT '锁定库存',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `t_storage` VALUES ('1', 'C201901140001', '水杯', '1000', '0');

-- 用于记录业务发起方的最终业务有没有执行
-- p_开头的，代表本事务对应的父事务id
-- select for update查询时，若事务ID对应的记录不存在则事务一定失败了
-- 记录存在，但status为0表示事务成功,为1表示事务失败（包含父事务和本事务）
-- 记录存在，但status为2表示本方法存在父事务，且父事务的最终状态未知
-- 父事务的状态将由发起方通过 优先同步告知 失败则 消息形式告知
DROP TABLE IF EXISTS `executed_trans`;
CREATE TABLE `executed_trans` (
`app_id` smallint(5) unsigned NOT NULL,
`bus_code` smallint(5) unsigned NOT NULL,
`trx_id` bigint(20) unsigned NOT NULL,
`p_app_id` smallint(5) unsigned DEFAULT NULL,
`p_bus_code` smallint(5) unsigned DEFAULT NULL,
`p_trx_id` bigint(20) unsigned DEFAULT NULL,
`status` tinyint(1) NOT NULL,
PRIMARY KEY (`app_id`,`bus_code`,`trx_id`),
KEY `parent` (`p_app_id`,`p_bus_code`,`p_trx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--  幂等性
DROP TABLE IF EXISTS `idempotent`;
CREATE TABLE `idempotent` (
  `src_app_id` smallint(5) unsigned NOT NULL COMMENT '来源AppID',
  `src_bus_code` smallint(5) unsigned NOT NULL COMMENT '来源业务类型',
  `src_trx_id` bigint(20) unsigned NOT NULL COMMENT '来源交易ID',
  `app_id` smallint(5) NOT NULL COMMENT '调用APPID',
  `bus_code` smallint(5) NOT NULL COMMENT '调用的业务代码',
  `call_seq` smallint(5) NOT NULL COMMENT '同一事务同一方法内调用的次数',
  `handler` smallint(5) NOT NULL COMMENT '处理者appid',
  `called_methods` varchar(64) NOT NULL COMMENT '被调用过的方法名',
  `md5` binary(16) NOT NULL COMMENT '参数摘要',
  `sync_method_result` blob COMMENT '同步方法的返回结果',
  `create_time` datetime NOT NULL COMMENT '执行时间',
  `update_time` datetime NOT NULL,
  `lock_version` smallint(32) NOT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`src_app_id`,`src_bus_code`,`src_trx_id`,`app_id`,`bus_code`,`call_seq`,`handler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------
-- 库存事务日志表
-- --------------------------------
DROP DATABASE IF EXISTS et_storage_translog;
CREATE DATABASE `et_storage_translog` default charset utf8 COLLATE utf8_general_ci;;
USE `et_storage_translog`;

CREATE TABLE `trans_log_detail` (
  `log_detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `trans_log_id` binary(12) NOT NULL,
  `log_detail` blob,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`log_detail_id`),
  KEY `app_id` (`trans_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `trans_log_unfinished` (
  `trans_log_id` binary(12) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`trans_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------
-- 订单表
-- --------------------------------
DROP DATABASE IF EXISTS et_order;
CREATE DATABASE et_order default charset utf8 COLLATE utf8_general_ci;

use et_order;
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `commodity_code` varchar(255) NOT NULL,
  `total_count` int(11) DEFAULT '0',
  `amount` double(14,2) DEFAULT '0.00' NOT NULL,
  `status` varchar(16) DEFAULT 'CREATING',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- 用于记录业务发起方的最终业务有没有执行
-- p_开头的，代表本事务对应的父事务id
-- select for update查询时，若事务ID对应的记录不存在则事务一定失败了
-- 记录存在，但status为0表示事务成功,为1表示事务失败（包含父事务和本事务）
-- 记录存在，但status为2表示本方法存在父事务，且父事务的最终状态未知
-- 父事务的状态将由发起方通过 优先同步告知 失败则 消息形式告知
DROP TABLE IF EXISTS `executed_trans`;
CREATE TABLE `executed_trans` (
`app_id` smallint(5) unsigned NOT NULL,
`bus_code` smallint(5) unsigned NOT NULL,
`trx_id` bigint(20) unsigned NOT NULL,
`p_app_id` smallint(5) unsigned DEFAULT NULL,
`p_bus_code` smallint(5) unsigned DEFAULT NULL,
`p_trx_id` bigint(20) unsigned DEFAULT NULL,
`status` tinyint(1) NOT NULL,
PRIMARY KEY (`app_id`,`bus_code`,`trx_id`),
KEY `parent` (`p_app_id`,`p_bus_code`,`p_trx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--  幂等性
DROP TABLE IF EXISTS `idempotent`;
CREATE TABLE `idempotent` (
  `src_app_id` smallint(5) unsigned NOT NULL COMMENT '来源AppID',
  `src_bus_code` smallint(5) unsigned NOT NULL COMMENT '来源业务类型',
  `src_trx_id` bigint(20) unsigned NOT NULL COMMENT '来源交易ID',
  `app_id` smallint(5) NOT NULL COMMENT '调用APPID',
  `bus_code` smallint(5) NOT NULL COMMENT '调用的业务代码',
  `call_seq` smallint(5) NOT NULL COMMENT '同一事务同一方法内调用的次数',
  `handler` smallint(5) NOT NULL COMMENT '处理者appid',
  `called_methods` varchar(64) NOT NULL COMMENT '被调用过的方法名',
  `md5` binary(16) NOT NULL COMMENT '参数摘要',
  `sync_method_result` blob COMMENT '同步方法的返回结果',
  `create_time` datetime NOT NULL COMMENT '执行时间',
  `update_time` datetime NOT NULL,
  `lock_version` smallint(32) NOT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`src_app_id`,`src_bus_code`,`src_trx_id`,`app_id`,`bus_code`,`call_seq`,`handler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------
-- 订单事务日志表
-- --------------------------------
DROP DATABASE IF EXISTS et_order_translog;
CREATE DATABASE `et_order_translog` default charset utf8 COLLATE utf8_general_ci;;
USE `et_order_translog`;

CREATE TABLE `trans_log_detail` (
  `log_detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `trans_log_id` binary(12) NOT NULL,
  `log_detail` blob,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`log_detail_id`),
  KEY `app_id` (`trans_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `trans_log_unfinished` (
  `trans_log_id` binary(12) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`trans_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;