package com.blinklogger.blinkdetection;

import android.app.Activity;
//import android.support.annotation.IntDef;
import androidx.annotation.IntDef; //<- Add this

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jason on 7/11/17.
 */

public class BlinkDetector extends FaceTrackerHelper implements SampleData.SampleDataCallbacks {
    private static final String TAG = "BlinkDetector";

    @IntDef({
            EyeState.NO_EYES_FOUND,
            EyeState.BOTH_EYES_OPEN,
            EyeState.LEFT_EYE_CLOSED,
            EyeState.RIGHT_EYE_CLOSED,
            EyeState.BOTH_EYES_CLOSED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EyeState {
        int NO_EYES_FOUND = -1;
        int BOTH_EYES_OPEN = 0;
        int LEFT_EYE_CLOSED = 1;
        int RIGHT_EYE_CLOSED = 2;
        int BOTH_EYES_CLOSED = 3;
    }

    public interface BlinkDetectorCallbacks {
        void eyeStateResult(@EyeState int state, float leftEyeValue, float rightEyeValue);
        void faceDetectionResult(boolean isFaceFound);
    }

    private BlinkDetectorCallbacks mCallbacks;
    private SampleData mSampleData;

    private float mLeftEyeClosedThreshold;
    private float mRightEyeClosedThreshold;

    public static final float MAX_EULER_Y = 18.0f;
    public static final float MIN_EULER_Y = -18.0f;
    public static final float EYE_OPEN_THRESHOLD = 0.6f;

    public BlinkDetector(Activity activity, float leftEyeClosedThreshold, float rightEyeClosedThreshold, BlinkDetectorCallbacks callbacks){
        super(activity);
        mLeftEyeClosedThreshold = leftEyeClosedThreshold;
        mRightEyeClosedThreshold = rightEyeClosedThreshold;
        mCallbacks = callbacks;
    }

    public void startSamplingData(int msWaitTimeTillNextSample, int numberOfPointsToAvg){
        startCameraFeed();

        mSampleData = new SampleData(msWaitTimeTillNextSample, numberOfPointsToAvg, this);
        mSampleData.startSampling();
    }

    public void stopSamplingData(){
        if(mSampleData != null){
            mSampleData.stopSampling();
        }
        stopCameraFeed();
    }

    @Override
    public void sampleDataResults(float avgLeftEye, float avgRightEye, float leftEye, float rightEye, float eulerY) {
        if(avgLeftEye > 0f && avgRightEye > 0f) {
            int eyeState = getCurrentEyeState(leftEye, avgLeftEye, rightEye, avgRightEye);
            mCallbacks.eyeStateResult(eyeState, avgLeftEye, avgRightEye);
        }
    }

    private @EyeState int getCurrentEyeState(float leftEye, float avgLeftEye, float rightEye, float avgRightEye) {
        //Log.d(TAG, "L: " + String.valueOf(avgLeftEye) + " :::::::: " + String.valueOf(avgRightEye));
        if(avgLeftEye < mLeftEyeClosedThreshold && avgRightEye < mRightEyeClosedThreshold
                || leftEye < mLeftEyeClosedThreshold && rightEye < mRightEyeClosedThreshold){
            return EyeState.BOTH_EYES_CLOSED;
        } else{
            return EyeState.BOTH_EYES_OPEN;
        }
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        super.onUpdate(detections, face);

        float eulerY = face.getEulerY();
        float leftEye = face.getIsLeftEyeOpenProbability();
        float rightEye = face.getIsRightEyeOpenProbability();

        boolean isEulerYValid = eulerY < MAX_EULER_Y && eulerY > MIN_EULER_Y;
        boolean isEyesFound = leftEye > 0f && rightEye > 0f;

        mCallbacks.faceDetectionResult(isEulerYValid && isEyesFound);

        if (isEyesFound && isEulerYValid) {
            mSampleData.updateValues(leftEye, rightEye, eulerY);
        } else {
            mSampleData.resetValues();
        }
    }
}
