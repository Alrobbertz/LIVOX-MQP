package com.example.speechclassifier.list_classifier;

import android.util.Log;

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

    public UtteranceDetectorOnline(ListClassificationOrchestrator orchestrator) {
        super(orchestrator);
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
        List<String> response = apiResolver(phrase.toString());
        tokens = new ArrayList<Phrase>(){{
           for(String s: response)
               add(new Phrase(s));
        }};
        recentPhrase = phrase;
        return true;
    }

    public List<String> apiResolver(String utterance){
        Log.d(TAG, "resolver.start");
        String[] response = new String[1];
        String keywordURL = utterance.replace(" ", "%20");
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL("http://api.axonbeats.com/offline_entities?phrase=" + keywordURL + "&ngram=2");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    con.setRequestProperty("Accept","*/*");
                    con.setInstanceFollowRedirects(true);

                    Log.d(TAG, "URL: " + con.toString());
                    response[0] = new Scanner(con.getInputStream(), "UTF-8").useDelimiter("\\A").next();
                    Log.d(TAG, response[0]);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }

        });

        t.start();
        try {
            t.join();
        } catch (Exception e) {}

        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\"[^,]*\"").matcher(response[0]);
        while (m.find()) {
            String currentMatch = m.group();
            currentMatch = currentMatch.substring(1, currentMatch.length() - 1);
            allMatches.add(currentMatch);
            Log.d(TAG, currentMatch);
        }

        return allMatches;
    }

}
