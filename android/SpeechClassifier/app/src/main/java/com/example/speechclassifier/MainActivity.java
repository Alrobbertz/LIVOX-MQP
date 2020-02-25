package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;
import com.example.speechclassifier.speechrecognition.QuestionManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    private String TAG = "MainActivity";

    private TextView fullPhrase;
    private TextView questionPhrase;

    QuestionManager mQuestionManager;

    private LinearLayout listEntities;

    private Drawable defaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(R.id.layout);

        fullPhrase = rootView.findViewById(R.id.full_phrase);
        questionPhrase = rootView.findViewById(R.id.question_phrase);

        Resources res = getResources();
        defaultImage = ResourcesCompat.getDrawable(res, R.drawable.pasta, null);

        listEntities = rootView.findViewById(R.id.list_entity_layout);

        // init of text fields
        setFullPhrase("");
        setQuestionPhrase("");

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

    public void setQuestionPhrase(String initiatorPhrase) {
        this.questionPhrase.setText("Question Phrase: " + initiatorPhrase);
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
