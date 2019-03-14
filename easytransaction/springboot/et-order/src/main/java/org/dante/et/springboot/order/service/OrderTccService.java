package org.dante.et.springboot.order.service;

import org.dante.et.springboot.order.api.OrderCreateRequestBO;
import org.dante.et.springboot.order.api.OrderCreateResponseBO;
import org.dante.et.springboot.order.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiqiniu.easytrans.protocol.tcc.TccMethod;

@Component
public class OrderTccService implements TccMethod<OrderCreateRequestBO, OrderCreateResponseBO> {

	@Autowired
	private IOrderService orderService;

	@Override
	public OrderCreateResponseBO doTry(OrderCreateRequestBO param) {
		OrderDTO orderDTO = orderService.createOrder(param.getUserId(), param.getCommodityCode(), param.getCount(),
				param.getAmount(), param.getOrderNo());
		OrderCreateResponseBO response = new OrderCreateResponseBO();
		response.setOrderId(orderDTO.getId());
		response.setOrderNo(orderDTO.getOrderNo());
		response.setStatus(orderDTO.getStatus());
		return response;
	}

	@Override
	public void doConfirm(OrderCreateRequestBO param) {
		orderService.confirmCreateOrder(param.getUserId(), param.getCommodityCode(), param.getCount(),
				param.getAmount(), param.getOrderNo());
	}

	@Override
	public void doCancel(OrderCreateRequestBO param) {
		orderService.cancelCreateOrder(param.getUserId(), param.getCommodityCode(), param.getCount(), param.getAmount(),
				param.getOrderNo());
	}

	@Override
	public int getIdempotentType() {
		return IDENPOTENT_TYPE_FRAMEWORK;
	}
}
