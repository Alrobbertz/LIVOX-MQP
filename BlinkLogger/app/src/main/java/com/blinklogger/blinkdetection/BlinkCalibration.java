package com.blinklogger.blinkdetection;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;

import com.blinklogger.utils.NumberUtils;

/**
 * Created by Jason on 7/18/17.
 */

public class BlinkCalibration extends FaceTrackerHelper {
    private static final String TAG = "BlinkCalibration";

    public interface BlinkCalibrationCallbacks {
        void leftEyeCalibrationComplete(float calibratedValue);
        void rightEyeCalibrationComplete(float calibratedValue);
        void faceFoundStatus(boolean isFaceFound);
        void eyeClosedCalibrationRunning(boolean isRunning, int whichEye);
    }

    public static final int NO_EYES_FOUND = -1;
    public static final int LEFT_EYE = 0;
    public static final int RIGHT_EYE = 1;

    private static final float MAX_ACCEPTABLE_CLOSED_THRESHOLD = 0.5f;

    private final MovingAverage mMovingAverage;
    private final SameValuesCheck mSameValuesCheck;
    private final BlinkCalibrationCallbacks mCallbacks;

    private int mCurrentEye = NO_EYES_FOUND;

    public BlinkCalibration(Activity activity, BlinkCalibrationCallbacks callbacks){
        super(activity);
        mCallbacks = callbacks;
        mMovingAverage = new MovingAverage(50);
        mSameValuesCheck = new SameValuesCheck(7);

        mMovingAverage.fillMovingAverageWithValue(1);
    }

    public void startCalibration(){
        startCameraFeed();
    }

    public void stopCalibration(){
        stopCameraFeed();
    }

    public void setCurrentEye(int eyeToCalibrate) {
        mCurrentEye = eyeToCalibrate;
    }

    public int getCurrentEye() {
        return mCurrentEye;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        super.onUpdate(detections, face);
        float eulerY = face.getEulerY();
        float eyeOpenProbability = NO_EYES_FOUND;

        if(mCurrentEye == LEFT_EYE){
            eyeOpenProbability = face.getIsLeftEyeOpenProbability();
        } else if(mCurrentEye == RIGHT_EYE){
            eyeOpenProbability = face.getIsRightEyeOpenProbability();
        }

        boolean isFaceFound = getFaceStatus(face, eulerY);
        boolean isCurrentEyeSet = mCurrentEye != NO_EYES_FOUND;
        if(isFaceFound && isCurrentEyeSet){
            mCallbacks.eyeClosedCalibrationRunning(true, mCurrentEye);
            findEyeCalibratedValue(eyeOpenProbability);
        } else {
            mCallbacks.eyeClosedCalibrationRunning(false, mCurrentEye);
        }
    }

    private boolean getFaceStatus(Face face, float eulerY){
        boolean isBothEyesFound = face.getIsLeftEyeOpenProbability() != -1.0f && face.getIsRightEyeOpenProbability() != -1.0f;
        boolean isEulerYValid = eulerY < BlinkDetector.MAX_EULER_Y && eulerY > BlinkDetector.MIN_EULER_Y;
        boolean isFaceFound = isEulerYValid && isBothEyesFound;

        mCallbacks.faceFoundStatus(isFaceFound);
        return isFaceFound;
    }

    private void findEyeCalibratedValue(float newEyeOpenProbability){
        float eyeAvg = mMovingAverage.getMovingAverage(newEyeOpenProbability);
        float eyeAvgRounded = NumberUtils.roundFloat(eyeAvg, 2);
        Log.d(TAG, "AVG: " + String.valueOf(eyeAvgRounded));

        if(eyeAvgRounded < MAX_ACCEPTABLE_CLOSED_THRESHOLD) {
            boolean isAllTheSame = mSameValuesCheck.hasAllTheSameValues(eyeAvgRounded);
            if (isAllTheSame) {
                Log.d(TAG, "AVG CHOOSEN: " + eyeAvgRounded);
                setCalibratedValue(eyeAvgRounded);
                mCallbacks.eyeClosedCalibrationRunning(false, mCurrentEye);
                resetAllValues();
            }
        }
    }

    private void setCalibratedValue(float eyeCalibratedValue){
        if(eyeCalibratedValue != 1.0f) {
            if(mCurrentEye == LEFT_EYE){
                mCallbacks.leftEyeCalibrationComplete(eyeCalibratedValue);
            } else if(mCurrentEye == RIGHT_EYE){
                mCallbacks.rightEyeCalibrationComplete(eyeCalibratedValue);
            }
        }
    }

    private void resetAllValues(){
        mMovingAverage.clearAllValues();
        mMovingAverage.fillMovingAverageWithValue(1);
        mSameValuesCheck.clearAllValues();
        mCurrentEye = NO_EYES_FOUND;
    }
}
