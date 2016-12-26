package com.nekkitest;

import com.nekkitest.service.ScanService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//@EnableAutoConfiguration
@Component
@ComponentScan
public class NekkitestApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(NekkitestApplication.class,
				args);

		//NekkitestApplication mainObj = ctx.getBean(NekkitestApplication.class);
		ScanService service = ctx.getBean(ScanService.class);

		System.out.println("Application has started");

		Timer time = new Timer();
		ScanTask st = new ScanTask(service);
		time.schedule(st, 0, 1000);
	}

	private static class ScanTask extends TimerTask {
		private ScanService service;
		public ScanTask(ScanService service) {
			this.service = service;
		}
		public void run() {
			service.doScan(new Date());
		}
	}
}
