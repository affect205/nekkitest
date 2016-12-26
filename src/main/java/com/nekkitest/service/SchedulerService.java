package com.nekkitest.service;

import com.nekkitest.dao.EntryXmlDao;
import com.nekkitest.entity.EntryXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Alex on 26.12.2016.
 */
@Service
public class SchedulerService {

    @Autowired
    private EntryXmlDao entryDao;

    @PostConstruct
    public void onInit() {
        System.out.println("SchedulerService::onInit");
        List<EntryXml> list = entryDao.getAll();
        list.forEach(System.out::println);
    }

    public void sayHello() {
        System.out.println("I say hello!");
    }
}
