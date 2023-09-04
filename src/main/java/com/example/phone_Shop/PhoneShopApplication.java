package com.example.phone_Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PhoneShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneShopApplication.class, args);
	}

}
