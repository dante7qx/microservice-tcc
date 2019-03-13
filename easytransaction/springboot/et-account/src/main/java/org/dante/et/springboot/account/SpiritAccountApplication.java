package org.dante.et.springboot.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.yiqiniu.easytrans.EnableEasyTransaction;

@EnableDiscoveryClient
@EnableEasyTransaction
@SpringBootApplication
public class SpiritAccountApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpiritAccountApplication.class, args);
	}

}
