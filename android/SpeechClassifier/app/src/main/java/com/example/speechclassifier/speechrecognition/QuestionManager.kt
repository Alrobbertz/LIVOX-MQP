package com.example.speechclassifier.speechrecognition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.util.Log
import com.example.speechclassifier.MainActivity
import com.example.speechclassifier.WebAPIHelper
import java.util.*
import com.example.speechclassifier.list_classifier.ListClassificationOrchestrator
import com.example.speechclassifier.list_classifier.ListClassificationOrchestrator_2
import com.example.speechclassifier.list_classifier.Phrase
import com.example.speechclassifier.list_classifier.UtteranceDetectorOnline
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.regex.Pattern


/**
 * Created by Jason on 11/28/17.
 */

class QuestionManager(keyWord: String,
                      languageTag: String,
                      private val mContext: Context,
                      private var mSoundUri: Uri?,
                      private val mCallback: KeywordManagerCallback,
                      private val mainActivity: MainActivity) : OnResultsReady {

    companion object {
        private const val TAG = "SpeechRecognizerManager"
        private const val KEYWORD_CALLED_WAIT_MS = 5000L
    }

    val speechRecognizerHelper = SpeechRecognizerHelper(mContext, true, this, languageTag)

    private val mRingtone = RingtoneManager.getRingtone(mContext, mSoundUri)
    private val mMediaPlayer = MediaPlayer.create(mContext, mSoundUri)
    private val mAudioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
    private val mSpeechTriggerClassifier = SpeechTriggerClassifier(mContext, languageTag, keyWord)

    private var mCanPlaySound = true
    private var mHasKeywordBeenCalled = false

    private val mKeywordHasBeenCalledHandler = Handler()
    private val mKeywordHasBeenCalledRunnable = Runnable {
        Log.d(TAG, "KEYWORD HAS BEEN CALLED: CANCELLED!")
        mHasKeywordBeenCalled = false
    }

    //This is the code for list classification
    private val orchestrator: ListClassificationOrchestrator_2 = ListClassificationOrchestrator_2()

    interface KeywordManagerCallback {
        fun onQuestionFound(filteredResult: FilteredResult)
    }

    init {
        if (mSoundUri == null) {
            mSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
    }

    fun startListeningForKeyword() {
        Log.d(TAG, "START LISTENING")
        speechRecognizerHelper.startListening()
    }

    fun stopListeningForKeyword() {
        Log.d(TAG, "STOP LISTENING")
        speechRecognizerHelper.cancelListening()
        speechRecognizerHelper.stopListening()
    }

    fun destroySpeechRecognizer() {
        speechRecognizerHelper.destroySpeechRecognizerObject()
    }

    fun setCanPlaySound(canPlaySound: Boolean) {
        mCanPlaySound = canPlaySound
    }

    @SuppressLint("NewApi")
    private fun playNotificationSound(canPlaySound: Boolean) {
        if(canPlaySound) {
            try {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val audioAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()

                    mRingtone?.audioAttributes = audioAttributes
                }

                mRingtone.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSpeechResults(results: ArrayList<String>?) {
        if (results != null && results.size > 0) {
            val filteredResult = mSpeechTriggerClassifier.getFilteredResult(results, mHasKeywordBeenCalled)
            val isSentenceNotEmpty = filteredResult.sentenceToEvaluate?.isNotEmpty() as Boolean
            val isSentenceOnlyKeyword = filteredResult.sentenceToEvaluate == SpeechTriggerClassifier.KEYWORD_ONLY_TRIGGER

            Log.d(TAG, "GOT HERE")
            Log.d(TAG, filteredResult.originalResult)

            if(isSentenceNotEmpty && !isSentenceOnlyKeyword){
                // Play notification sound cause sentence with a trigger has been found
                playNotificationSound(mCanPlaySound)
                // Get the question type

                mainActivity.resetEntities()

                val filteredPhrase = Phrase(filteredResult.originalResult)
                val success = orchestrator.classify(filteredPhrase)
                if (!success) {
                    Log.d(TAG, "Unsuccessful at parsing sentence")
                    // Start Listening again
                    speechRecognizerHelper.listenAgain(false)
                }
                else{
                    mainActivity.setFullPhrase(filteredResult.originalResult)
                    mainActivity.setQuestionPhrase(orchestrator.getQuestionPhrase())

                    var listEntities: Set<String> = orchestrator.getListEntities()
                    Log.d(TAG, listEntities.toString())
                    for (s in listEntities) {
                        val image = orchestrator.getImage(s)
                        mainActivity.addListEntity(image, s)
                    }
                }
                //filteredResult.questionType = QuestionClassifier.getQuestionType(mContext, livoxNowHelper, filteredResult)
                // Set has keyword been called to false to avoid any false positives
                mHasKeywordBeenCalled = false
                // Launch callback to send filtered results
                mCallback.onQuestionFound(filteredResult)

                //found a valid phrase stop listening
                stopListeningForKeyword();

            } else {
                Log.d(TAG, "ON SPEECH RESULTS LISTEN AGAIN")
                // Start Listening again
                speechRecognizerHelper.listenAgain(false)
                // Check if only keyword was spoken
                mHasKeywordBeenCalled = isSentenceOnlyKeyword

                if (mHasKeywordBeenCalled) {
                    // Play notification sound because only keyword has been found
                    playNotificationSound(mCanPlaySound)
                    // Listen for any questions for 5 seconds
                    mKeywordHasBeenCalledHandler.postDelayed(mKeywordHasBeenCalledRunnable, KEYWORD_CALLED_WAIT_MS)
                }
            }
        }
    }

    override fun onSpeechPartialResult(partialResult: String) {
        if (mHasKeywordBeenCalled) {
            mKeywordHasBeenCalledHandler.removeCallbacks(mKeywordHasBeenCalledRunnable)
            Log.d(TAG, "partial result")
        }
    }
}
