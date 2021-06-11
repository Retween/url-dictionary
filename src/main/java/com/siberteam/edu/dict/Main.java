package com.siberteam.edu.dict;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int threadsEntity;

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addRequiredOption("i", "inputFile", true,
                "File with URL addresses list");
        options.addRequiredOption("o", "outputFile", true,
                "Output file for recording the final dictionary");
        options.addRequiredOption("t", "threadsNumber", true,
                "number of threads to work with URL addresses");

        try {
            CommandLine cmd = parser.parse(options, args);

            File inputFile = new File(cmd.getOptionValue("i"));
            File outputFile = new File(cmd.getOptionValue("o"));
            threadsEntity = Integer.parseInt(cmd.getOptionValue("t"));

            if (threadsEntity < 1) {
                throw new UrlDictionaryAppException(
                        UrlDictionaryExitCode.COMMAND_LINE_USAGE,
                        inputFile.getName());
            }

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
            outputStream = new FileOutputStream(outputFile);

            InputStreamToQueueReader reader =
                    new InputStreamToQueueReader(inputStream);
            Queue<String> urlFiles = reader.getUrlQueue();

            ReadingUrlThreadsStarter threadsExecutor =
                    new ReadingUrlThreadsStarter(urlFiles, threadsEntity);
            threadsExecutor.startThreads();

            CollectionToOutputStreamWriter writer =
                    new CollectionToOutputStreamWriter(outputStream);
            writer.writeSortedCollectionToFile(
                    threadsExecutor.getWordSet());

        } catch (IOException | RuntimeException e) {
            handleException(UrlDictionaryExitCode.INPUT_OUTPUT, e);
        } catch (InterruptedException e) {
            handleException(UrlDictionaryExitCode.INTERRUPTED, e);
        } catch (UrlDictionaryAppException e) {
            handleException(e.getExitCode(), e);
        } catch (ParseException e) {
            handleException(UrlDictionaryExitCode.COMMAND_LINE_USAGE, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
