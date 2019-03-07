package org.dante.hmily.springboot.account.service;

import org.dante.hmily.springboot.account.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

public interface IAccountService {
	
	/**
	 * 扣款支付
	 * 
	 * @param userId
	 * @param amount
	 */
	@Hmily
	public void decreaseAmount(String userId, double amount);
	
	public void decreaseAmountLocal(String userId, double amount);
	
	public AccountDTO fintByUserId(String userId);
}
