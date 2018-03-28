package com.bs.tphoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TphotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TphotoApplication.class, args);
	}



}
