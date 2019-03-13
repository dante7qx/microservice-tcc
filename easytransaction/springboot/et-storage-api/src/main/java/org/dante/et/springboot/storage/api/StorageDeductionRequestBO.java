package org.dante.et.springboot.storage.api;

import org.dante.et.springboot.storage.consts.StoragePayServiceAPIConstant;

import com.yiqiniu.easytrans.protocol.BusinessIdentifer;
import com.yiqiniu.easytrans.protocol.tcc.TccMethodRequest;

import lombok.Data;

@Data
@BusinessIdentifer(appId = StoragePayServiceAPIConstant.APPID, busCode = StoragePayServiceAPIConstant.BUSINESSCODE, rpcTimeOut = 2000)
public class StorageDeductionRequestBO implements TccMethodRequest<StorageDeductionResponseBO> {

	private static final long serialVersionUID = 1L;
	
	private String commodityCode;
	private int count;

}
