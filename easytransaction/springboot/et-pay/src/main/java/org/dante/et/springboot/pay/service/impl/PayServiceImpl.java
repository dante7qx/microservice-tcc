package org.dante.et.springboot.pay.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.dante.et.springboot.account.api.AccountPayRequestBO;
import org.dante.et.springboot.account.api.AccountPayResponseBO;
import org.dante.et.springboot.order.api.OrderCreateRequestBO;
import org.dante.et.springboot.order.api.OrderCreateResponseBO;
import org.dante.et.springboot.pay.consts.PayConstant;
import org.dante.et.springboot.pay.service.IPayService;
import org.dante.et.springboot.storage.api.StorageDeductionRequestBO;
import org.dante.et.springboot.storage.api.StorageDeductionResponseBO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiqiniu.easytrans.core.EasyTransFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PayServiceImpl implements IPayService {
	
	@Resource
	private EasyTransFacade transaction;

	@Override
	@Transactional
	public void pay(String userId, double amount, String commodityCode, int count) {
		// 支付流水（生产环境可存储入库）
		long paySeq = Instant.now().toEpochMilli();
		log.info("{} 购买 {}，流水号{}", userId, commodityCode, paySeq);
		// 声明全局事务ID，其由appId,业务代码，业务代码内ID构成
		transaction.startEasyTrans(PayConstant.APPID.concat("|").concat(PayConstant.BUSINESSCODE), paySeq);
		
		// 创建订单
		createOrder(userId, amount, commodityCode, count);
		
		// 扣减库存
		decreaseStock(commodityCode, count);
		
		// 帐户扣款
		decreaseAccount(userId, amount);
	}
	
	/**
	 * 创建订单
	 * 
	 * @param userId
	 * @param amount
	 * @param commodityCode
	 * @param count
	 */
	public Future<OrderCreateResponseBO> createOrder(String userId, double amount, String commodityCode, int count) {
		String orderNo = "ET_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
		OrderCreateRequestBO orderCreateRequest = new OrderCreateRequestBO();
		orderCreateRequest.setUserId(userId);
		orderCreateRequest.setAmount(amount);
		orderCreateRequest.setCommodityCode(commodityCode);
		orderCreateRequest.setCount(count);
		orderCreateRequest.setOrderNo(orderNo);
		Future<OrderCreateResponseBO> orderCreateFuture = transaction.execute(orderCreateRequest);
		return orderCreateFuture;
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
