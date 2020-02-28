package com.example.speechclassifier.list_classifier;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineDriver implements ListClassifierDriver{

    //TODO make sure that all input streams are closed

    private static final String TAG = "OnlineDriver";

    public OnlineDriver(){}

    /**
     * Interface to the /listclassifier api on api.axonbeats.com
     *
     * @param phrase the question to be classified
     * @return true if the given question is a list question, otherwise false
     */
    public boolean isQuestion(String phrase){
        Log.d(TAG, "SENDING API REQUEST: listclassifier");
        Log.d(TAG, "Phrase: " + phrase);

        HashMap<String, String> params = new HashMap<>();
        params.put("phrase", phrase);
        InputStream response = WebAPIHelper.get("http://api.axonbeats.com/listclassifier", params);

        if(response == null){//exception thrown during web request
            //TODO handle this better
            System.out.println("Response was Null");
            return false;
        }
        String decodedResponse = WebAPIHelper.responseToString(response).trim();
        boolean type =  Boolean.valueOf(decodedResponse);
        //TODO ask andrew about setting web api response to true always
        return type;
    }


    /**
     * Interface to the /question_img_parser api on api.axonbeats.com
     * Returns a JSONArray of JSONObjects, each of which contains an entity String and a url String
     * The entity String is the representation of one answer entity extracted from the question
     * The url String is the name of the related Livox image
     * Returns null in case of errors
     *
     * @param phrase the question to be classified
     * @return a List of ListEntity containing list entities and associated images
     */
    public List<ListEntity> getListEntities(String phrase){
        Log.d(TAG, "SENDING API REQUEST: question_img_parser");
        Log.d(TAG, "Phrase: " + phrase);

        HashMap<String, String> params = new HashMap<>();
        params.put("phrase", phrase);
        params.put("ngram", "3");
        params.put("local", "true");
        InputStream response = WebAPIHelper.get("http://api.axonbeats.com/question_img_parser", params);

        if(response == null){//exception thrown during web request
            return null;
        }
        String decodedResponse = WebAPIHelper.responseToString(response);

        try{
            JSONArray jsonEntityArray = new JSONArray(decodedResponse);
            List<ListEntity> listEntities = new ArrayList<>();
            for(int i = 0; i < jsonEntityArray.length(); i++){
                JSONObject currentObject = jsonEntityArray.getJSONObject(i);
                String entityText = currentObject.getString("entity");
                String entityImage = currentObject.getString("url");
                Log.d(TAG, entityImage);
                ListEntity currentEntity = new ListEntity(entityText, entityImage);
                listEntities.add(currentEntity);
            }
            return listEntities;
        }catch(JSONException e){
            Log.d(TAG, e.toString());
            return null;
        }
    }


}