package com.example.speechclassifier.list_classifier;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.speechclassifier.WebAPIHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ListClassificationOrchestrator_2 {

    WakewordDetector wwdetector;

    public Phrase recentPhrase;
    public String questionPhrase;
    public HashMap<String, String> listEntities;

    public String TAG = "SpeechRecognizerManager";

    public ListClassificationOrchestrator_2() {
        wwdetector = new WakewordDetectorImpl("John");
        listEntities = new HashMap<>();
    }

    public boolean classify(Phrase phrase) {
        if(! wwdetector.classify(phrase))
            return false;
        Phrase wwPhrase = phrase.subPhrase(wwdetector.getWWIndex() + 1);

        JSONArray parsedQuestion = WebAPIHelper.getParsedQuestion(wwPhrase.toString());

        if(!extractListEntities(parsedQuestion))
            return false;

        //if successfully parsed, store this as the most recent phrase
        recentPhrase = phrase;
        return true;
    }

    public boolean extractListEntities(JSONArray parsedQuestion){
        for(int i = 0; i < parsedQuestion.length(); i++)
            try{
                JSONObject object = parsedQuestion.getJSONObject(i);
                String listEntity = object.getString("entity");
                String listURL = object.getString("url");
                listEntities.put(listEntity, listURL);
            }catch(Exception e){
                return false;
            }
        return true;
    }

    public void clear(){
        recentPhrase = null;
        questionPhrase = null;
        listEntities.clear();
    }

    public String getPhrase(){
        return recentPhrase.toString();
    }

    public String getQuestionPhrase() {
        //TODO fix
        return questionPhrase.toString();
    }

    public Set<String> getListEntities(){
        return listEntities.keySet();
    }

    public Bitmap getImage(String listEntity){
        return WebAPIHelper.getImage(listEntities.get(listEntity));
    }

    private String recentPhraseSubstring(int startIndex, int endIndex) {
        return recentPhrase.getSubphrase(startIndex, endIndex);
    }
}
