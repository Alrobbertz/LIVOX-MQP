package com.blinklogger;

import androidx.appcompat.app.AppCompatActivity;
import com.blinklogger.blinkdetection.*;
import com.blinklogger.controllers.BlinkControl;
import com.blinklogger.R;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements BlinkControl.BlinkControlCallback{
    private static final String TAG = "MAIN";


    private RelativeLayout mMainLayout;

    private BlinkControl mBlinkControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main);

        mMainLayout = (RelativeLayout)findViewById(R.id.rl_main);

        float[] eyeClosedThresholds = {0.6f, 0.6f};
        Log.d(TAG, "EYE THRESHOLDS L: " + String.valueOf(eyeClosedThresholds[0]) + " R: " + String.valueOf(eyeClosedThresholds[1]));
        mBlinkControl = new BlinkControl(this, eyeClosedThresholds[0], eyeClosedThresholds[1], this);

        startBlinkDetection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // check for TTS data
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");




    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");


        if(mBlinkControl != null) {
            startBlinkDetection();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mBlinkControl != null) {
            mBlinkControl.stopBlinkDetection();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }

    /**
     *  L or R Blink control disabled for now, will enable in future update
     */
    private void startBlinkDetection(){
        //boolean useBlinkDetection = LivoxSettings.getUseBlinkDetection(this, mUserId);
        boolean useBlinkDetection = true;
        if(mBlinkControl != null && useBlinkDetection){
            //int secondScan = LivoxSettings.getSecondsScan(this, mUserId);
            int secondScan = 0;
            if(secondScan == 0) {
                mBlinkControl.startBlinkDetection(false);
                Log.d(TAG, "BLINK DETECTION STARTED");
            } else {
                mBlinkControl.startBlinkDetection(false);
            }
        }
    }

    @Override
    public void bothEyesOpen() {
        if(mMainLayout != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMainLayout.setBackgroundColor(Color.GREEN);
                }
            });
        }
    }

    @Override
    public void noEyesFound() {
        if(mMainLayout != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMainLayout.setBackgroundColor(Color.RED);
                }
            });
        }
    }
}
