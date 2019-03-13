package org.dante.et.springboot.account.api;

import org.dante.et.springboot.account.consts.AccountPayServiceAPIConstant;

import com.yiqiniu.easytrans.protocol.BusinessIdentifer;
import com.yiqiniu.easytrans.protocol.tcc.TccMethodRequest;

import lombok.Data;

@Data
@BusinessIdentifer(appId = AccountPayServiceAPIConstant.APPID, busCode = AccountPayServiceAPIConstant.BUSINESSCODE, rpcTimeOut = 2000)
public class AccountPayRequestBO implements TccMethodRequest<AccountPayResponseBO> {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private double amount;

}
