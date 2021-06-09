package com.siberteam.edu.dict;

import com.google.common.collect.Sets;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ReadingUrlThreadsExecutionService {
    private final Set<String> urlDictionary;
    private final Queue<String> urlFilesQueue;
    private final int threadsEntity;
    private final ExecutorService executor;
    private final CountDownLatch latch;

    public ReadingUrlThreadsExecutionService(Queue<String> urlFilesQueue,
                                             int threadsEntity) {
        this.urlFilesQueue = urlFilesQueue;
        this.threadsEntity = threadsEntity;
        urlDictionary = Sets.newConcurrentHashSet();
        executor = Executors.newFixedThreadPool(threadsEntity);
        latch = new CountDownLatch(urlFilesQueue.size());
    }

    public void executeReading() throws IOException, InterruptedException {
        int urlFilesEntity = urlFilesQueue.size();

        for (int i = 0; i < threadsEntity && i < urlFilesEntity; i++) {
            executor.execute(new UrlToSetReader(urlDictionary, urlFilesQueue,
                    latch));
        }

        executor.shutdown();

        latch.await();
    }

    public List<String> gerSortedUrlDictionary() {
        List<String> sortedList = new ArrayList<>(urlDictionary);
        Collections.sort(sortedList);
        return sortedList;
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
