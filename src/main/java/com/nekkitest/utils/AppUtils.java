package com.nekkitest.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alex on 28.12.2016.
 */
public class AppUtils {

    public static final String APP_PROCESSED = "app.monitoring.processed";
    public static final String APP_UNPROCESSED = "app.monitoring.unprocessed";
    public static final String APP_DELAY = "app.monitoring.delay";
    public static final String APP_PERIOD = "app.monitoring.period";

    private static Map<String, Boolean> processedIndex;

    static {
        processedIndex = new ConcurrentHashMap<>();
    }

    public static Set<String> getProcessedIndex() {
        return Collections.newSetFromMap(processedIndex);
    }

    public static void addProcessedIndex(Collection<String> index) {
        index.forEach(key -> processedIndex.put(key, true));
    }
}
