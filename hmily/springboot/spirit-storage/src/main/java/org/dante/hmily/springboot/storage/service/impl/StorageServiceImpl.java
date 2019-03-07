package org.dante.hmily.springboot.storage.service.impl;

import org.dante.hmily.springboot.storage.dao.StorageDAO;
import org.dante.hmily.springboot.storage.dto.StorageDTO;
import org.dante.hmily.springboot.storage.po.StoragePO;
import org.dante.hmily.springboot.storage.service.IStorageService;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageDAO storageDAO;

	@Override
	@Hmily(confirmMethod = "confirm4Stock", cancelMethod = "cancel4Stock")
	@Transactional
	public void decreaseStock(String commodityCode, int count) {
		log.info("[ 库存 Try ] -----------> 扣减库存业务逻辑 <-----------");
		storageDAO.decreaseStock(commodityCode, count);
	}

	@Override
	@Hmily(confirmMethod = "confirm4Stock", cancelMethod = "cancel4Stock")
	@Transactional
	public void decreaseStockException(String commodityCode, int count) {
		log.info("[ 库存 Try ] -----------> 扣减库存业务逻辑, 模拟异常，调用 decreaseStockException <-----------");
		storageDAO.decreaseStock(commodityCode, count);
		throw new HmilyRuntimeException("扣减库存异常！");
	}

	@Override
	@Hmily(confirmMethod = "confirm4Stock", cancelMethod = "cancel4Stock")
	@Transactional
	public void decreaseStockTimeout(String commodityCode, int count) {
		log.info("扣减库存，模拟超时，调用方法 decreaseStockTimeout");
		try {
			// 模拟延迟 当前线程暂停10秒
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			log.error("异常 -> {}", e.getMessage(), e);
		}
		try {
			storageDAO.decreaseStock(commodityCode, count);
		} catch (Exception e) {
			throw new HmilyRuntimeException("库存不足");
		}
	}

	@Override
	public StorageDTO findByCommodityCode(String commodityCode) {
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		StorageDTO storageDTO = new StorageDTO();
		BeanUtils.copyProperties(storagePO, storageDTO);
		return storageDTO;
	}

	@Transactional
	public void confirm4Stock(String commodityCode, int count) {
		log.info("[ 库存 Confirm ] -----------> 扣减库存业务逻辑 <-----------");
		storageDAO.confirm4Stock(commodityCode, count);
	}

	@Transactional
	public void cancel4Stock(String commodityCode, int count) {
		log.info("[ 库存 Cancel ] -----------> 扣减库存业务逻辑 <-----------");
		storageDAO.cancel4Stock(commodityCode, count);
	}

}
