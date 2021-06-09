package com.siberteam.edu.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
        System.out.println("CREATED");//////////////////////
        this.urlDictionary = urlDictionary;
        this.urlFiles = urlFiles;
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started");///////////////
        while (!urlFiles.isEmpty()) {
            String urlFilePath = urlFiles.poll();
            URL url = null;

            try {
                if (urlFilePath != null) {
                    url = new URL(urlFilePath);

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(url.openStream()));

                    String inputString;
                    while ((inputString = br.readLine()) != null) {
                        parseString(inputString);
                    }
                }
            } catch (IOException e) {
                Main.log("Invalid URL: " + url);
            } finally {
                latch.countDown();
            }
        }

//        } finally {
//            latch.countDown();
//        }
    }

    public void parseString(String inputString) {
        inputString = inputString.replaceAll("[^А-Яа-я]", " ")
                .replaceAll(" +", " ").trim();

        if (!inputString.isEmpty()) {
            for (String word : inputString.toLowerCase()
                    .split(" ")) {
                if (word.length() > 2) {
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
