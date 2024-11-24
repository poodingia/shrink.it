package org.uetmydinh.appserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppserverApplication.class, args);
	}

}
