package org.dante.hmily.springboot.account.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AccountDTO {
	private Long id;
	private String userId;
	private BigDecimal amount;
	private BigDecimal frozen;
}
