package org.dante.et.springboot.order.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderCreateResponseBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long orderId;
	private String orderNo;
	private String status;

}
