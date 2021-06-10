package com.siberteam.edu.dict;

import java.io.*;
import java.util.*;

public class CollectionToOutputStreamWriter {
    OutputStream outputStream;
    List<String> sortedCollection;

    public CollectionToOutputStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeSortedCollectionToFile(final Collection<String> collection)
            throws IOException {
        sortedCollection = sortUrlDictionary(collection);
        try (BufferedWriter bw = new BufferedWriter(new
                OutputStreamWriter(outputStream))) {

            for (String word : sortedCollection) {
                bw.write(word + "\n");
            }
        }
    }

    private List<String> sortUrlDictionary(Collection<String> collection) {
        List<String> sortedCollection = new ArrayList<>(collection);
        Collections.sort(sortedCollection);

        return sortedCollection;
    }

    @Override
    public String toString() {
        return "CollectionToOutputStreamWriter{" +
                "outputStream=" + outputStream +
                ", sortedCollectionSize=" + sortedCollection.size() +
                '}';
    }
}
