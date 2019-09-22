package com.example.speechclassifier.speechrecognition;

import java.util.ArrayList;

/**
 * Created by Jason on 11/28/17.
 */

public interface OnResultsReady {
    void onSpeechResults(ArrayList<String> results);
    void onSpeechPartialResult(String partialResult);
}
