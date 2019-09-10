package com.blinklogger.blinkdetection;

/**
 * Created by Jason on 7/10/17.
 */

public class MovingAverage {
    private static final String TAG = "MovingAverage";

    private float[][] mValues;
    private int mValueChangedAtIndex = 0;
    private int mNumberOfValuesToAverage;

    public MovingAverage(int numberOfValuesToAverage){
        mNumberOfValuesToAverage = numberOfValuesToAverage;
        mValues = numberOfValuesToAverage != 0 ? new float[numberOfValuesToAverage][2] :
                new float[1][2];
    }

    public void fillMovingAverageWithValue(float value){
        for(int i = 0; i < mNumberOfValuesToAverage; i++){
            getMovingAverage(value);
        }
    }

    public void clearAllValues(){
        mValues = null;
        mValues = new float[mNumberOfValuesToAverage][2];
    }

    public int getNumberOfValuesToAverage(){
        return mNumberOfValuesToAverage;
    }

    public int getValueChangedAtIndex(){
        return mValueChangedAtIndex;
    }

    public float getMovingAverage(float newValue){
        float sum = 0;
        float average;

        mValueChangedAtIndex = mValueChangedAtIndex == mValues.length ? 0 : mValueChangedAtIndex;
        for(int i = 0; i < mValues.length; i++) {
            if(i == mValueChangedAtIndex){
                mValues[i][0] = newValue;
            }
            //Log.d(TAG, "VCAI: " + String.valueOf(mValueChangedAtIndex) + " Value[" + String.valueOf(i) + "] = " + String.valueOf(mValues[i]));

            sum += mValues[i][0];
        }
        mValueChangedAtIndex++;

        average = sum/mValues.length;
        //Log.d(TAG, "AVG: " + String.valueOf(average));
        return average;
    }

    public float[] getMovingAverageForTwoValues(float newValue, float anotherNewValue){
        float sum[] = {0, 0};

        float average[] = new float[2];

        mValueChangedAtIndex = mValueChangedAtIndex == mValues.length ? 0 : mValueChangedAtIndex;
        for(int i = 0; i < mValues.length; i++) {
            if(i == mValueChangedAtIndex){
                mValues[i][0] = newValue;
                mValues[i][1] = anotherNewValue;
            }
            //Log.d(TAG, "VCAI: " + String.valueOf(mValueChangedAtIndex) +
            //        " Value[" + String.valueOf(i) + "] " + "= " + String.valueOf(mValues[i][0]) +
            //        " Value2: " + String.valueOf(mValues[i][1]));

            sum[0] += mValues[i][0];
            sum[1] += mValues[i][1];

        }
        mValueChangedAtIndex++;

        average[0] = sum[0]/mValues.length;
        average[1] = sum[1]/mValues.length;
        //Log.d(TAG, "AVG 1: " + String.valueOf(average[0]) + " AVG 2: " + String.valueOf(average[1]));
        return average;
    }


}
