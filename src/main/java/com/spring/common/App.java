package com.spring.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//uncomment to unable quartz scheduler
//@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.spring"})
@EnableTransactionManagement
@EntityScan(basePackages = "com.spring.model") class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
