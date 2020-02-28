package com.example.speechclassifier.list_classifier.questionparser;


import android.content.Context;

import com.example.speechclassifier.R;
import com.example.speechclassifier.list_classifier.utils.ResourceLoader;

import java.util.LinkedHashMap;

/**
 * QuestionWordGenerator
 *
 * class handles loading phrase parse vocabulary from resources
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */
public class QuestionWordGenerator {

    /**
     * loads phrase parse vocabulary as a dictionary from resource files
     * @param context android context for resource loading
     * @return double dictionary of phrase parse vocabulary
     */
    public static LinkedHashMap initializeVocabulary(Context context) {
        String filename = "./res/raw/phrase_parse_vocabulary.json";
        String backup_url = "www.axonbeats.com/resources/questionwords.json";
        int resourceID = R.raw.phrase_parse_vocabulary;
        return ResourceLoader.loadDoubleDict(resourceID, context);
    }
}
