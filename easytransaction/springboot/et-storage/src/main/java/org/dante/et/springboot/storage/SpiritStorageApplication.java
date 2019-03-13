package org.dante.et.springboot.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.yiqiniu.easytrans.EnableEasyTransaction;

@EnableDiscoveryClient
@EnableEasyTransaction
@SpringBootApplication
public class SpiritStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritStorageApplication.class, args);
	}

}
