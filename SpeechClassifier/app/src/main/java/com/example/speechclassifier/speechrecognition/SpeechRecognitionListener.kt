package com.example.speechclassifier.speechrecognition

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.*

/**
 * Created by Jason on 11/28/17.
 */

class SpeechRecognitionListener(private val isListeningRepeating: Boolean,
                                private val mListener: OnResultsReady?,
                                private val mCallback: SpeechRecListenerCallback) : RecognitionListener {

    interface SpeechRecListenerCallback {
        fun listenAgain(onError: Boolean)
        fun onBeginningOfSpeech()
        fun onReadyForSpeech()
        fun onEndOfSpeech()
    }

    companion object {
        private const val TAG = "SpeechRecognitionListen"
    }

    override fun onReadyForSpeech(bundle: Bundle) {
        Log.d(TAG, "READY FOR SPEECH")
        mCallback.onReadyForSpeech()
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "BEGINNING OF SPEECH")
        mCallback.onBeginningOfSpeech()
    }

    override fun onRmsChanged(v: Float) {}

    override fun onBufferReceived(bytes: ByteArray) {}

    override fun onEndOfSpeech() {
        Log.d(TAG, "END OF SPEECH")
        mCallback.onEndOfSpeech()
    }

    override fun onError(error: Int) {
        var isBusy = false
        var isListeningAgain = true

        Log.d(TAG, "ERROR CODE: $error")
        when (error) {
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                Log.e(TAG, "ERROR RECOGNIZER BUSY")
                isBusy = true
            }

            SpeechRecognizer.ERROR_NO_MATCH -> Log.e(TAG, "NO MATCH")

            SpeechRecognizer.ERROR_NETWORK -> Log.e(TAG, "Error Network STOPPED LISTENING")

            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.e(TAG, "ERROR NETWORK TIMEOUT")

            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                Log.e(TAG, "Error Speech Timeout")
                isBusy = true
            }

            SpeechRecognizer.ERROR_AUDIO -> Log.e(TAG, "ERROR AUDIO")

            SpeechRecognizer.ERROR_CLIENT -> {
                Log.e(TAG, "ERROR CLIENT")
                isListeningAgain = false
            }

            SpeechRecognizer.ERROR_SERVER -> Log.e(TAG, "ERROR SERVER")
        }

        if (isListeningRepeating && isListeningAgain) {
            Log.d(TAG, "ON ERROR LISTEN AGAIN")
            mCallback.listenAgain(isBusy)
        }
    }

    override fun onResults(bundle: Bundle?) {
        if (bundle != null && mListener != null) {
            val results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val newResults = ArrayList<String>()
            if (results != null) {
                for (result in results) {
                    val resultAllLowerCase = result.replace("?", "").toLowerCase()
                    newResults.add(resultAllLowerCase)
                    Log.d(TAG, "R: $resultAllLowerCase")
                }
            }

            mListener.onSpeechResults(newResults)
        }
    }

    override fun onPartialResults(bundle: Bundle) {
        val results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (results != null && mListener != null) {
            val partialResult = results[0]
            Log.d(TAG, "PARTIAL R: $partialResult")

            mListener.onSpeechPartialResult(partialResult)
        }
    }

    override fun onEvent(i: Int, bundle: Bundle) {
        Log.d(TAG, "ON EVENT")
    }
}
