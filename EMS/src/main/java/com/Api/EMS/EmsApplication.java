package com.Api.EMS; // Make sure this package includes 'repository' as a sub-package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories; // Correct annotation for MongoDB

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.Api.EMS.repository")
public class EmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmsApplication.class, args);
	}
}
