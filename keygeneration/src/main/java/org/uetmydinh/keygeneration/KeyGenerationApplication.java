package org.uetmydinh.keygeneration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KeyGenerationApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeyGenerationApplication.class, args);
    }

}
