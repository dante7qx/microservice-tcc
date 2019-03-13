package org.dante.et.springboot.account.service;

import org.dante.et.springboot.account.api.AccountPayRequestBO;
import org.dante.et.springboot.account.api.AccountPayResponseBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiqiniu.easytrans.protocol.tcc.TccMethod;

@Component
public class AccountTccService implements TccMethod<AccountPayRequestBO, AccountPayResponseBO> {
	
	@Autowired
	private IAccountService accountService;

	@Override
	public AccountPayResponseBO doTry(AccountPayRequestBO param) {
		accountService.decreaseAmount(param.getUserId(), param.getAmount());
		AccountPayResponseBO response = new AccountPayResponseBO();
		response.setFrozen(param.getAmount());
		return response;
	}

	@Override
	public void doConfirm(AccountPayRequestBO param) {
		accountService.confirmDecreaseAmount(param.getUserId(), param.getAmount());
	}

	@Override
	public void doCancel(AccountPayRequestBO param) {
		accountService.cancelDecreaseAmount(param.getUserId(), param.getAmount());
	}

	@Override
	public int getIdempotentType() {
		return IDENPOTENT_TYPE_FRAMEWORK;	// 0: 由 ET 框架负责幂等性 1: 由业务方负责幂等性
	}

}
