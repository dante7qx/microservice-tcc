package org.dante.et.springboot.account.controller;

import org.dante.et.springboot.account.dto.AccountDTO;
import org.dante.et.springboot.account.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	
	@Autowired
	private IAccountService accountService;
	
	@PostMapping("/decrease_acmout/{userId}/{amount}")
	public AccountDTO decreaseAccount(@PathVariable String userId, @PathVariable double amount) {
		accountService.decreaseAmount(userId, amount);
		AccountDTO accountDTO = accountService.fintByUserId(userId);
		return accountDTO;
	}
	
	@PostMapping("/decrease_acmout_local/{userId}/{amount}")
	public AccountDTO decreaseAccountLocal(@PathVariable String userId, @PathVariable double amount) {
		accountService.decreaseAmountLocal(userId, amount);
		AccountDTO accountDTO = accountService.fintByUserId(userId);
		return accountDTO;
	}
	
}
