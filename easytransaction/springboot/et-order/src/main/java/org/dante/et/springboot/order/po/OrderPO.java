package org.dante.et.springboot.order.po;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dante.et.springboot.order.enums.OrderStatusEnum;

import lombok.Data;

@Data
@Entity
@Table(name = "t_order")
public class OrderPO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String orderNo;
	private String userId;
	private String commodityCode;
	private int totalCount;
	private BigDecimal amount;
	private String status = OrderStatusEnum.CREATING.getCode();
}
