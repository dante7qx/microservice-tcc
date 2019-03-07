package org.dante.hmily.springboot.order.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dante.hmily.springboot.order.dao.OrderDAO;
import org.dante.hmily.springboot.order.dto.OrderDTO;
import org.dante.hmily.springboot.order.enums.OrderStatusEnum;
import org.dante.hmily.springboot.order.po.OrderPO;
import org.dante.hmily.springboot.order.service.IOrderService;
import org.dante.hmily.springboot.order.service.IPayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private OrderDAO orderDAO;
	@Autowired(required=false)
	private IPayService payService;

	@Override
	@Transactional
	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount));
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		log.info("创建订单 ========> {}", orderDTO);
		payService.pay(orderDTO);
		return orderDTO;
	}
	
	@Override
	@Transactional(noRollbackFor= {RuntimeException.class})
	public OrderDTO createOrderWithStockException(String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount));
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		log.info("创建订单 ========> {}", orderDTO);
		payService.payStockException(orderDTO);
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO createOrderWithStockTimeout(String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount));
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		log.info("创建订单 ========> {}", orderDTO);
		payService.payStockTimeout(orderDTO);
		return orderDTO;
	}
	
	/**
	 * 构造订单
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	private OrderPO buildOrder(String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = new OrderPO();
		order.setOrderNo("BTX_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setTotalCount(totalCount);
		order.setAmount(BigDecimal.valueOf(amount));
		order.setStatus(OrderStatusEnum.CREATING.getCode());
		return order;
	}

}
