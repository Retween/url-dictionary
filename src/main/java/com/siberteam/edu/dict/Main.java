package com.siberteam.edu.dict;

import java.io.*;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream = null;
        int threadsEntity;
        long s = System.currentTimeMillis();//////////////////
        try {
            if (args.length != 3
                    || (threadsEntity = Integer.parseInt(args[2])) < 1) {
                throw new UrlDictionaryAppException(
                        UrlDictionaryExitCode.COMMAND_LINE_USAGE);
            }

            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            if (!inputFile.exists()) {
                throw new UrlDictionaryAppException(
                        UrlDictionaryExitCode.CANNOT_OPEN_INPUT,
                        inputFile.getName());
            }

//            if (outputFile.exists() && outputFile.isFile()) {
//                throw new UrlDictionaryAppException(
//                        UrlDictionaryExitCode.FILE_ALREADY_EXISTS,
//                        outputFile.getName());
//            }

            inputStream = new FileInputStream(inputFile);

            ReadingUrlThreadsExecutionService threadsExecutor =
                    new ReadingUrlThreadsExecutionService(
                            getUrlQueue(inputStream), threadsEntity);

            threadsExecutor.executeReading();

            writeDictionaryToFile(outputFile,
                    threadsExecutor.gerSortedUrlDictionary());

        } catch (IOException | RuntimeException e) {
            handleException(UrlDictionaryExitCode.INPUT_OUTPUT, e);
        } catch (InterruptedException e) {
            handleException(UrlDictionaryExitCode.INTERRUPTED, e);
        } catch (UrlDictionaryAppException e) {
            handleException(e.getExitCode(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long f = System.currentTimeMillis();//////////////////
        System.out.println(f - s);

    }

    public static Queue<String> getUrlQueue(
            InputStream inputStream) throws IOException {
        ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream))) {

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                urlQueue.add(inputLine);
            }
        }

        return urlQueue;
    }

    public static void writeDictionaryToFile(
            File outputFile, Collection<String> collection) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(outputFile))) {

            for (String word : collection) {
                bw.write(word + "\n");
            }
        }
    }

    public static void handleException(UrlDictionaryExitCode exitCode,
                                       Exception e) {
        log(exitCode.getDescription() + "\n" + e.getMessage() + "\n" +
                e.getCause());
        System.exit(exitCode.getCode());
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
