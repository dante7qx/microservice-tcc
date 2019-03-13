package org.dante.et.springboot.account.service.impl;

import java.math.BigDecimal;

import org.dante.et.springboot.account.dao.AccountDAO;
import org.dante.et.springboot.account.dto.AccountDTO;
import org.dante.et.springboot.account.po.AccountPO;
import org.dante.et.springboot.account.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountDAO accountDAO;

	@Override
	@Transactional
	public void decreaseAmount(String userId, double amount) {
		log.info("[ 账户 Try ] -----------> 扣款支付业务逻辑 <-----------");
		AccountPO accountPO = accountDAO.findByUserId(userId);
		if(accountPO == null || accountPO.getAmount().doubleValue() < amount) {
			throw new RuntimeException("账户余额不足，请充值后购买！");
		}
 		accountDAO.decreaseAmount(userId, BigDecimal.valueOf(amount));
	}
	
	@Override
	@Transactional
	public void confirmDecreaseAmount(String userId, double amount) {
		log.info("[ 账户 Confirm ] -----------> 扣款支付业务逻辑 <-----------");
		accountDAO.confirm4Amount(userId, BigDecimal.valueOf(amount));
	}

	@Override
	@Transactional
	public void cancelDecreaseAmount(String userId, double amount) {
		log.info("[ 账户 Cancel ] -----------> 扣款支付业务逻辑 <-----------");
		accountDAO.cancel4Amount(userId, BigDecimal.valueOf(amount));
	}

	@Override
	public AccountDTO fintByUserId(String userId) {
		AccountPO accountPO = accountDAO.findByUserId(userId);
		AccountDTO accountDTO = new AccountDTO();
		BeanUtils.copyProperties(accountPO, accountDTO);
		return accountDTO;
	}

	@Override
	@Transactional
	public void decreaseAmountLocal(String userId, double amount) {
		log.info("Local -----------> 扣款支付业务逻辑 <-----------");
		accountDAO.decreaseAmount(userId, BigDecimal.valueOf(amount));
	}

}
