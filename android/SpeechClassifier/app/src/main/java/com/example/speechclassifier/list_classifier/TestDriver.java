package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public class TestDriver implements ListClassifierDriver{

    private static final String TAG = "OfflineDriver";

    public TestDriver() {
    }

    public boolean isQuestion(String phrase) {
        return true;
    }

    public List<ListEntity> getListEntities(String phrase) {
        List<ListClassifierDriver.ListEntity> entities = new ArrayList<>();
        entities.add(new ListEntity("pizza", "symbol00064291"));
        entities.add(new ListEntity("pasta", "spaghetti2"));
        return entities;
    }

}
