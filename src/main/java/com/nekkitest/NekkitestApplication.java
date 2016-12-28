package com.nekkitest;

import com.nekkitest.service.ScanService;
import com.nekkitest.utils.TestUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import static com.nekkitest.utils.AppUtils.*;
import static com.nekkitest.utils.TestUtils.*;

//@EnableAutoConfiguration
@Component
@ComponentScan
public class NekkitestApplication {
	public static void main(String[] args) {
		System.out.println("Application has started");
		try {
			ConfigurableApplicationContext ctx = SpringApplication.run(NekkitestApplication.class,  args);

			ConfigurableEnvironment env = ctx.getEnvironment();

			Files.createDirectories(Paths.get(env.getProperty(APP_PROCESSED)));
			Files.createDirectories(Paths.get(env.getProperty(APP_UNPROCESSED)));

			long delay = Long.parseLong(env.getProperty(APP_DELAY, "0"));
			long period = Long.parseLong(env.getProperty(APP_PERIOD, "3000"));

			ScanService service = ctx.getBean(ScanService.class);

			// запускаем генератор
			// startGeneration(env.getProperty(APP_UNPROCESSED), 200, 2000);

			// запускаем мониторинг
			new Timer().schedule(new ScanTask(service), delay, period);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private static class ScanTask extends TimerTask {
		private ScanService service;

		public ScanTask(ScanService service) {
			this.service = service;
		}

		public void run() {
			try {
				service.doScan();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
