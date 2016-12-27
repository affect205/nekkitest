package com.nekkitest;

import com.nekkitest.entity.EntryXml;
import com.nekkitest.service.ScanService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.ThreadLocalRandom.current;

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
		time.schedule(st, 0, 5000);

	}

	private static class ScanTask extends TimerTask {
		private ScanService service;
		public ScanTask(ScanService service) {
			this.service = service;
		}
		public void run() {
			generateEntryXmlFile();
			service.doScan(new Date());
		}
	}

	private static EntryXml generateEntryXml() {
		StringBuilder builder = new StringBuilder();
		for (int ndx=0; ndx < current().nextInt(30, 200); ndx++) {
			String str = String.valueOf((char)current().nextInt(65, 91));
			str = current().nextInt(2) % 2 == 0 ? str.toLowerCase() : str.toUpperCase();
			builder.append(str);
		}
		return new EntryXml(-1, builder.toString(), new Date());
	}

	private static void generateEntryXmlFile() {
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("yyyy-mm-dd hh:MM:ss")
				.withZone(ZoneId.systemDefault());
		EntryXml entryXml = generateEntryXml();
		String xmlString = String.format(
				"<entry>\n" +
						"<content>%s</content>\n" +
						"<createdate>%s</createdate>\n" +
				"</entry>", entryXml.getContent(), formatter.format(entryXml.getCreateDate().toInstant())
		);
		System.out.println("GENERATED XML FILE....");
		System.out.println(xmlString);

		String pathStr = NekkitestApplication.class.getClassLoader().getResource("").getPath();
		pathStr = System.getProperty("os.name").contains("indow") ? pathStr.substring(1) : pathStr;

		Path path = Paths.get(pathStr + "/" + System.nanoTime() + ".xml");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(xmlString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
