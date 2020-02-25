package com.example.speechclassifier.list_classifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.speechclassifier.WebAPIHelper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

public class UtteranceDetectorOnline extends UtteranceDetector {

    public String TAG = "SpeechRecognizerManager";
    List<Phrase> tokens;
    Phrase recentPhrase;

    public UtteranceDetectorOnline() {
        tokens = null;
    }

    public int getStartIndex(){
        return recentPhrase.phraseIndex(tokens.get(0));
    }

    public int getEndIndex(){
        return recentPhrase.phraseIndex(tokens.get(tokens.size() - 1));
    }

    public List<String> getUtteranceList(){
        return new ArrayList<String>(){{
            for(Phrase p: tokens)
                add(p.toString());
        }};
    }

    public void clear() {
        tokens = null;
        recentPhrase = null;
    }

    public boolean classify(Phrase phrase) {
        List<String> response = WebAPIHelper.getListEntities(phrase.toString());
        if(response.size() == 0){// if size of 0 then it automatically fails bc it didn't parse any list entities
            return false;
        }

        tokens = new ArrayList<Phrase>(){{
           for(String s: response)
               add(new Phrase(s));
        }};
        recentPhrase = phrase;
        return true;
    }

}
