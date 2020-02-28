package com.example.speechclassifier.list_classifier.databaseimageparser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Dictionary;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ImageParser
 *
 * class handles relating entity keywords to the image database, currently the
 * labels and tags are stored on a local SQLite database
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */

public class ImageParser {
    /**
     *
     */
    private SQLiteDatabase db;
    private static final String TAG = "ImageParser";

    /**
     * used as an interface to relate entity keywords to images
     * @param context android activity context, for initializing database
     */
    public ImageParser(Context context){
        MySQLDB db_open = new MySQLDB(context);
        this.db = db_open.getWritableDatabase();
        if(! db_open.dataLoaded(db)) {
            Log.d(TAG, "loading database files");
            db_open.loadValues(db);
        }
    }


    /**
     * handles cases where keyword is not found in the database
     * @param keyword word/s to find image for
     * @return image path or default path to "?" image
     */
    private String notFound(String keyword) {
        String path = "symbol00071621.png";
        try {
            path = searchImageWord(keyword.substring(0, keyword.length()));
        } catch (Exception e) {
            try {
                path = stemmed(keyword);
            } catch (Exception e2) {
                try {
                    path = translate(keyword);
                } catch (Exception e3) {
                    try {
                        path = closest(keyword);
                    } catch (Exception e4) {
                        assert true;
                    }
                }
            }
        }
        return path;
    }

    /**
     * given a keyword will stem the word and search database
     * @param keyword word/s to find image
     * @return path to image or throws Exception
     * @throws Exception if stemmed-word is not found in database
     */
    private String stemmed(String keyword) throws Exception{
        // TODO: convert logic (non-essential)
        if (true){
            throw new Exception();
        }
        return "";
    }

    /**
     * tries to search for closest words from pre-generated WordEmbedding dictionary
     * @param keyword word/s to find image
     * @return path to image or throws Exception
     * @throws Exception if non of closest words are found in database
     */
    private String closest(String keyword) throws Exception{
        // TODO: convert logic (non-essential)
        if (true){
            throw new Exception();
        }
        return "";
    }

    /**
     * uses known database tags and tries to translate similar words using WordEmbedding dictionary
     * @param keyword word/s to find image
     * @return path to image or throws Exception
     * @throws Exception if no translation can be found for given word in database
     */
    private String translate(String keyword) throws Exception{
        // TODO: convert logic (non-essential)
        if (true){
            throw new Exception();
        }
        return "";
    }

    /**
     * searches Livox image tags from Tag database tabel for keyword
     * @param keyword word/s to find image
     * @return list of image_ids where tag was found
     */
    private List<String> searchTags(String keyword) {
        String stmt = "SELECT image_id FROM Tags WHERE tag = ? COLLATE NOCASE";
        // stmt = "SELECT * FROM Tags";
        String[] words = keyword.split("\\s");
        Log.d(TAG, words[0]);
        Dictionary<String, List<String>> image_ids = new Hashtable();
        for(String word: words) {
            String[] keywords = {word};
            Cursor tags_responses = db.rawQuery(stmt, keywords);
            Log.d(TAG, "searches tag" + Integer.toString(tags_responses.getCount()));
            tags_responses.moveToFirst();
            while(!tags_responses.isAfterLast()) {
                String image_id = tags_responses.getString(0);
                Log.d(TAG, image_id);
                if(image_ids.get(image_id) != null) {
                    image_ids.get(image_id).add(word);
                }
                else {
                    image_ids.put(image_id, new LinkedList<String>());
                    image_ids.get(image_id).add(word);
                }
            }
            tags_responses.close();
        }
        LinkedList<String> imageIDList = new LinkedList<String>();
        Enumeration<String> imageIDs = image_ids.keys();
        while(imageIDs.hasMoreElements()) {
            String key = imageIDs.nextElement();
            List<String> value = image_ids.get(key);
            if(value.size() == words.length) {
                imageIDList.add(key);
            }
        }
        Log.d(TAG, "Tag " + imageIDList.toString());
        return imageIDList;
    }


    /**
     * searches Google Image Labeling labels from Label for keyword
     * @param keyword word/s to find image
     * @return list of image_ids where label was found
     */
    private Dictionary searchLabels(String keyword) {
        String stmt = "SELECT image_id, confidence FROM Labels WHERE label = ? COLLATE NOCASE";
        String[] keywords = {keyword};
        Cursor labels_response = db.rawQuery(stmt, keywords);
        Log.d(TAG, Integer.toString(labels_response.getCount()));
        labels_response.moveToFirst();
        Dictionary<String, Double> labels = new Hashtable();
        while (!labels_response.isAfterLast()) {
            labels.put(labels_response.getString(0), labels_response.getDouble(1));
            labels_response.moveToNext();
        }
        labels_response.close();
        Log.d(TAG, labels.toString());
        return labels;
    }

    /**
     * given an image_id finds the relative path for that image in Images database tabel
     * @param image_id UUID for an image
     * @return relative path for a given image_id
     */
    private String searchImages(String image_id) throws Exception{
        String stmt = "SELECT location FROM Images WHERE image_id = ? COLLATE NOCASE";
        String[] image_ids = {image_id};
        Cursor images_response = db.rawQuery(stmt, image_ids);
        images_response.moveToFirst();
        String path = images_response.getString(0);
        images_response.close();
        Log.d(TAG, "is it 15" + Integer.toString(path.length()) + " " + path);
        return path;
    }

    /**
     * given a list of labels and confidences, returns the label with the highest confidence
     * @param labels dictionary of (String, Double) with (label, confidence)
     * @return image_id of max score or throws exception if dictionary is empty
     * @throws Exception if labels is empty
     */
    private String findMaxImageID(Dictionary labels) throws Exception{
        if (labels.isEmpty()) {
            throw new Exception();
        }
        Double max_score = -1.0;
        // find max_score
        for (Enumeration<Double> scores = labels.elements(); scores.hasMoreElements();) {
            Double score = scores.nextElement();
            if(max_score < score) {
                max_score = score;
            }
        }
        // find image_id of max_score
        for (Enumeration<String> image_ids = labels.keys(); image_ids.hasMoreElements();) {
            String image_id = image_ids.nextElement();
            if (labels.get(image_id) == max_score) {
                return image_id;
            }
        }
        return "";
    }

    /**
     * searches for keyword in database and returns most relevant image path
     * @param keyword word/s to find image
     * @return path if image is directly found otherwise throws Exception
     * @throws Exception if keyword is not found in Labels or Tags tables
     */
    private String searchImageWord(String keyword) throws Exception{
        Dictionary<String, Double> labels = searchLabels(keyword);
        Log.d(TAG, keyword);
        Log.d(TAG, labels.toString());
        List<String> tags = searchTags(keyword);
        Log.d(TAG, tags.toString());
        for(String tag: tags) {
            if (labels.get(tag) != null) {
                labels.put(tag, labels.get(tag) + 0.85);
            }
            else {
                labels.put(tag, 0.85);
            }
        }

        String path = searchImages(findMaxImageID(labels));
        return path;
    }

    /**
     * searches a keyword in labels, tags database to find most relevant image path
     * @param keyword word/s to find image
     * @return path to image or default "?" path, currently prefixed by google storage bucket link
     */
    public String getImage(String keyword) {
        String path = "";
        try {
            path = searchImageWord(keyword);
        } catch (Exception e5) {
            path = notFound(keyword);
        }
//        path = "https://storage.googleapis.com/livox-images/full/" + path;
        path = path.replace(".png", "").replace("\r", "");
        return path;
    }
}
