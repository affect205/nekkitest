package com.nekkitest;

import com.nekkitest.service.SchedulerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@EnableAutoConfiguration
@Component
@ComponentScan
public class NekkitestApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(NekkitestApplication.class,
				args);

		NekkitestApplication mainObj = ctx.getBean(NekkitestApplication.class);
		SchedulerService service = ctx.getBean(SchedulerService.class);
		service.sayHello();

		mainObj.init();

		System.out.println("Application exited");
	}

	public void init() {
		System.out.println("inside init method");
	}
}
