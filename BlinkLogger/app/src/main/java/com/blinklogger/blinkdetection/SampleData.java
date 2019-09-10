package com.blinklogger.blinkdetection;


import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Jason on 7/11/17.
 */

public class SampleData {
    private static final String TAG = "SampleData";

    public interface SampleDataCallbacks {
        void sampleDataResults(float avgLeftEye, float avgRightEye, float leftEye, float rightEye, float eulerY);
    }

    private float mAvgLeftEye = 1f;
    private float mAvgRightEye = 1f;
    private float mLeftEye = 1f;
    private float mRightEye = 1f;
    private float mEulerY = 20.0f;

    private int mWaitTimeMS;
    private SampleDataCallbacks mCallback;

    private MovingAverage mMovingAverage;

    private Handler mCallbackHandler;
    private Runnable mCallbackRunnable;
    private Runnable mMainRunnable;
    private Future mLongRunningTask;

    private boolean canReset = true;

    public SampleData(int waitTimeMS, int numberOfValuesToAverage, SampleDataCallbacks callback){
        mWaitTimeMS = waitTimeMS;
        mCallback = callback;
        mMovingAverage = new MovingAverage(numberOfValuesToAverage);
        mMovingAverage.fillMovingAverageWithValue(1f);
    }

    public void updateValues(float leftEye, float rightEye, float eulerY){
        float [] avgData = mMovingAverage.getMovingAverageForTwoValues(leftEye, rightEye);
        mAvgLeftEye = avgData[0];
        mAvgRightEye = avgData[1];
        mLeftEye = leftEye;
        mRightEye = rightEye;
        mEulerY = eulerY;

        if(!canReset){
            canReset = true;
        }
    }

    public void resetValues(){
        if(canReset) {
            Log.d(TAG, "RESET VALUES ::::::::");
            mMovingAverage.fillMovingAverageWithValue(1f);
            mAvgLeftEye = 1f;
            mAvgRightEye = 1f;
            mLeftEye = 1f;
            mRightEye = 1f;
            mEulerY = 20.0f;
            canReset = false;
        }
    }

    public void startSampling() {
        mCallbackHandler = new Handler();
        mCallbackRunnable = new Runnable() {
            @Override
            public void run() {
                mCallback.sampleDataResults(mAvgLeftEye, mAvgRightEye, mLeftEye, mRightEye, mEulerY);
                mCallbackHandler.postDelayed(this, mWaitTimeMS);
            }
        };

        mMainRunnable = new Runnable() {
            @Override
            public void run() {
                mCallbackRunnable.run();
            }
        };
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        mLongRunningTask = threadPoolExecutor.submit(mMainRunnable);

    }

    public void stopSampling(){
        mLongRunningTask.cancel(true);
        mCallbackHandler.removeCallbacks(mCallbackRunnable);
    }
}
