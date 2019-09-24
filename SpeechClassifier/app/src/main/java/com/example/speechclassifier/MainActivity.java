package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;
import com.example.speechclassifier.speechrecognition.QuestionManager;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    private String TAG = "MainActivity";

    private TextView fullPhrase;
    private TextView initiatorPhrase;
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
        initiatorPhrase = rootView.findViewById(R.id.intentions_phrase);
        invocationPhrase = rootView.findViewById(R.id.initiators_phrase);
        listEntityPhrase = rootView.findViewById(R.id.list_entity_phrase);


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

    public void setFullPhrase(String fullPhrase) {
        this.fullPhrase = fullPhrase;
    }

    public void setInitiatorPhrase(String initiatorPhrase) {
        this.initiatorPhrase = initiatorPhrase;
    }

    public void setInvocationPhrase(String invocationPhrase) {
        this.invocationPhrase = invocationPhrase;
    }

    public void setListEntityPhrase(String listEntityPhrase) {
        this.listEntityPhrase = listEntityPhrase;
    }

    public void setListEntityImage1(ImageView listEntityImage1) {
        this.listEntityImage1 = listEntityImage1;
    }

    public void setListEntityText1(String listEntityText1) {
        this.listEntityText1 = listEntityText1;
    }

    public void setListEntityImage2(ImageView listEntityImage2) {
        this.listEntityImage2 = listEntityImage2;
    }

    public void setListEntityText2(String listEntityText2) {
        this.listEntityText2 = listEntityText2;
    }
}
