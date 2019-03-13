package org.dante.et.springboot.order.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.dante.et.springboot.account.api.AccountPayRequestBO;
import org.dante.et.springboot.account.api.AccountPayResponseBO;
import org.dante.et.springboot.order.consts.OrderConstant;
import org.dante.et.springboot.order.dao.OrderDAO;
import org.dante.et.springboot.order.dto.OrderDTO;
import org.dante.et.springboot.order.enums.OrderStatusEnum;
import org.dante.et.springboot.order.po.OrderPO;
import org.dante.et.springboot.order.service.IOrderService;
import org.dante.et.springboot.storage.api.StorageDeductionRequestBO;
import org.dante.et.springboot.storage.api.StorageDeductionResponseBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiqiniu.easytrans.core.EasyTransFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private OrderDAO orderDAO;
	@Resource
	private EasyTransFacade transaction;

	@Override
	@Transactional
	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount));
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		log.info("创建订单 ========> {}", orderDTO);
		
		// 声明全局事务ID，其由appId,业务代码，业务代码内ID构成
		transaction.startEasyTrans(OrderConstant.APPID.concat("|").concat(BUSINESSCODE), orderDTO.getId());
		
		// 扣减库存
		decreaseStock(commodityCode, totalCount);
		
		// 帐户扣款
		decreaseAccount(userId, amount);
		
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
//		order.setStatus(OrderStatusEnum.CREATING.getCode());
		order.setStatus(OrderStatusEnum.SUCCESS.getCode());
		return order;
	}
	
	/**
	 * 扣减库存
	 * 
	 * @param commodityCode
	 * @param totalCount
	 * @return
	 */
	private Future<StorageDeductionResponseBO> decreaseStock(String commodityCode, int totalCount) {
		StorageDeductionRequestBO storageDeductionRequest = new StorageDeductionRequestBO();
		storageDeductionRequest.setCommodityCode(commodityCode);
		storageDeductionRequest.setCount(totalCount);
		Future<StorageDeductionResponseBO> storageDeductFuture = transaction.execute(storageDeductionRequest);
		return storageDeductFuture;
	}
	
	/**
	 * 帐户扣款
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	private Future<AccountPayResponseBO> decreaseAccount(String userId, double amount) {
		AccountPayRequestBO accountPayRequest = new AccountPayRequestBO();
		accountPayRequest.setUserId(userId);
		accountPayRequest.setAmount(amount);
		Future<AccountPayResponseBO> accountPayFuture = transaction.execute(accountPayRequest);
		return accountPayFuture;
	}

}
