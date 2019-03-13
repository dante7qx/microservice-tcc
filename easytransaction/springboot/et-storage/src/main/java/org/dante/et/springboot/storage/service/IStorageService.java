package org.dante.et.springboot.storage.service;

import org.dante.et.springboot.storage.dto.StorageDTO;

public interface IStorageService {

	/**
	 * 扣减库存
	 * 
	 * @param commodity
	 * @param count
	 */
	public void decreaseStock(String commodityCode, int count);
	
	public void confirmDecreaseStock(String commodityCode, int count);
	
	public void cancelDecreaseStock(String commodityCode, int count);

	/**
	 * 扣减库存 - 模拟异常
	 * 
	 * @param commodityCode
	 * @param count
	 */
	public void decreaseStockException(String commodityCode, int count);

	/**
	 * 扣减库存 - 模拟超时
	 * 
	 * @param commodityCode
	 * @param count
	 */
	public void decreaseStockTimeout(String commodityCode, int count);

	public StorageDTO findByCommodityCode(String commodityCode);

}
