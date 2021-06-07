package com.siberteam.edu.dict;

import java.io.*;

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

            if (outputFile.exists() && outputFile.isFile()) {
                throw new UrlDictionaryAppEcxeption(
                        UrlDictionaryExitCode.FILE_ALREADY_EXISTS,
                        outputFile.getName());
            }

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);

            Thread.sleep(3);
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
