package com.example.speechclassifier.speechrecognition

import android.annotation.SuppressLint
import android.content.Context
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
import java.util.*
import com.example.speechclassifier.list_classifier.ListClassificationOrchestrator
import com.example.speechclassifier.list_classifier.Phrase
import java.net.URL


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
    private val orchestrator: ListClassificationOrchestrator = ListClassificationOrchestrator();

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
                // TODO Integrage here with Cole's Code

                val filteredPhrase = Phrase(filteredResult.originalResult)
                val success = orchestrator.classify(filteredPhrase)
                if (!success) {
                    Log.d(TAG, "Unsuccessful at parsing sentence")
                }
                else{
                    mainActivity.setFullPhrase(filteredPhrase.phrase)
                    mainActivity.setInitiatorPhrase(orchestrator.launch)
                    mainActivity.setInvocationPhrase(orchestrator.invocation)
                    mainActivity.setListEntityPhrase(orchestrator.utterance)

                    //TODO add images
                    var listEntities: List<String> = orchestrator.listEntities;
                    Log.d(TAG, listEntities.toString());
                    if(listEntities.size == 2){
                        var baseURL = "https://storage.googleapis.com/livox-images/full/"
                        //TODO Rich, add the queries to the database
                        //TODO This should take in the list entity and give back the image name in the database
                        mainActivity.setListEntityImage1(baseURL + "hot_dogs.png")//TODO replace with the image name
                        mainActivity.setListEntityText1(listEntities[0])
                        mainActivity.setListEntityImage2(baseURL + "symbol00000997.png")//TODO replace with the image name
                        mainActivity.setListEntityText2(listEntities[1])
                    }
                }
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
            Log.d(TAG, "partial result")
        }
    }
}
