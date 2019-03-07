package org.dante.hmily.springboot.order.service;

import org.dante.hmily.springboot.order.dto.OrderDTO;

public interface IOrderService {
	
	/**
	 * 创建订单 - 扣减库存 -  扣款支付
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount);
	
	/**
	 * 创建订单 - 扣减库存 -  扣款支付 (模拟扣减库存异常)
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	public OrderDTO createOrderWithStockException(String userId, String commodityCode, int totalCount, double amount);
	
	/**
	 * 创建订单 - 扣减库存 -  扣款支付 (模拟扣减库存异常)
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	public OrderDTO createOrderWithStockTimeout(String userId, String commodityCode, int totalCount, double amount);
}
