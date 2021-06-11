package com.siberteam.edu.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class UrlToSetReader implements Runnable {
    private final Set<String> wordSet;
    private final Queue<String> process;
    private final CountDownLatch readersLatch;

    public UrlToSetReader(Set<String> wordSet, Queue<String> process,
                          CountDownLatch readersLatch) {
        this.wordSet = wordSet;
        this.process = process;
        this.readersLatch = readersLatch;
    }

    @Override
    public void run() {
        String urlFilePath = process.poll();
        if (urlFilePath == null) {
            readersLatch.countDown();
        }

        try {
            while (urlFilePath != null) {
                URL url = new URL(urlFilePath);
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(url.openStream()))) {

                    String inputString;
                    while ((inputString = br.readLine()) != null) {
                        parseString(inputString);
                    }
                }

                urlFilePath = process.poll();
            }

        } catch (IOException e) {
            Main.log("Invalid URL " + urlFilePath);
        } finally {
            readersLatch.countDown();
        }
    }

    private void parseString(String inputString) {
        if (!inputString.isEmpty()) {
            for (String word : inputString.toLowerCase()
                    .split("[\\p{Punct}\\s«»+]")) {
                if (word.matches("[А-Яа-яЁё]{3,}")) {
                    wordSet.add(word);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UrlToSetReader" + "[" +
                "wordSet=" + wordSet +
                ", process=" + process +
                ", readersLatch=" + readersLatch +
                ']';
    }
}
