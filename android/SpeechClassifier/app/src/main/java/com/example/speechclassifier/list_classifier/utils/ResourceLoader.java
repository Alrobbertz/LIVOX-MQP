package com.example.speechclassifier.list_classifier.utils;

import android.content.Context;
import android.util.Log;

import com.example.speechclassifier.list_classifier.WebAPIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class ResourceLoader {
    public static final String TAG = "ResourceLoader";


    public static List loadTSVList(int resourceID, Context context) {
//        int id = context.getResources().getIdentifier(filename, "raw", context.getPackageName());
        Log.d(TAG, "Loads the TSV variables into SQL server");
        InputStream is = context.getResources().openRawResource(resourceID);
        String jsonString = WebAPIHelper.responseToString(is);
        String[] tokens = jsonString.split("\n");
        List<List<String>> columnValues = new ArrayList<List<String>>();
        for(String rowString: tokens){
            ArrayList<String> row = new ArrayList<String>();
            for(String columnValue: rowString.split("\t")){
                row.add(columnValue);
            }
            columnValues.add(row);
        }

        return columnValues;
    }

    public static String loadSQL(String resourceName, Context context){
        int id = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
        return loadSQL(id, context);
    }

    public static String loadSQL(int resourceID, Context context) {
        // TODO: android loading String from filename
        InputStream is = context.getResources().openRawResource(resourceID);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        String sql = "";

        try {
            // While the BufferedReader readLine is not null
            while ((readLine = br.readLine()) != null) {
                sql+=readLine;
            }

            // Close the InputStream and BufferedReader
            is.close();
            br.close();

        } catch (IOException e) {
                e.printStackTrace();
        }


        return sql;
    }

    public static Dictionary loadDict(String resourceName, Context context)throws JSONException{
        int id = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
        return loadDict(id, context);
    }

    public static Dictionary loadDict(int resourceID, Context context){
        InputStream is = context.getResources().openRawResource(resourceID);
        String jsonString = WebAPIHelper.responseToString(is);

        Dictionary jsonDict = new Hashtable();
        try {
        JSONObject json = new JSONObject(jsonString);
        JSONArray names = json.names();
        for(int i = 0; i < names.length(); i++){
            String name = names.getString(i);
            jsonDict.put(name, json.getDouble(name));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonDict;
    }

    public static LinkedHashMap loadDoubleDict(String resourceName, Context context){
        int id = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
        return loadDoubleDict(id, context);
    }

    public static LinkedHashMap loadDoubleDict(int resourceID, Context context){
        InputStream is = context.getResources().openRawResource(resourceID);
        String jsonString = WebAPIHelper.responseToString(is);

        LinkedHashMap<String, LinkedHashMap<String, Integer>> jsonDict = new LinkedHashMap<>();
        //Dictionary jsonDict = new Hashtable();
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            JSONArray names = json.names();
//            Log.d(TAG, names.get(0).toString());
//            Log.d(TAG, Integer.toString(names.length()));
            for(int i = 0; i < names.length();i++) {
                jsonDict.put(names.get(i).toString(), new LinkedHashMap<>());
            }
            for(int i = 0; i < names.length(); i++){
                JSONObject mappedWords = json.getJSONObject(names.get(i).toString());
                JSONArray subwords = mappedWords.names();
//                Log.d(TAG, Integer.toString(subwords.length()));
                LinkedHashMap questionWordDict = (LinkedHashMap) jsonDict.get(names.get(i).toString());
                for(int j = 0; j < subwords.length(); j++) {
                    String subword = subwords.getString(j);
//                    Log.d(TAG, Integer.toString(mappedWords.getInt(subword)));
                    questionWordDict.put(subword, mappedWords.getInt(subword));
                }
                jsonDict.put(names.get(i).toString(), questionWordDict);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonDict;
    }

    private static Dictionary downloadDict(String url) throws JSONException{
        InputStream is = WebAPIHelper.get(url);
        String jsonString = WebAPIHelper.responseToString(is);

        Dictionary internetJsonDict = new Hashtable();
        JSONObject json = new JSONObject(jsonString);
        JSONArray names = json.names();
        for(int i = 0; i < names.length(); i++){
            String name = names.getString(i);
            internetJsonDict.put(name, json.getDouble(name));
        }

        return internetJsonDict;
    }

    public static Dictionary initializeResource(String filename, String url, Context context) {
        // TODO: correct exception handling for android
        Dictionary resourceDict = null;
        try {
            resourceDict = loadDict(filename, context);
        } catch (Exception e) {
            //resourceDict = downloadDict(url);
            e.printStackTrace();
        }
        return resourceDict;
    }
}
