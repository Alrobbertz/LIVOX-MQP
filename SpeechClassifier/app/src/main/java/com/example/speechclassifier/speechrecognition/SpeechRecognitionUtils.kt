package com.example.speechclassifier.speechrecognition

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.util.Log

import java.io.File

/**
 * Created by Jason on 12/22/17.
 */

object SpeechRecognitionUtils {
    private const val TAG = "SpeechRecognitionUtils"
    private const val GOOGLE_APP_PKG_NAME = "com.google.android.googlequicksearchbox"

//    fun launchVoiceDownloadSettings(activity: Activity) {
//        val isPackageFound = PackageUtils.isPackageExisted(activity, GOOGLE_APP_PKG_NAME)
//
//        if (isPackageFound) {
//            val intent = Intent(Intent.ACTION_MAIN)
//            val components = arrayOf(
//                    ComponentName(GOOGLE_APP_PKG_NAME, "com.google.android.voicesearch.greco3.languagepack.InstallActivity"),
//                    ComponentName(GOOGLE_APP_PKG_NAME, "com.google.android.apps.gsa.settingsui.VoiceSearchPreferences"),
//                    ComponentName("com.google.android.voicesearch", "com.google.android.voicesearch.VoiceSearchPreferences"),
//                    ComponentName(GOOGLE_APP_PKG_NAME, "com.google.android.voicesearch.VoiceSearchPreferences"),
//                    ComponentName(GOOGLE_APP_PKG_NAME, "com.google.android.apps.gsa.velvet.ui.settings.VoiceSearchPreferences"))
//
//            for (componentName in components) {
//                try {
//                    intent.component = componentName
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    activity.startActivity(intent)
//                    Log.d(TAG, "COMPONENT NAME THAT WORKED: $componentName")
//                    break
//                } catch (e: Exception) {
//                    Log.e(TAG, e.message)
//                }
//
//            }
//        } else {
//            PackageUtils.launchPlayStore(activity, GOOGLE_APP_PKG_NAME)
//        }
//    }

    // TODO:// FIGURE OUT HOW TO CHECK IF THE USER DOWNLOADED THE LANGUAGE
    fun doesOfflineFolderExists(languageTag: String): Boolean {
        val dir = File("data/data/com.google.android.googlequicksearchbox/app_g3_models$languageTag/")
        val dirExists = dir.isDirectory
        Log.d(TAG, "IS OFFLINE VOICE FOLDER EXISTS: " + dirExists.toString())
        return true
    }

//    fun isNCKeywordEmpty(context: Context, userId: Int): Boolean {
//        val keyword = LivoxSettings.getNaturalConversationKeyword(context, userId)
//        return keyword.isEmpty()
//    }
//
//    fun getSpeechRecognizerIntent(context: Context, userId: Int, enableDictationMode: Boolean): Intent {
//        val languageTag: String = Languages.getLivoxVoiceIETFLanguageTag(context, userId)
//        return getSpeechRecognizerIntent(context, languageTag, enableDictationMode)
//    }

    fun getSpeechRecognizerIntent(context: Context, languageTag: String, enableDictationMode: Boolean): Intent {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)

        if(enableDictationMode) {
            speechRecognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true)
        }

        return speechRecognizerIntent
    }
}
