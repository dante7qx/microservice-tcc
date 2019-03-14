package org.dante.et.springboot.pay.controller;

import org.dante.et.springboot.pay.dto.PayDTO;
import org.dante.et.springboot.pay.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountPayController {

	@Autowired
	private IPayService payService;

	@PostMapping("/pay")
	public void pay(@RequestBody PayDTO payDTO) {
		payService.pay(payDTO.getUserId(), payDTO.getAmount().doubleValue(), payDTO.getCommodityCode(),
				payDTO.getTotalCount());
	}

}
