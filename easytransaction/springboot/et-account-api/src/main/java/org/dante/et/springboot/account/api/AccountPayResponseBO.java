package org.dante.et.springboot.account.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountPayResponseBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double frozen;

}
