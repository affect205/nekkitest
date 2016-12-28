package com.nekkitest.service;

import com.nekkitest.dao.EntryXmlDao;
import com.nekkitest.entity.EntryXml;
import com.nekkitest.utils.AppUtils;
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

import static com.nekkitest.utils.AppUtils.*;

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
        System.out.println("Unprocessed files:");
        try {
            // список необработанных файлов
            Set<String> processedSet = AppUtils.getProcessedIndex();
            Set<String> unprocessedSet = Files.list(Paths.get(env.getProperty(APP_UNPROCESSED)))
                    .filter(Files::isRegularFile)
                    .map(String::valueOf)
                    .filter(p -> !processedSet.contains(String.valueOf(p)))
                    .collect(Collectors.toSet());
            unprocessedSet.forEach(System.out::println);
            System.out.println("---------------------------------");

            // парсинг и сохранение файлов
            unprocessedSet.forEach(srcXml -> {
                EntryXml entryXml = XmlUtils.parseEntryXml(srcXml);
                entryDao.create(entryXml);
            });

            // список обработанных файлов
            addProcessedIndex(unprocessedSet);

            // перемещаем в директорию с обработанными файлами
            unprocessedSet.forEach(srcXml -> {
                try {
                    Path srcPath = Paths.get(srcXml);
                    Path destPath = Paths.get(env.getProperty(APP_PROCESSED) + "/" + String.valueOf(srcPath.getFileName()));
                    Files.move(srcPath, destPath);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
