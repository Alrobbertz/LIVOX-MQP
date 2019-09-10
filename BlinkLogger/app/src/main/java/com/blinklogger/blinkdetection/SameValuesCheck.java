package com.blinklogger.blinkdetection;


import com.blinklogger.utils.ArrayUtils;

/**
 * Created by Jason on 7/19/17.
 */

public class SameValuesCheck {
    private static final String TAG = "SameValuesCheck";

    private int mIndex = 0;
    private float[] mFloatValues;

    public SameValuesCheck(int numberOfValuesToCompare){
        mFloatValues = new float[numberOfValuesToCompare];
    }

    public void clearAllValues(){
        int numberOfValuesToCompare = mFloatValues.length;
        mFloatValues = null;
        mFloatValues = new float[numberOfValuesToCompare];
    }

    public boolean hasAllTheSameValues(float value){
        boolean isAllTheSame = false;
        mFloatValues[mIndex] = value;
        mIndex++;

        if(mIndex == mFloatValues.length){
            isAllTheSame = ArrayUtils.areAllValuesTheSame(mFloatValues);
            mIndex = 0;
        }

        return isAllTheSame;
    }
}
