package com.blinklogger.utils;

import android.util.Log;

/**
 * Created by Jason on 7/19/17.
 */

public class ArrayUtils {
    private static final String TAG = "ArrayUtils";
    /**
     * For int arrays
     */
    // get the maximum value
    public static int getMaxValue(int[] array) {
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    // get the miniumum value
    public static int getMinValue(int[] array) {
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    // check if all values in the array are the same
    public static boolean areAllValuesTheSame(int[] array){
        int allSameValueSum = array[0] * array.length;
        int addedSum = 0;

        for(int value : array){
            addedSum += value;
        }
        return allSameValueSum == addedSum;
    }


    /**
     * For float arrays
     */
    // get the maximum value
    public static float getMaxValue(float[] array) {
        float maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    // get the miniumum value
    public static float getMinValue(float[] array) {
        float minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    // check if all values in the array are the same
    public static boolean areAllValuesTheSame(float[] array){
        float allSameValueSum = array[0] * array.length;
        float addedSum = 0.0f;

        for(float value : array){
            addedSum += value;
        }
        return allSameValueSum == addedSum;
    }
}
