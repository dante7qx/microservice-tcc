package org.dante.et.springboot.pay.service;

public interface IPayService {
	
	/**
	 * 用户购买商品 - 创建订单、扣减库存、扣除账户余额
	 * 
	 * @param userId
	 * @param amount
	 * @param commodityCode
	 * @param count
	 */
	public void pay(String userId, double amount, String commodityCode, int count);
	
}
