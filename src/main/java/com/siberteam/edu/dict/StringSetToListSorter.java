package com.siberteam.edu.dict;

import java.util.*;

public class StringSetToListSorter {
    private final Set<String> setToSort;

    public StringSetToListSorter(Set<String> setToSort) {
        this.setToSort = setToSort;
    }

    public List<String> getSortedList() {
        List<String> sortedList = new ArrayList<>(setToSort);

        Collections.sort(sortedList);

        return sortedList;
    }
}
