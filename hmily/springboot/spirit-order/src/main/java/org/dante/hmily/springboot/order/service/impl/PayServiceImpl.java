package org.dante.hmily.springboot.order.service.impl;

import org.dante.hmily.springboot.order.client.AccountFeignClient;
import org.dante.hmily.springboot.order.client.StorageFeignClient;
import org.dante.hmily.springboot.order.dao.OrderDAO;
import org.dante.hmily.springboot.order.dto.OrderDTO;
import org.dante.hmily.springboot.order.enums.OrderStatusEnum;
import org.dante.hmily.springboot.order.po.OrderPO;
import org.dante.hmily.springboot.order.service.IPayService;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayServiceImpl implements IPayService {

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private AccountFeignClient accountFeignClient;
	@Autowired
	private StorageFeignClient storageFeignClient;
	

	@Override
	@Hmily(confirmMethod = "confirm4Pay", cancelMethod = "cancel4Pay")
	public void pay(OrderDTO order) {
		log.info("[ 订单支付 Try ]  -----------> 用户支付、创建订单业务逻辑 <-----------");
		updateOrderStatus(order.getId(), OrderStatusEnum.PAYING.getCode());

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());
		
		// 扣减库存
		storageFeignClient.decreaseStorage(order.getCommodityCode(), order.getTotalCount());
	}
	
	@Override
	@Hmily(confirmMethod = "confirm4Pay", cancelMethod = "cancel4Pay")
	public void payStockException(OrderDTO order) {
		log.info("[ 订单支付 Try ]  -----------> 用户支付、创建订单业务逻辑 <-----------");
		updateOrderStatus(order.getId(), OrderStatusEnum.PAYING.getCode());

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());
		
		// 扣减库存
		storageFeignClient.decreaseStorageException(order.getCommodityCode(), order.getTotalCount());
	}

	@Override
	@Hmily(confirmMethod = "confirm4Pay", cancelMethod = "cancel4Pay")
	@Transactional
	public void payStockTimeout(OrderDTO order) {
		log.info("[ 订单支付 Try ]  -----------> 用户支付、创建订单业务逻辑 <-----------");
		updateOrderStatus(order.getId(), OrderStatusEnum.PAYING.getCode());

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());
		
		// 扣减库存
		storageFeignClient.decreaseStorageTimeout(order.getCommodityCode(), order.getTotalCount());
	}
	
	@Transactional
	public void confirm4Pay(OrderDTO order) {
		log.info("[ 订单支付 Confirm ]  -----------> 用户支付、创建订单业务逻辑 <-----------");
		order.setStatus(OrderStatusEnum.SUCCESS.getCode());
		updateOrderStatus(order.getId(), OrderStatusEnum.SUCCESS.getCode());
	}

	@Transactional
	public void cancel4Pay(OrderDTO order) {
		log.info("[ 订单支付 Cancel ]  -----------> 用户支付、创建订单业务逻辑 <-----------");
		order.setStatus(OrderStatusEnum.FAILURE.getCode());
		updateOrderStatus(order.getId(), OrderStatusEnum.FAILURE.getCode());
	}

	/**
	 * 更新订单状态
	 * 
	 * @param id
	 * @param status
	 */
	private void updateOrderStatus(Long id, String status) {
		OrderPO order = new OrderPO();
		order.setId(id);
		order.setStatus(status);
		orderDAO.updateOrderStatus(id, status);
	}

}
