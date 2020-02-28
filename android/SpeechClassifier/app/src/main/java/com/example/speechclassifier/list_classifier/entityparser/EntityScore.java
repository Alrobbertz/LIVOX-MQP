package com.example.speechclassifier.list_classifier.entityparser;

import android.content.Context;
import android.util.Log;
import java.util.Dictionary;

/**
 * EntityScore
 *
 * Handles scoring of ngram entities using generated/loaded dictionary vocabulary
 */
public class EntityScore {
    // TODO: correct load
    private static Dictionary ngramVocab;
    private static final String TAG = "EntityScore";

    public EntityScore(Context context) {
        ngramVocab = VocabGenerator.initializeVocabulary(context);
    }


    /**
     * offline scoring mechanism, references dicitonary of keys
     * @param words word to check if exists
     * @return score of word, (1 for unigram, > 1 for identified ngrams, < 1 for unidentified ngrams)
     */
    public double getNgramScore(String words) {
        double recognizedNgramOffset = 0.10;
        double unknownNgramOffset = 0.15;
        int wordsCount = words.split("\\s").length;
        double score = 1;
        Log.d(TAG, ngramVocab.toString());
        if (wordsCount > 1) {
            if (ngramVocab.get(words) != null) {
                score += (recognizedNgramOffset * wordsCount);
            }
            else {
                score -= (unknownNgramOffset * wordsCount);
            }
        }
        Log.d(TAG, Double.toString(score));
        return score;
    }
}
