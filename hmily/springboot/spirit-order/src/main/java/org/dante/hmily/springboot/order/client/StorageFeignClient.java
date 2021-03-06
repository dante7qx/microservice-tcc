package org.dante.hmily.springboot.order.client;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("spirit-tcc-storage")
public interface StorageFeignClient {

	/**
	 * 扣减库存
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Hmily
	@RequestMapping(value = "/decrease_stock/{commodityCode}/{count}", method = RequestMethod.POST)
	public void decreaseStorage(@PathVariable String commodityCode, @PathVariable int count);
	
	/**
	 * 扣减库存 - 模拟异常
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Hmily
	@RequestMapping(value = "/decrease_stock_exception/{commodityCode}/{count}", method = RequestMethod.POST)
	public void decreaseStorageException(@PathVariable String commodityCode, @PathVariable int count);
	
	/**
	 * 扣减库存 - 模拟超时
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Hmily
	@RequestMapping(value = "/decrease_stock_timeout/{commodityCode}/{count}", method = RequestMethod.POST)
	public void decreaseStorageTimeout(@PathVariable String commodityCode, @PathVariable int count);
	
}
