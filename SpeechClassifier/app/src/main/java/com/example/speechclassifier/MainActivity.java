package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;
import com.example.speechclassifier.speechrecognition.QuestionManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.function.Function;


public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    private String TAG = "MainActivity";

    private TextView fullPhrase;
    private TextView launchPhrase;
    private TextView invocationPhrase;
    private TextView listEntityPhrase;

    QuestionManager mQuestionManager;

    private LinearLayout listEntities;

    private Drawable defaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(R.id.layout);

        fullPhrase = rootView.findViewById(R.id.full_phrase);
        launchPhrase = rootView.findViewById(R.id.launch_phrase);
        invocationPhrase = rootView.findViewById(R.id.invocation_phrase);
        listEntityPhrase = rootView.findViewById(R.id.list_entity_phrase);

        Resources res = getResources();
        defaultImage = ResourcesCompat.getDrawable(res, R.drawable.pasta, null);

        listEntities = rootView.findViewById(R.id.list_entity_layout);

        // init of text fields
        setFullPhrase("");
        setInitiatorPhrase("");
        setInvocationPhrase("");
        setListEntityPhrase("");

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

        mQuestionManager = new QuestionManager(mKeyword, languageTag, this, keywordSoundUri, this, this);
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

    public void startListening(View view){
        mQuestionManager.startListeningForKeyword();
    }

    public void stopListening(View view){
        mQuestionManager.stopListeningForKeyword();
    }

    public void addListEntity(Bitmap image, String entityName){
        ListEntity newListEntity = new ListEntity(this);
        newListEntity.setImage(image);
        newListEntity.setText(entityName);
        newListEntity.setLayoutParams(new ListEntity.LayoutParams(200, 300));

        listEntities.addView(newListEntity);
    }

    public void resetEntities(){
        listEntities.removeAllViews();
    }
}
