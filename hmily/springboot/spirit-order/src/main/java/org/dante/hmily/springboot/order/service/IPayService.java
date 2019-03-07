package org.dante.hmily.springboot.order.service;

import org.dante.hmily.springboot.order.dto.OrderDTO;
import org.dromara.hmily.annotation.Hmily;

public interface IPayService {

	/**
	 * 支付、创建订单
	 * 
	 * @param order
	 */
	@Hmily
	public void pay(OrderDTO order);
	
	/**
	 * 支付、创建订单 (扣减库存异常)
	 * 
	 * @param order
	 */
	@Hmily
	public void payStockException(OrderDTO order);
	
	/**
	 * 支付、创建订单 (扣减库存超时)
	 * 
	 * @param order
	 */
	@Hmily
	public void payStockTimeout(OrderDTO order);

}
