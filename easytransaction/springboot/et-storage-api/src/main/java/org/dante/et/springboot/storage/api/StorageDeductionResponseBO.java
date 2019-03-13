package org.dante.et.springboot.storage.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class StorageDeductionResponseBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int lockCount;

}
