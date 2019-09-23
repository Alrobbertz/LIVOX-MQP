package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;
import com.example.speechclassifier.R;
import com.example.speechclassifier.speechrecognition.QuestionManager;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import androidx.core.content.ContextCompat;
public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        QuestionManager mQuestionManager = new QuestionManager(mKeyword, languageTag, this, keywordSoundUri, this);
        mQuestionManager.startListeningForKeyword();
        String keyword = mKeyword; //LivoxSettings.getNaturalConversationKeyword(this, mUserId);
        Log.d(TAG,  "Listening for KEYWORD " + keyword);
    }

    public void onQuestionFound(FilteredResult filteredResult){

    }
}
