package com.example.speechclassifier.speechrecognition

import android.content.Context
import android.util.Log
import com.example.speechclassifier.R
import java.util.ArrayList

/**
 * Created by Jason on 3/12/18.
 */
class SpeechTriggerClassifier(context: Context,
                              languageTag: String,
                              private val mKeyword: String) {
    companion object {
        const val TAG = "SpeechTriggerClassifier"

        // TRIGGERS
        const val NAME_QUESTION_TRIGGER = "nameQuestion"
        const val KEYWORD_ONLY_TRIGGER = "keyWordOnly"
    }

//    private val mNameWord = Languages.getLocaleStringResource(context, languageTag, R.string.name_word)
//    private val mNameQuestions = Languages.getLocaleStringArrayResource(context, languageTag, R.array.name_questions);

    private val mKeywordRegex = Regex("(?i)($mKeyword).*")

    fun getFilteredResult(results: ArrayList<String>, hasKeywordBeenCalled: Boolean): FilteredResult {
        val filteredResult = FilteredResult("", "", 0)
        cycleThroughResults@ for(result in results) {
            when {
            // Check if result is only the keyword
                result == mKeyword -> {
                    Log.d(TAG, "ONLY THE KEYWORD FOUND")
                    filteredResult.sentenceToEvaluate = KEYWORD_ONLY_TRIGGER
                    filteredResult.originalResult = result
                    break@cycleThroughResults
                }
            // Check if result contains the keyword or that the keyword only was called previously
                result.contains(mKeywordRegex) || hasKeywordBeenCalled -> {
                    Log.d(TAG, "KEYWORD RESULT: $result")
                    filteredResult.sentenceToEvaluate = result
                            .replace(mKeyword.toRegex(), "")
                            .replace("?", "")
                            .toLowerCase()
                    filteredResult.originalResult = result
                    break@cycleThroughResults
                }
            // Check if result contains the nameword ie: name
//                result.contains(mNameWord, true) -> {
//                    Log.d(TAG, "CHECK FOR NAME QUESTION!!!!")
//                    var isNameQuestion = false
//                    cycleThroughNameQuestion@ for (nameQuestion in mNameQuestions) {
//                        val nameQuestionRegex = Regex("(?i)($nameQuestion).*")
//                        if (result.contains(nameQuestionRegex)) {
//                            Log.d(TAG, "NAME QUESTION MATCHED: $nameQuestion")
//                            isNameQuestion = true
//                            filteredResult.sentenceToEvaluate = NAME_QUESTION_TRIGGER
//                            filteredResult.originalResult = result
//                            break@cycleThroughNameQuestion
//                        }
//                    }
//
//                    if (isNameQuestion) break@cycleThroughResults
//                }
            }
        }

        return filteredResult
    }
}