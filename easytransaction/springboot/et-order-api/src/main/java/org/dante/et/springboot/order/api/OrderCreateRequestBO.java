package org.dante.et.springboot.order.api;

import org.dante.et.springboot.order.consts.OrderServiceAPIConstant;

import com.yiqiniu.easytrans.protocol.BusinessIdentifer;
import com.yiqiniu.easytrans.protocol.tcc.TccMethodRequest;

import lombok.Data;

@Data
@BusinessIdentifer(appId = OrderServiceAPIConstant.APPID, busCode = OrderServiceAPIConstant.BUSINESSCODE, rpcTimeOut = 2000)
public class OrderCreateRequestBO implements TccMethodRequest<OrderCreateResponseBO> {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private double amount;
	private String commodityCode;
	private int count;
	private String orderNo;
}
