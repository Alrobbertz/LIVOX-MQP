package com.example.speechclassifier.list_classifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Scanner;


public class WebAPIHelper {


    private static final String TAG = "WebHelper";

    /**
     * Gets an input stream of the response to a get request at the given url.
     * Adds the given parameters to the url
     * returns null in case of errors
     *
     * @param urlStr the URL to create a get request to
     * @param parameters a map of the input parameters and their associated values
     * @return an InputStream of the response from the Url
     */
    public static InputStream get(String urlStr, AbstractMap<String, String> parameters){
        String params = createParamString(parameters);
        return get(urlStr + "?" + params);
    }

    /**
     * Gets an input stream of the response to a get request at the given url.
     * returns null in case of error
     *
     * @param urlStr the URL to create a get request to
     * @return an InputStream of the response from the Url
     */
    public static InputStream get(String urlStr){
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

    /**
     * Turns the given URL parameters into a string
     *
     * @param map the map of each parameter and its associated value
     * @return a String representation of the given url parameters
     */
    private static String createParamString(AbstractMap<String, String> map){
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

    /**
     * Converts an InputStream into a String
     * @param response the InputStream to convert
     * @return The String representation of the InputStream
     */
    public static String responseToString(InputStream response){
        return new Scanner(response, "UTF-8").useDelimiter("\\A").next();
    }

    /**
     * Converts an InputStream into a Bitmap
     * @param response the InputStream to convert
     * @return The Bitmap representation of the InputStream
     */
    public static Bitmap responseToBitmap(InputStream response){
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
