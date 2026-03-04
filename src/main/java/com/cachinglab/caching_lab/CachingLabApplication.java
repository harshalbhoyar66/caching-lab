package com.cachinglab.caching_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachingLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingLabApplication.class, args);
	}

}
