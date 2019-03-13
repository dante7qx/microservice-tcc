package org.dante.et.springboot.order.controller;

import org.dante.et.springboot.order.dto.OrderDTO;
import org.dante.et.springboot.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderPayController {

	@Autowired
	private IOrderService orderService;

	@PostMapping("/pay")
	public OrderDTO pay(@RequestBody OrderDTO reqOrder) {
		OrderDTO orderDTO = orderService.createOrder(reqOrder.getUserId(), reqOrder.getCommodityCode(),
				reqOrder.getTotalCount(), reqOrder.getAmount().doubleValue());
		return orderDTO;
	}

}
