package com.siberteam.edu.dict;

import com.google.common.collect.Sets;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ReadingUrlThreadsStarter {
    private final Set<String> wordSet;
    private final Queue<String> process;
    private final ExecutorService executor;
    private final CountDownLatch readersLatch;
    private final int threadsCount;
    private final int urlFilesCount;

    public ReadingUrlThreadsStarter(Queue<String> process, int threadsCount) {
        this.process = process;
        this.threadsCount = threadsCount;
        urlFilesCount = process.size();
        wordSet = Sets.newConcurrentHashSet();
        executor = Executors.newFixedThreadPool(threadsCount);
        readersLatch = new CountDownLatch(
                Math.min(threadsCount, urlFilesCount));
    }

    public void startThreads() throws IOException, InterruptedException {
        for (int i = 0; i < Math.min(threadsCount, urlFilesCount); i++) {
            executor.execute(new UrlToSetReader(wordSet, process,
                    readersLatch));
        }

        readersLatch.await();

        executor.shutdown();
    }

    public Set<String> getWordSet() {
        return wordSet;
    }

    @Override
    public String toString() {
        return "ReadingUrlThreadsStarter" + "[" +
                "wordSetSize=" + wordSet.size() +
                ", processSize=" + process.size() +
                ", executor=" + executor +
                ", readersLatch=" + readersLatch +
                ", threadsCount=" + threadsCount +
                ", urlFilesCount=" + urlFilesCount +
                ']';
    }
}
