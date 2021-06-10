package com.siberteam.edu.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class UrlToSetReader implements Runnable {
    private final Set<String> urlDictionary;
    private final Queue<String> urlFiles;
    private final CountDownLatch latch;

    public UrlToSetReader(Set<String> urlDictionary, Queue<String> urlFiles,
                          CountDownLatch latch) {
        this.urlDictionary = urlDictionary;
        this.urlFiles = urlFiles;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (!urlFiles.isEmpty()) {
            String urlFilePath = urlFiles.poll();
            URL url = null;
            BufferedReader br = null;

            try {
                if (urlFilePath != null) {
                    url = new URL(urlFilePath);
                    br = new BufferedReader(new InputStreamReader(
                            url.openStream()));

                    String inputString;
                    while ((inputString = br.readLine()) != null) {
                        parseString(inputString);
                    }
                }
            } catch (IOException e) {
                Main.log("Invalid URL: " + url);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }
        }
    }

    private void parseString(String inputString) {
        if (!inputString.isEmpty()) {
            for (String word : inputString.toLowerCase()
                    .split("[\\p{Punct}\\s«»+]")) {
//                    .split("[\\s+]")) {
                if (word.matches("[А-Яа-яЁё]{3,}")) {
                    urlDictionary.add(word);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UrlToSetReader{" +
                "urlDictionarySize=" + urlDictionary.size() +
                ", urlFilesSize=" + urlFiles.size() +
                '}';
    }
}
