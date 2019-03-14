package org.dante.et.springboot.order.service.impl;

import java.math.BigDecimal;

import org.dante.et.springboot.order.dao.OrderDAO;
import org.dante.et.springboot.order.dto.OrderDTO;
import org.dante.et.springboot.order.enums.OrderStatusEnum;
import org.dante.et.springboot.order.po.OrderPO;
import org.dante.et.springboot.order.service.IOrderService;
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


	@Override
	@Transactional
	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo) {
		log.info("[ 订单 Try ] -----------> 创建订单{}业务逻辑 <-----------", orderNo);
		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount, orderNo));
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO confirmCreateOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo) {
		log.info("[ 订单 Confirm ] -----------> 创建订单{}业务逻辑 <-----------", orderNo);
		OrderPO order = orderDAO.findByOrderNo(orderNo);
		if(order != null) {
			orderDAO.updateOrderStatus(order.getId(), OrderStatusEnum.SUCCESS.getCode());
		}
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		return orderDTO;
	}

	@Override
	@Transactional
	public OrderDTO cancelCreateOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo) {
		log.info("[ 订单 Cancel ] -----------> 创建订单{}业务逻辑 <-----------", orderNo);
		OrderPO order = orderDAO.findByOrderNo(orderNo);
		if(order != null) {
			orderDAO.updateOrderStatus(order.getId(), OrderStatusEnum.FAILURE.getCode());
		}
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
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
	private OrderPO buildOrder(String userId, String commodityCode, int totalCount, double amount, String orderNo) {
		OrderPO order = new OrderPO();
		order.setOrderNo(orderNo);
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setTotalCount(totalCount);
		order.setAmount(BigDecimal.valueOf(amount));
		order.setStatus(OrderStatusEnum.PAYING.getCode());
		return order;
	}

	
	
	
}
