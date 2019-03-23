package com.eltaieb.microservice.authorizationserver.config;

import java.util.concurrent.Executor;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
 
@Configuration
public class BeanConfig {

	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	@Bean("asyncExecutor")
		    public Executor taskExecutor() {
		        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		        executor.setCorePoolSize(4);
		        executor.setMaxPoolSize(20);
		        executor.setQueueCapacity(500);
 		        executor.initialize();
		        return executor;
		    }
      }
	
	
	 

