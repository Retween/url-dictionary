package com.siberteam.edu.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Queue;
import java.util.Set;

public class UrlToSetReader implements Runnable {
    private final Set<String> urlDictionary;
    private final Queue<String> urlFiles;

    public UrlToSetReader(Set<String> urlDictionary, Queue<String> urlFiles) {
        this.urlDictionary = urlDictionary;
        this.urlFiles = urlFiles;
    }

    @Override
    public void run() {
        while (!urlFiles.isEmpty()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new URL(urlFiles.poll()).openStream()))) {

                String inputString;
                while ((inputString = br.readLine()) != null) {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
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
