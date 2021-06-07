package com.siberteam.edu.dict;

public class UrlDictionaryAppEcxeption extends Exception {
    private final UrlDictionaryExitCode exitCode;

    public UrlDictionaryAppEcxeption(UrlDictionaryExitCode exitCode,
                                     String message) {
        super(message);
        this.exitCode = exitCode;
    }

    public UrlDictionaryAppEcxeption(UrlDictionaryExitCode exitCode) {
        super("Char Frequency Exception");
        this.exitCode = exitCode;
    }

    public UrlDictionaryExitCode getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "UrlDictionaryAppException{" +
                "exitCode=" + exitCode +
                ", message=" + getMessage() +
                '}';
    }
}
