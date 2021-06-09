package com.siberteam.edu.dict;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int threadsEntity;

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

            if (outputFile.exists() && outputFile.isFile()) {
                throw new UrlDictionaryAppException(
                        UrlDictionaryExitCode.FILE_ALREADY_EXISTS,
                        outputFile.getName());
            }

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);

            ReadingUrlThreadsExecutionService threadsExecutor =
                    new ReadingUrlThreadsExecutionService(
                            getUrlQueue(inputStream), threadsEntity);

            if (!threadsExecutor.executeReading()) {
                throw new UrlDictionaryAppException(
                        UrlDictionaryExitCode.READING_NOT_FINISHED,
                        threadsExecutor.toString());
            }

            StringSetToListSorter setSorter = new StringSetToListSorter(
                    threadsExecutor.getUrlDictionary());

            for (String word : setSorter.getSortedList()) {
                outputStream.write(word.getBytes(StandardCharsets.UTF_8));
                outputStream.write('\n');
            }

        } catch (IOException | RuntimeException e) {
            handleException(UrlDictionaryExitCode.INPUT_OUTPUT, e);
        } catch (InterruptedException e) {
            handleException(UrlDictionaryExitCode.INTERRUPTED, e);
        } catch (UrlDictionaryAppException e) {
            handleException(e.getExitCode(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public static void handleException(UrlDictionaryExitCode exitCode,
                                       Exception e) {
        log(exitCode.getDescription() + "\n" +
                e.getMessage() + "\n" +
                e.getCause());
        System.exit(exitCode.getCode());
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
