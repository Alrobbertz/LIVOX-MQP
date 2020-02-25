package com.example.speechclassifier.speechrecognition

import android.content.Context

/**
 * Created by Jason on 3/9/18.
 */
object QuestionClassifier {

    // QUESTION TYPES
    const val NON_CLASSIFIABLE_QUESTION = 0
    const val YES_NO_QUESTION = 1
    const val NAME_QUESTION = 2

    fun getQuestionType(context: Context,
                        filteredResult: FilteredResult): Int = when {
        // check if is a name question
        filteredResult.sentenceToEvaluate == SpeechTriggerClassifier.NAME_QUESTION_TRIGGER -> NAME_QUESTION
        // check if Yes Or no Question
        //livoxNowHelper.isYesNo(context, filteredResult.sentenceToEvaluate) -> YES_NO_QUESTION
        // Unable to classify question
        else -> NON_CLASSIFIABLE_QUESTION
    }
}