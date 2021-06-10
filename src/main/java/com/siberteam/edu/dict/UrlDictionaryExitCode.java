package com.siberteam.edu.dict;

public enum UrlDictionaryExitCode {
    COMMAND_LINE_USAGE(64, "usage: url-dictionary\n" +
            " -i,--inputFile <arg>       File with URL addresses list\n" +
            " -o,--outputFile <arg>      Output file for recording the " +
            "final dictionary\n" +
            " -t,--threadsNumber <arg>   number of threads to work with URL " +
            "addresses\n" +
            "Example: -i inputFile.txt -o outputFile.txt -t 3"),
    CANNOT_OPEN_INPUT(66, "File not found"),
    INPUT_OUTPUT(74, "Input/Output exception was caught"),
    FILE_ALREADY_EXISTS(74, "File already exists"),
    INTERRUPTED(1, "Interrupted exception was caught");

    private final int code;
    private final String description;

    UrlDictionaryExitCode(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UrlDictionaryExitCode{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
