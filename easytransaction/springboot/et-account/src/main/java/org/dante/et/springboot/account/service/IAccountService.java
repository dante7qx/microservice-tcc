package org.dante.et.springboot.account.service;

import org.dante.et.springboot.account.dto.AccountDTO;

public interface IAccountService {
	
	/**
	 * 扣款支付
	 * 
	 * @param userId
	 * @param amount
	 */
	public void decreaseAmount(String userId, double amount);
	
	public void confirmDecreaseAmount(String userId, double amount);
	
	public void cancelDecreaseAmount(String userId, double amount);
	
	/**
	 * 扣款支付（本地事务）
	 * 
	 * @param userId
	 * @param amount
	 */
	public void decreaseAmountLocal(String userId, double amount);
	
	public AccountDTO fintByUserId(String userId);
}
