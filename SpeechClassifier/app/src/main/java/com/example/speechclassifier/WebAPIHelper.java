package com.example.speechclassifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebAPIHelper {

    //TODO make sure that all input streams are closed

    private static final String TAG = "WebHelper";

    public static JSONArray getParsedQuestion(String question){
        HashMap<String, String> params = new HashMap<>();
        params.put("phrase", question);
        params.put("ngram", "3");
        InputStream response = get("http://api.axonbeats.com/question_img_parser", params);

        if(response == null){//exception thrown during web request
            //TODO handle this better
            return null;
        }
        String decodedResponse = responseToString(response);
        Log.d(TAG, decodedResponse);

        try{
            return new JSONArray(decodedResponse);
        }catch(JSONException e){
            Log.d(TAG, e.toString());
            return null;
        }
    }

    public static List<String> getListEntities(String utterance){

        HashMap<String, String> params = new HashMap<String,String>();
        params.put("phrase", utterance);
        params.put("ngram", "3");
        InputStream response = get("http://api.axonbeats.com/offline_entities", params);
        if(response == null){//exception thrown during web request
            //TODO handle this better
            return new ArrayList<String>();
        }
        String decodedResponse = responseToString(response);
        Log.d(TAG, decodedResponse);

        //change this to a JSON parsing library
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\"[^,]*\"").matcher(decodedResponse);
        while (m.find()) {
            String currentMatch = m.group();
            currentMatch = currentMatch.substring(1, currentMatch.length() - 1);
            allMatches.add(currentMatch);
            Log.d(TAG, currentMatch);
        }

        return allMatches;
    }

    public static String getRelevantImage(String entity){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keyword", entity);
        InputStream response = get("http://api.axonbeats.com/image", params);
        if(response == null){//exception thrown during web request
            //TODO find better default image
            return "https://storage.googleapis.com/livox-images/full/symbol00064291.png";
        }

        String decodedResponse = responseToString(response);
        decodedResponse = decodedResponse.substring(1, decodedResponse.length() - 2);//get rid of "" on either side of the string
        Log.d(TAG, decodedResponse);

        return decodedResponse;
    }

    public static Bitmap getImage(String imageUrl){
        InputStream response = get(imageUrl);
        if(response == null){
            //TODO get a better default image
            Log.d(TAG, "Failure loading image.");
            return null;
        }
        Bitmap decodedResponse = responseToBitmap(response);
        Log.d(TAG, "decoded:" + decodedResponse);
        return decodedResponse;
    }

    private static InputStream get(String urlStr, AbstractMap<String,String> parameters){
        String params = createParamString(parameters);
        return get(urlStr + "?" + params);
    }

    private static InputStream get(String urlStr){
        Log.d(TAG, "Get request start");
        InputStream[] response = new InputStream[1];

        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    con.setRequestProperty("Accept","*/*");
                    con.setInstanceFollowRedirects(true);
                    Log.d(TAG, "URL: " + con.toString());

                    response[0] = new BufferedInputStream(con.getInputStream());
                    Log.d(TAG, "Get request successful");
                } catch (Exception e) {
                    Log.d(TAG, "Get request failure");
                    Log.d(TAG, e.toString());
                }
            }

        });

        t.start();
        try {
            t.join();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return response[0];
    }

    private static String createParamString(AbstractMap<String,String> map){
        String params = "";
        boolean first = true;
        for(Map.Entry<String, String> entry: map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().replace(" ", "%20");
            if (!first)
                params += "&";
            else
                first = false;
            params += key + "=" + value;
        }
        return params;
    }

    private static String responseToString(InputStream response){
        return new Scanner(response, "UTF-8").useDelimiter("\\A").next();
    }

    private static Bitmap responseToBitmap(InputStream response){
        Bitmap[] bitmap = new Bitmap[1];
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    bitmap[0] = BitmapFactory.decodeStream(response);

                }catch(Exception e){
                    Log.d(TAG, e.toString());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return bitmap[0];
    }


}
