package com.example.speechclassifier;

import androidx.appcompat.app.AppCompatActivity;
import com.example.speechclassifier.speechrecognition.QuestionManager.KeywordManagerCallback;
import com.example.speechclassifier.speechrecognition.FilteredResult;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements KeywordManagerCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onQuestionFound(FilteredResult filteredResult){

    }
}
