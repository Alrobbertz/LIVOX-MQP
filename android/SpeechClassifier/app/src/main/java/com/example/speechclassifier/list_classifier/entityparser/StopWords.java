package com.example.speechclassifier.list_classifier.entityparser;

import android.content.Context;
import java.util.Dictionary;
import com.example.speechclassifier.R;
import com.example.speechclassifier.list_classifier.utils.ResourceLoader;

/**
 * StopWords
 *
 * class handles loading the stopwords from the resource file
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */

public class StopWords {

    /**
     * loads stopwords as a dictionary from resource files
     * @param context android context for resource loading
     * @return dictionary of stopwords
     */
    public static Dictionary initializeVocabulary(Context context) {
        String filename = "./res/raw/stopwords.json";
        String backup_url = "www.axonbeats.com/resources/stopwords.json";
        int resourceID = R.raw.stopwords;
        return ResourceLoader.loadDict(resourceID, context);
    }
}
