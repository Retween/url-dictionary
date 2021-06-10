package com.siberteam.edu.dict;

import com.google.common.collect.Sets;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ReadingUrlThreadsExecutor {
    private final Set<String> urlDictionary;
    private final Queue<String> urlFilesQueue;
    private final ExecutorService executor;
    private final CountDownLatch latch;
    private final int threadsEntity;
    private final int urlFilesEntity;

    public ReadingUrlThreadsExecutor(Queue<String> urlFilesQueue,
                                     int threadsEntity) {
        this.urlFilesQueue = urlFilesQueue;
        this.threadsEntity = threadsEntity;
        urlFilesEntity = urlFilesQueue.size();
        urlDictionary = Sets.newConcurrentHashSet();
        executor = Executors.newFixedThreadPool(threadsEntity);
        latch = new CountDownLatch(urlFilesEntity);
    }

    public void executeReading() throws IOException, InterruptedException {
        for (int i = 0; i < threadsEntity && i < urlFilesEntity; i++) {
            executor.execute(new UrlToSetReader(urlDictionary, urlFilesQueue,
                    latch));
        }

        executor.shutdown();

        latch.await();
    }


    public Set<String> getUrlDictionary() {
        return urlDictionary;
    }

    @Override
    public String toString() {
        return "ReadingUrlThreadsExecutionService{" +
                "urlDictionarySize=" + urlDictionary.size() +
                ", urlFilesQueueSize=" + urlFilesQueue.size() +
                ", threadsEntity=" + threadsEntity +
                '}';
    }
}
