package com.example.speechclassifier.list_classifier;


import android.content.Context;
import android.util.Log;

import com.example.speechclassifier.list_classifier.databaseimageparser.ImageParser;
import com.example.speechclassifier.list_classifier.entityparser.EntityPhraseParser;
import com.example.speechclassifier.list_classifier.questionparser.QuestionParser;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


/**
 * OfflineAPIHelper
 *
 * interface for offline list classifier model, classifys statements as list questions and parses
 * list questions for entities/image paths
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */
public class OfflineAPIHelper {

    private static final String TAG = "OfflineAPIHelper";

    private EntityPhraseParser entityParser;
    private ImageParser imageParser;
    private QuestionParser questionParser;

    /**
     * constructor for ListQuestionClassifier, initializes from resource files
     * @param context android context to load resource sfiles
     */
    public OfflineAPIHelper(Context context){
        this.entityParser = new EntityPhraseParser(context);
        this.imageParser = new ImageParser(context);
        this.questionParser = new QuestionParser(context);
    }


    /**
     * parse list question for entities and images for each entity
     * @param phrase statement to parse (excluding wakeword)
     * @return dictionary of {image_name: path}
     */
    public List<Dictionary<String, String>> getListImages(String phrase) {
        List<String> phrases = questionParser.phraseSplit(phrase);
        Log.d(TAG, phrases.get(0) + "--" + phrases.get(1));
        List<String> entities = entityParser.parse(phrases.get(1));
        List<Dictionary<String, String>> urls_all = new LinkedList<Dictionary<String, String>>();
        for(String entity: entities) {
            Dictionary<String, String> urls = new Hashtable<String, String>();
            String url = imageParser.getImage(entity);
            urls.put("entity", entity);
            urls.put("image_path", url);
            urls_all.add(urls);
        }
        return urls_all;
    }

    /**
     * determine if a statement is a list question (excluding wakeword)
     * @param phrase statement to test is list question
     * @return true if the question is a list question, false otherwise
     */
    public boolean isQuestion(String phrase) {
        return questionParser.questionClassifier(phrase);
    }
}
