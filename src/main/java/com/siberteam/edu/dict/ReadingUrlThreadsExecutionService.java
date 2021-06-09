package com.siberteam.edu.dict;

import com.google.common.collect.Sets;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ReadingUrlThreadsExecutionService {
    private final Set<String> urlDictionary;
    private final Queue<String> urlFilesQueue;
    private final int threadsEntity;
    private boolean readingCompleted;

    public ReadingUrlThreadsExecutionService(Queue<String> urlFilesQueue,
                                             int threadsEntity) {
        this.urlFilesQueue = urlFilesQueue;
        this.threadsEntity = threadsEntity;
        urlDictionary = Sets.newConcurrentHashSet();
    }

    public boolean executeReading() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadsEntity);
        int urlFilesEntity = urlFilesQueue.size();

        for (int i = 0; i < threadsEntity && i < urlFilesEntity; i++) {
            executor.execute(new UrlToSetReader(urlDictionary, urlFilesQueue));
        }

        executor.shutdown();

        readingCompleted = executor.awaitTermination(1, TimeUnit.MINUTES);

        return readingCompleted;
    }

    public Set<String> getUrlDictionary() {
        return urlDictionary;
    }

    @Override
    public String toString() {
        return "ReadingUrlThreadsExecutionService{" +
                "urlDictionarySize=" + urlDictionary.size() +
                ", urlFilesQueue=" + urlFilesQueue +
                ", threadsEntity=" + threadsEntity +
                ", readingCompleted=" + readingCompleted +
                '}';
    }
}
