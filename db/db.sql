-- --------------------------------
-- 账户表
-- --------------------------------

DROP DATABASE IF EXISTS tcc_member;
CREATE DATABASE tcc_member default charset utf8 COLLATE utf8_general_ci;

use tcc_member;
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

-- --------------------------------
-- 库存表
-- --------------------------------
DROP DATABASE IF EXISTS tcc_storage;
CREATE DATABASE tcc_storage default charset utf8 COLLATE utf8_general_ci;

use tcc_storage;
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

-- --------------------------------
-- 订单表
-- --------------------------------
DROP DATABASE IF EXISTS tcc_order;
CREATE DATABASE tcc_order default charset utf8 COLLATE utf8_general_ci;

use tcc_order;
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

-- --------------------------------
-- Hmily TCC
-- --------------------------------
DROP DATABASE IF EXISTS hmily_tcc;
CREATE DATABASE hmily_tcc default charset utf8 COLLATE utf8_general_ci;