package org.dante.et.springboot.storage.service;

import org.dante.et.springboot.storage.api.StorageDeductionRequestBO;
import org.dante.et.springboot.storage.api.StorageDeductionResponseBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiqiniu.easytrans.protocol.tcc.TccMethod;

@Component
public class StorageTccService implements TccMethod<StorageDeductionRequestBO, StorageDeductionResponseBO>{
	
	@Autowired
	private IStorageService storageService;

	@Override
	public StorageDeductionResponseBO doTry(StorageDeductionRequestBO param) {
		// 正常
		storageService.decreaseStock(param.getCommodityCode(), param.getCount());
		// 异常
//		storageService.decreaseStockException(param.getCommodityCode(), param.getCount());
		// 超时
//		storageService.decreaseStockTimeout(param.getCommodityCode(), param.getCount());
		
		StorageDeductionResponseBO response = new StorageDeductionResponseBO();
		response.setLockCount(param.getCount());
		return response;
	}

	@Override
	public void doConfirm(StorageDeductionRequestBO param) {
		storageService.confirmDecreaseStock(param.getCommodityCode(), param.getCount());
	}

	@Override
	public void doCancel(StorageDeductionRequestBO param) {
		storageService.cancelDecreaseStock(param.getCommodityCode(), param.getCount());
	}

	@Override
	public int getIdempotentType() {
		return IDENPOTENT_TYPE_FRAMEWORK;
	}
	
}
