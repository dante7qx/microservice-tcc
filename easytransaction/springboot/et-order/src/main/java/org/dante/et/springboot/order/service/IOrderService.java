package org.dante.et.springboot.order.service;

import org.dante.et.springboot.order.dto.OrderDTO;

public interface IOrderService {
	
	public static final String BUSINESSCODE = "ACCOUNT_ORDER_PAY";
	
	/**
	 * 创建订单 - 扣减库存 -  扣款支付
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo);
	
	public OrderDTO confirmCreateOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo);
	
	public OrderDTO cancelCreateOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo);
	
}
