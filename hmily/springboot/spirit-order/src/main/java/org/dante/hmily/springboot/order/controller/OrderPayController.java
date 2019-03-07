package org.dante.hmily.springboot.order.controller;

import org.dante.hmily.springboot.order.dto.OrderDTO;
import org.dante.hmily.springboot.order.service.IOrderService;
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
		OrderDTO orderDTO = orderService.createOrder(reqOrder.getUserId(), reqOrder.getCommodityCode(), reqOrder.getTotalCount(),
				reqOrder.getAmount().doubleValue());
		return orderDTO;
	}
	
	@PostMapping("/pay_stock_exception")
	public OrderDTO payStockException(@RequestBody OrderDTO reqOrder) {
		OrderDTO orderDTO = orderService.createOrderWithStockException(reqOrder.getUserId(), reqOrder.getCommodityCode(), reqOrder.getTotalCount(),
				reqOrder.getAmount().doubleValue());
		return orderDTO;
	}
	
	@PostMapping("/pay_stock_timeout")
	public OrderDTO payStockTimeout(@RequestBody OrderDTO reqOrder) {
		OrderDTO orderDTO = orderService.createOrderWithStockTimeout(reqOrder.getUserId(), reqOrder.getCommodityCode(), reqOrder.getTotalCount(),
				reqOrder.getAmount().doubleValue());
		return orderDTO;
	}

}
