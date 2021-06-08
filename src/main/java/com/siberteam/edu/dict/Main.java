package com.siberteam.edu.dict;

import java.io.*;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {


    public static void main(String[] args) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int threadsEntity;
        try {
            if (args.length != 3
                    || (threadsEntity = Integer.parseInt(args[2])) < 1) {
                throw new UrlDictionaryAppEcxeption(
                        UrlDictionaryExitCode.COMMAND_LINE_USAGE);
            }

            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            if (!inputFile.exists()) {
                throw new UrlDictionaryAppEcxeption(
                        UrlDictionaryExitCode.CANNOT_OPEN_INPUT,
                        inputFile.getName());
            }

//            if (outputFile.exists() && outputFile.isFile()) {
//                throw new UrlDictionaryAppEcxeption(
//                        UrlDictionaryExitCode.FILE_ALREADY_EXISTS,
//                        outputFile.getName());
//            }

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
//            System.out.println(getUrlList(inputStream).size());
            ReadingUrlThreadsExecutor executor = new ReadingUrlThreadsExecutor(
                    getUrlQueue(inputStream), 2);
            executor.executeReading();

//            System.out.println(executor.getUrlDictionary());

            Thread.sleep(3);//////////
        } catch (IOException | RuntimeException e) {
            handleException(UrlDictionaryExitCode.INPUT_OUTPUT, e);
        } catch (InterruptedException e) {
            handleException(UrlDictionaryExitCode.INTERRUPTED, e);
        } catch (UrlDictionaryAppEcxeption e) {
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

    public static Queue<String> getUrlQueue(InputStream inputStream) throws IOException {
        Queue<String> urlQueue = new PriorityQueue<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            urlQueue.add(inputLine);
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
