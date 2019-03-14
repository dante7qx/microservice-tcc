package org.dante.et.springboot.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.yiqiniu.easytrans.EnableEasyTransaction;

@EnableDiscoveryClient
@EnableEasyTransaction
@SpringBootApplication
public class SpiritPayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpiritPayApplication.class, args);
	}

}
