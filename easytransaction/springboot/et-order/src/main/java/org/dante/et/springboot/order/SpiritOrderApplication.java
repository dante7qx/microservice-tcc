package org.dante.et.springboot.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.yiqiniu.easytrans.EnableEasyTransaction;

@EnableDiscoveryClient
@EnableEasyTransaction
@SpringBootApplication
public class SpiritOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritOrderApplication.class, args);
	}

}
