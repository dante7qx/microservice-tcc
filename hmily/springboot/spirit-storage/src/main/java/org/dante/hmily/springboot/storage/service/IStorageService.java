package org.dante.hmily.springboot.storage.service;

import org.dante.hmily.springboot.storage.dto.StorageDTO;
import org.dromara.hmily.annotation.Hmily;

public interface IStorageService {
	
	/**
	 * 扣减库存
	 * 
	 * @param commodity
	 * @param count
	 */
	@Hmily
	public void decreaseStock(String commodityCode, int count);
	
	/**
	 * 扣减库存 - 模拟异常
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Hmily
	public void decreaseStockException(String commodityCode, int count);
	
	/**
	 * 扣减库存 - 模拟超时
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Hmily
	public void decreaseStockTimeout(String commodityCode, int count);
	
	public StorageDTO findByCommodityCode(String commodityCode);
	
}
