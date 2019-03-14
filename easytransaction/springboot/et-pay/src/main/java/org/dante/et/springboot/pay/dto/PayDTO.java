package org.dante.et.springboot.pay.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class PayDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String commodityCode;
	private int totalCount;
	private BigDecimal amount;
}
