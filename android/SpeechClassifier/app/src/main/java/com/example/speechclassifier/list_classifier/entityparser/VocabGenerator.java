package com.example.speechclassifier.list_classifier.entityparser;

import android.content.Context;
import java.util.Dictionary;
import com.example.speechclassifier.R;
import com.example.speechclassifier.list_classifier.utils.ResourceLoader;

/**
 * VocabGenerator
 *
 * class handles loading of recognized n-gram vocabulary from resource files into memory
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 *
 */

public class VocabGenerator {

    /**
     * loads vocabulary as a dictionary from resource files
     * @param context android context for resource loading
     * @return dictionary of n-gram vocabulary
     */
    public static Dictionary initializeVocabulary(Context context) {
        String filename = "./res/raw/vocabulary.json";
        String backup_url = "www.axonbeats.com/resources/vocabulary.json";
        int resourceID = R.raw.vocabulary;
        return ResourceLoader.loadDict(resourceID, context);
    }
}