package com.siberteam.edu.dict;

import java.io.*;
import java.util.*;

public class CollectionToOutputStreamWriter {
    private final OutputStream outputStream;

    public CollectionToOutputStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeSortedCollectionToFile(final Collection<String> collection)
            throws IOException {
        List<String> collectionDictionary = new ArrayList<>(collection);

        Collections.sort(collectionDictionary);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                outputStream))) {

            for (String word : collectionDictionary) {
                bw.write(word + "\n");
            }
        }
    }

    @Override
    public String toString() {
        return "CollectionToOutputStreamWriter[" +
                "outputStream=" + outputStream +
                ']';
    }
}
