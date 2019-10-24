package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;
import com.example.speechclassifier.speechrecognition.QuestionManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    private String TAG = "MainActivity";

    private TextView fullPhrase;
    private TextView launchPhrase;
    private TextView invocationPhrase;
    private TextView listEntityPhrase;

    private ImageView listEntityImage1;
    private TextView listEntityText1;
    private ImageView listEntityImage2;
    private TextView listEntityText2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(R.id.layout);
        listEntityImage1 = rootView.findViewById(R.id.entity_1_image);
        listEntityText1 = rootView.findViewById(R.id.entity_1_text);
        listEntityImage2 = rootView.findViewById(R.id.entity_2_image);
        listEntityText2 = rootView.findViewById(R.id.entity_2_text);

        fullPhrase = rootView.findViewById(R.id.full_phrase);
        launchPhrase = rootView.findViewById(R.id.launch_phrase);
        invocationPhrase = rootView.findViewById(R.id.invocation_phrase);
        listEntityPhrase = rootView.findViewById(R.id.list_entity_phrase);


        // init of text fields
        setFullPhrase("");
        setInitiatorPhrase("");
        setInvocationPhrase("");
        setListEntityPhrase("");

        //init of list entities and associated image
        setListEntityText1("");

        setListEntityText2("");
        //setListEntityImage2();

        initNaturalConversation();
    }



    private void initNaturalConversation() {
        String languageTag = "en-us";

        //VolumeUtils.INSTANCE.muteStreamVolume(this, true);

        String mKeyword = "john";
        String KEYWORD_FOUND_TONE = "android.resource://br.com.livox/raw/keyword_found_tone";
        Uri keywordSoundUri = Uri.parse(KEYWORD_FOUND_TONE);

        //int liteBlueColor = ContextCompat.getColor(this, R.color.lite_blue);
        //mMainLayout.setBackgroundColor(liteBlueColor);

        QuestionManager mQuestionManager = new QuestionManager(mKeyword, languageTag, this, keywordSoundUri, this, this);
        mQuestionManager.startListeningForKeyword();
        String keyword = mKeyword; //LivoxSettings.getNaturalConversationKeyword(this, mUserId);
        Log.d(TAG,  "Listening for KEYWORD " + keyword);
    }

    public void onQuestionFound(FilteredResult filteredResult){}

    public void setFullPhrase(String fullPhrase) {
        this.fullPhrase.setText("Full Phrase: " + fullPhrase);
    }

    public void setInitiatorPhrase(String initiatorPhrase) {
        this.launchPhrase.setText("Launch Phrase: " + initiatorPhrase);
    }

    public void setInvocationPhrase(String invocationPhrase) {
        this.invocationPhrase.setText("Invocation Phrase: " + invocationPhrase);
    }

    public void setListEntityPhrase(String listEntityPhrase) {
        this.listEntityPhrase.setText("List Phrase: " + listEntityPhrase);
    }

    public void setListEntityImage1(String imageURL) {
        Bitmap[] bmp = {null};

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(imageURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    con.setRequestProperty("Accept","*/*");
                    con.setInstanceFollowRedirects(true);
                    bmp[0] = BitmapFactory.decodeStream(con.getInputStream());
                } catch (Exception e){}
            }
        });

        t.start();
        try {
            t.join();
        }catch(Exception e){}

        if(bmp[0] != null){
            this.listEntityImage1.setImageBitmap(bmp[0]);
        }else{
            Log.d(TAG, "trying to load null image");
        }
    }

    public void setListEntityText1(String listEntityText1) {
        this.listEntityText1.setText(listEntityText1);
    }

    public void setListEntityImage2(String imageURL) {
        Bitmap[] bmp = {null};

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(imageURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    con.setRequestProperty("Accept","*/*");
                    con.setInstanceFollowRedirects(true);
                    bmp[0] = BitmapFactory.decodeStream(con.getInputStream());
                } catch (Exception e){}
            }
        });

        t.start();
        try {
            t.join();
        }catch(Exception e){}

        if(bmp[0] != null){
            this.listEntityImage2.setImageBitmap(bmp[0]);
        }else{
            Log.d(TAG, "trying to load null image");
        }

    }

    public void setListEntityText2(String listEntityText2) {
        this.listEntityText2.setText(listEntityText2);
    }
}
