package com.example.speechclassifier.list_classifier.questionparser;

import android.content.Context;
import android.util.Log;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * QuestionParser
 *
 * determines if a text statement is a list question, and splits a list question into two parts
 * the invocation phrase and the entity phrase
 *
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */

public class QuestionParser {

    private static final String TAG = "QuestionParser";


    private static LinkedHashMap<String, LinkedHashMap<String, Integer>> patternDict;

    /**
     * constructor for QuestionParser
     * @param context android context to load in vocabulary resources
     */
    public QuestionParser(Context context) {
        patternDict = QuestionWordGenerator.initializeVocabulary(context);
    }

    /**
     * split a phrase into the invocation phrase and the entity phrase,
     * the invocation phrase is the question portion while the entity phrase contains the list of
     * entities
     * @param phrase a list question phrase
     * @return list containing [invocationPhrase, entityPhrase]
     */
    public List<String> phraseSplit(String phrase) {
        String[] words = phrase.split("\\s");
        LinkedHashMap<String, Integer> offset = patternDict.get(words[0]);
        Log.d(TAG, offset.toString());
        Log.d(TAG, words.toString());
        int numOffset = 0;

        Iterator<String> offset_iter = offset.keySet().iterator();
        boolean matchFound = false;
        while(offset_iter.hasNext() && !matchFound) {
            String next_key = offset_iter.next();
            for(int i=0; i < words.length;i++) {
                if(next_key.equals(words[i])) {
                    numOffset = offset.get(words[i]) + i + 1;
                    matchFound = true;
                    break;
                }
            }
        }
        String invocationPhrase = "";
        String listPhrase = "";
        for(int i=0;i < words.length;i++) {
            if(i < numOffset) {
                invocationPhrase += words[i] + " ";
            }
            else {
                listPhrase += words[i] + " ";
            }
        }
        invocationPhrase = invocationPhrase.substring(0, invocationPhrase.length() - 1);
        listPhrase = listPhrase.substring(0, listPhrase.length() - 1);
        List<String> splitPhrases = new LinkedList<>();
        splitPhrases.add(invocationPhrase);
        splitPhrases.add(listPhrase);
        return splitPhrases;
    }

    /**
     * classify if a question is a list question
     * @param phrase any text, to classify if it is a list question
     * @return boolean if question is a list question or not
     */
    public boolean questionClassifier(String phrase) {
        String[] words = phrase.split("\\s");
        for(int i=0;i < words.length;i++) {
            if(words[i].equals("or")) {
                Iterator<String> keys = patternDict.keySet().iterator();
                for(int j=0; j < words.length;j++) {
                    while(keys.hasNext()) {
                        String key = keys.next();
                        if(key.equals(words[j])) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
