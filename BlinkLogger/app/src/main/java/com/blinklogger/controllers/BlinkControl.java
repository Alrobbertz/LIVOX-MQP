package com.blinklogger.controllers;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.blinklogger.blinkdetection.BlinkDetector;

import static com.blinklogger.blinkdetection.BlinkDetector.*;

/**
 * Created by Jason on 7/13/17.
 */

public class BlinkControl implements BlinkDetectorCallbacks {
    private static final String TAG = "BlinkControl";

    public interface BlinkControlCallback {
        void bothEyesOpen();
        void noEyesFound();
    }

    private static final int MS_DELAY = 100;
    private static final int NUMBER_TO_AVERAGE = 7;

    private final BlinkDetector mBlinkDetector;
    private final BlinkControlCallback mBlinkControlCallback;
    private final Activity mActivity;


    private int[] mEyeStates = new int[2];
    private int mEyeStatesIndex = 0;

    private boolean mUseMoveRightOrLeft = true;
    private boolean mCanPerformAction = true;

    public BlinkControl(Activity activity, float leftEyeCloseThreshold, float rightEyeCloseThreshold, BlinkControlCallback blinkControlCallback){
        mActivity = activity;
        mBlinkControlCallback = blinkControlCallback;
        mBlinkDetector = new BlinkDetector(activity, leftEyeCloseThreshold, rightEyeCloseThreshold, this);

    }

    public void startBlinkDetection(boolean useMoveLeftOrRight){
        mUseMoveRightOrLeft = useMoveLeftOrRight;
        startBlinkDetection();
    }

    public void startBlinkDetection(){
        mBlinkDetector.startSamplingData(MS_DELAY, NUMBER_TO_AVERAGE);
    }

    public void stopBlinkDetection(){
        mBlinkDetector.stopSamplingData();
    }

    @Override
    public void faceDetectionResult(boolean isFaceFound) {
        if(isFaceFound) {
            //Log.d(TAG, "EYE STATE: Both eyes open");
            mBlinkControlCallback.bothEyesOpen();
        } else {
            //Log.d(TAG, "EYE STATE: No eyes found");
            mBlinkControlCallback.noEyesFound();
        }
    }

    @Override
    public void eyeStateResult(@EyeState int state, float leftEyeValue, float rightEyeValue) {
        Log.d(TAG,  "Left Eye: " + String.valueOf(leftEyeValue) +
                "\tRight Eye: " + String.valueOf(rightEyeValue) +
                "\tblink EYE STATE: " + String.valueOf(state));
        mEyeStatesIndex = mEyeStatesIndex == 1 ? 0 : 1;
        mEyeStates[mEyeStatesIndex] = state;

        if(mEyeStates[0] == mEyeStates[1]) {

            performAction(state);
            mEyeStates[0] = 0;
            mEyeStates[1] = 0;

        }
    }

    private void performAction(@EyeState int state){
        switch(state){
            case EyeState.LEFT_EYE_CLOSED:
                if(mUseMoveRightOrLeft) {
//                    mButtonControls.moveLeft();
                }
                break;
            case EyeState.RIGHT_EYE_CLOSED:
                if(mUseMoveRightOrLeft) {
//                    mButtonControls.moveRight();
                }
                break;
            case EyeState.BOTH_EYES_CLOSED:
                if(mCanPerformAction) {
//                    mButtonControls.selectItem();
//                    delayNextPerformAction(1000);
                }
                break;
        }
    }

    private void delayNextPerformAction(int msDelay) {
        mCanPerformAction = false;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCanPerformAction = true;
            }
        };
        handler.postDelayed(runnable, msDelay);
    }
}
