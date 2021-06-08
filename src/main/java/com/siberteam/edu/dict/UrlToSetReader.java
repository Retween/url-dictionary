package com.siberteam.edu.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UrlToSetReader implements Runnable {
    private final Set<String> set;
    String urlFile;
    boolean freeToRead = true;


    public UrlToSetReader(Set<String> set) {
        System.out.println(Thread.currentThread().getName() + " CREATED");
        this.set = set;
    }

    public UrlToSetReader(Set<String> set, String urlFile) {
        System.out.println(Thread.currentThread().getName() + " CREATED");

        this.set = set;
        this.urlFile = urlFile;
    }

    @Override
    public void run() {
        freeToRead = false;
        try {
            System.out.println(Thread.currentThread().getName() + " started" +
                    " work with " + urlFile);
            URL url = new URL(urlFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputString;
            while ((inputString = br.readLine()) != null) {
                inputString =
                        inputString.replaceAll("[^А-Яа-я]", " ")
                                .replaceAll(" +", " ").trim();
                if (!inputString.isEmpty() && !inputString.equals(" ")) {
//                    System.out.println(inputString);
                    set.addAll(Arrays.asList(inputString.split(" ")));
                }
            }
//            System.out.println(set);
            System.out.println(Thread.currentThread().getName() + " finished");
            freeToRead = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUrlFile(String urlFile) {
        if (freeToRead) {
            this.urlFile = urlFile;
        }
    }

    public boolean isFreeToRead() {
        return freeToRead;
    }

    //    public Set<String> getSet() {
//        return set;
//    }
}
