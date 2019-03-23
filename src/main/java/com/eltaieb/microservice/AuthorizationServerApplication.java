package com.eltaieb.microservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.eltaieb.microservice.base.feignclient.UserRoleService;

@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaAuditing
@PropertySource(value = {"classpath:db.properties","classpath:rabbit-mq.properties"})
public class AuthorizationServerApplication {

 
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}
	
	 	
	@Bean
	public CommandLineRunner commandLineRunner(UserRoleService userRoleService)
	{
		return (args) ->{
			 
		};
	}
	 
	 
}

