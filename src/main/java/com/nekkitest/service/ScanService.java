package com.nekkitest.service;

import com.nekkitest.dao.EntryXmlDao;
import com.nekkitest.entity.EntryXml;
import com.nekkitest.utils.TestUtils;
import com.nekkitest.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nekkitest.config.AppConfig.APP_PROCESSED;
import static com.nekkitest.config.AppConfig.APP_UNPROCESSED;

/**
 * Created by Alex on 26.12.2016.
 */
@Service
public class ScanService {
    @Autowired
    private Environment env;

    @Autowired
    private EntryXmlDao entryDao;

    @PostConstruct
    public void onInit() {
        System.out.println("ScanService::onInit");
    }

    public void doScan() {
        System.out.println("ScanService::doScan");
        try {
            // список необработанных файлов
            System.out.println("[Step1]: Scan unprocessed files...");
            //Set<String> processedSet = AppUtils.getProcessedIndex();
            Set<String> unprocessedSet = Files.list(Paths.get(env.getProperty(APP_UNPROCESSED)))
                    .filter(Files::isRegularFile)
                    .map(String::valueOf)
                    //.filter(p -> !processedSet.contains(String.valueOf(p)))
                    .filter(p -> String.valueOf(p).toLowerCase().contains(".xml"))
                    .collect(Collectors.toSet());
            unprocessedSet.forEach(System.out::println);

            if (unprocessedSet.size() == 0) {
                TestUtils.printStatistic();
                System.exit(-1);
            }

            // парсинг и сохранение файлов
            System.out.println("[Step2]: parse xml...");
            unprocessedSet.forEach(srcXml -> {
                EntryXml entryXml = XmlUtils.parseEntryXml(srcXml);
                entryDao.create(entryXml);
            });

            // перемещаем в директорию с обработанными файлами
            System.out.println("[Step3]: move xml to processed...");
            unprocessedSet.forEach(srcXml -> {
                try {
                    System.out.println(srcXml);
                    Path srcPath = Paths.get(srcXml);
                    Path destPath = Paths.get(env.getProperty(APP_PROCESSED) + "/" + String.valueOf(srcPath.getFileName()));
                    Files.move(srcPath, destPath);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("----------------------------------------------------------------");
            TestUtils.processedCount += unprocessedSet.size();
            TestUtils.printStatistic();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
