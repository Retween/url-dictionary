package com.siberteam.edu.dict;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ReadingUrlThreadsExecutor {
    private final Set<String> urlDictionary;
    //    private final ArrayList<String> urlFilesList;
    private final Queue<String> urlFilesQueue;
    private final int threadsEntity;
    private final ConcurrentSkipListSet<String> dict;

    public ReadingUrlThreadsExecutor(Queue<String> urlFilesQueue, int threadsEntity) {
        this.urlFilesQueue = urlFilesQueue;
        this.threadsEntity = threadsEntity;
        urlDictionary = Collections.synchronizedSet(new HashSet<>());
        dict = new ConcurrentSkipListSet<>();
    }

//    public ReadingUrlThreadsExecutor(ArrayList<String> urlFilesList, int threadsEntity) {
//        this.urlFilesList = urlFilesList;
//        this.threadsEntity = threadsEntity;
//        urlDictionary = Collections.synchronizedSet(new HashSet<>());
//    }


    public void executeReading() throws IOException, InterruptedException {
//        int partitionSize =urlList.size()/threadsEntity;
//        System.out.println(partitionSize);
//        List<List<String>>partitions = new LinkedList<>();
//        for (int i = 0; i < urlList.size(); i+=partitionSize) {
//
//        }
        long s = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(threadsEntity);

//        executor.execute(new UrlToSetReader(urlDictionary, urlFilesList));

        ArrayList<UrlToSetReader> threads = new ArrayList<>();
        for (int i = 0; i < threadsEntity; i++) {
            threads.add(new UrlToSetReader(urlDictionary));
        }

        while (!urlFilesQueue.isEmpty()) {
            for (UrlToSetReader thread : threads) {
//                if (thread.isFreeToRead()) {
                    thread.setUrlFile(urlFilesQueue.poll());
                    executor.execute(thread);
//                }
            }
        }

//        while (!urlFilesQueue.isEmpty()) {
//            executor.execute(new UrlToSetReader(urlDictionary, urlFilesQueue.poll()));
//        }
        executor.shutdown();

        while (!executor.isTerminated()) {
            TimeUnit.MILLISECONDS.sleep(1);
        }

        long f = System.currentTimeMillis();

        System.out.println(dict);
        System.out.println(dict.size());
        System.out.println(urlDictionary);
        System.out.println(urlDictionary.size());

        System.out.println("Time: " + (f - s));

        System.out.println("finished all threads");
    }

//    public Set<String> getUrlDictionary() {
//        return urlDictionary;
//    }
}
