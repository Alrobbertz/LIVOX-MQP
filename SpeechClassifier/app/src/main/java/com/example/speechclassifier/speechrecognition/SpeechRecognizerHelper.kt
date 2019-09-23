package com.example.speechclassifier.speechrecognition

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log


/**
 * Created by Jason on 11/28/17.
 */

class SpeechRecognizerHelper(private val mContext: Context,
                             isListeningRepeating: Boolean,
                             onResultsReady: OnResultsReady,
                             private val mLanguageTag: String) : SpeechRecognitionListener.SpeechRecListenerCallback {
    companion object {
        private const val TAG = "SpeechRecognitionManage"
        private const val START_LISTENING_DELAY = 500L
        private const val FREEZE_TIME_MS = 300000L
    }

    private val mHandler = Handler()

    private var mSpeechRecognizer: SpeechRecognizer? = null
    private val mSpeechRecognizerIntent: Intent? = SpeechRecognitionUtils.getSpeechRecognizerIntent(mContext, mLanguageTag, true)

    private val mSpeechRecognitionListener: SpeechRecognitionListener =
            SpeechRecognitionListener(isListeningRepeating, onResultsReady, this)

    private val mFreezeCountDownTimer = object : CountDownTimer(FREEZE_TIME_MS, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "Freeze countdown: $millisUntilFinished")
        }

        override fun onFinish() {
            Log.d(TAG, "FREEZE RESET")
            listenAgain(true)
        }
    }

    private var isListening = false

    init {
        initSpeechRecognizer()
    }

    private fun initSpeechRecognizer() {
        val isRecognizerAvailable = SpeechRecognizer.isRecognitionAvailable(mContext)
        if(isRecognizerAvailable) {
            // Create Speech Recognizer Instance
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext)
            Log.d(TAG, "initialized speech recognizer")
        } else {
            Log.d(TAG, "SPEECH RECOGNIZER IS NOT AVAILABLE")
        }

        if(mSpeechRecognizer != null) {
            Log.d(TAG, "added speech recognizer callback")
            mSpeechRecognizer?.setRecognitionListener(mSpeechRecognitionListener)
        }
    }

    fun startListening() {
        Log.d(TAG, "startListening")
        if(!isListening) {
            mHandler.postDelayed({
                if (mSpeechRecognizer == null) {
                    initSpeechRecognizer()
                }

                mSpeechRecognizer?.startListening(mSpeechRecognizerIntent)
            }, START_LISTENING_DELAY)
        }
    }

    fun stopListening() {
        mFreezeCountDownTimer.cancel()

        if (mSpeechRecognizer != null) {
            isListening = false
            mSpeechRecognizer?.stopListening()
        }
    }

    fun cancelListening() {
        if (mSpeechRecognizer != null) {
            isListening = false
            mSpeechRecognizer?.cancel()
        }
    }

    fun destroySpeechRecognizerObject() {
        if (mSpeechRecognizer != null) {
            isListening = false
            mFreezeCountDownTimer.cancel()
            mSpeechRecognizer?.destroy()
            mSpeechRecognizer = null
        }
    }

    override fun listenAgain(onError: Boolean) {
        Log.d(TAG, "LISTEN AGAIN")
        mFreezeCountDownTimer.cancel()

        if (onError) {
            destroySpeechRecognizerObject()
        }

        startListening()
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "On NOT FROZEN")
        mFreezeCountDownTimer.cancel()
    }

    override fun onReadyForSpeech() {
        mFreezeCountDownTimer.start()
        isListening = true
    }

    override fun onEndOfSpeech() {
        isListening = false
    }
}
