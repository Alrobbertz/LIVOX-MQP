package com.example.speechclassifier.list_classifier;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class OfflineDriver implements ListClassifierDriver{

    private static final String TAG = "OfflineDriver";
    private OfflineAPIHelper offlineAPIHelper;

    public OfflineDriver(Context context) {
        offlineAPIHelper= new OfflineAPIHelper(context);
    }

    public boolean isQuestion(String phrase) {
        phrase = phrase.trim();
        Log.d(TAG, "SENDING LOCAL REQUEST: listclassifier");
        Log.d(TAG, "Phrase: " + phrase);
        Log.d(TAG, "IsQuestion: " + Boolean.toString(offlineAPIHelper.isQuestion(phrase)));
        return offlineAPIHelper.isQuestion(phrase);
    }

    public List<ListEntity> getListEntities(String phrase) {
        List<Dictionary<String, String>> image_pairs = offlineAPIHelper.getListImages(phrase);
        Log.d(TAG, image_pairs.toString());
        List<ListEntity> entities = new ArrayList<>();
        for(Dictionary<String, String> pair: image_pairs) {
            String entity = pair.get("entity");
            String image = pair.get("image_path");
            Log.d(TAG, image);
            ListEntity currentEntity = new ListEntity(entity, image);
            entities.add(currentEntity);
        }
        return entities;
    }

}
