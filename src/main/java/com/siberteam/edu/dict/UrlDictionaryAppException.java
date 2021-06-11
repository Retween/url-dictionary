package com.siberteam.edu.dict;

public class UrlDictionaryAppException extends Exception {
    private final UrlDictionaryExitCode exitCode;

    public UrlDictionaryAppException(UrlDictionaryExitCode exitCode,
                                     String message) {
        super(message);
        this.exitCode = exitCode;
    }

    public UrlDictionaryAppException(UrlDictionaryExitCode exitCode) {
        super("Char Frequency Exception");
        this.exitCode = exitCode;
    }

    public UrlDictionaryExitCode getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "UrlDictionaryAppException" + "[" +
                "exitCode=" + exitCode +
                ']';
    }
}
