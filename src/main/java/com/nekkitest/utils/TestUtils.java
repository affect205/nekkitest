package com.nekkitest.utils;

import com.nekkitest.entity.EntryXml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Created by Alex on 28.12.2016.
 */
public class TestUtils {
    public static void startGeneration(String path, long delay, long period) {
        new Thread(() -> {
            try {
                if (delay > 0) Thread.currentThread().sleep(delay);
                while(true) {
                    generateEntryXmlFile(path);
                    Thread.currentThread().sleep(period);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static EntryXml generateEntryXml() {
        StringBuilder builder = new StringBuilder();
        for (int ndx=0; ndx < current().nextInt(30, 200); ndx++) {
            String str = String.valueOf((char)current().nextInt(65, 91));
            str = current().nextInt(2) % 2 == 0 ? str.toLowerCase() : str.toUpperCase();
            builder.append(str);
        }
        return new EntryXml(null, builder.toString(), new Date());
    }

    private static void generateEntryXmlFile(String destPath) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd hh:mm:ss")
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

        Path path = Paths.get(destPath + "/" + System.nanoTime() + ".xml");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(xmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMonitoringPath() {
        // TODO: get from config
        //String pathStr = AppUtils.class.getClassLoader().getResource("").getPath();
        String pathStr = "C://entryXml/unprocessed";
        return System.getProperty("os.name").contains("indow") ? pathStr.substring(1) : pathStr;
    }

    private static String getEntryXmlPath(String filename) {
        return getMonitoringPath() + "/" + filename;
    }
}
