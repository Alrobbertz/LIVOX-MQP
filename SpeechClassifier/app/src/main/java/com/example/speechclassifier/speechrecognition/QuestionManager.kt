package com.example.speechclassifier.speechrecognition

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import java.util.*

/**
 * Created by Jason on 11/28/17.
 */

class QuestionManager(keyWord: String,
                      languageTag: String,
                      private val mContext: Context,
                      private var mSoundUri: Uri?,
                      private val mCallback: KeywordManagerCallback) : OnResultsReady {

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

            if(isSentenceNotEmpty && !isSentenceOnlyKeyword){
                // Play notification sound cause sentence with a trigger has been found
                playNotificationSound(mCanPlaySound)
                // Get the question type
                // TODO Integrage here with Cole's Code
                //filteredResult.questionType = QuestionClassifier.getQuestionType(mContext, livoxNowHelper, filteredResult)
                // Set has keyword been called to false to avoid any false positives
                mHasKeywordBeenCalled = false
                // Launch callback to send filtered results
                mCallback.onQuestionFound(filteredResult)
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
        }
    }
}
