package com.example.speechclassifier.list_classifier;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
/*
 * Following Amazon's process of parsing input data, the following
 * steps are used
 * - wakeword
 * - launch phrase
 * - invocation phrase (and associated intent)
 * - utterance phrase
 * Source: https://towardsdatascience.com/how-amazon-alexa-works-your-guide-to-natural-language-processing-ai-7506004709d3
 * 
 * The following phrase would be broken as follows following these steps
 *   John,   what do you want   to have for lunch?   hamburgers or chicken?
 * |- WW -| |---- launch ----| |--- invocation ---| |----- utterance -----|
 * 								  intent = lunch
 */
public class ListClassificationOrchestrator {
	
	//TODO each detector needs to take context from the previous. Make sure there are no overlaps in detection
	//TODO throw errors instead of returning specific values
	WakewordDetector wwdetector;
	LaunchDetector ldetector;
	InvocationDetector idetector;
	UtteranceDetector udetector;
	public Phrase recentPhrase;
	public int wwStart, lStart, iStart, uStart;

	public String TAG = "ListClassifier_V1";
	
	public ListClassificationOrchestrator() {
		wwdetector = new WakewordDetectorImpl("John");
		ldetector = new LaunchDetectorImpl();
		idetector = new InvocationDetectorImpl();
		udetector = new UtteranceDetectorOnline();
	}
	
	public boolean classify(Phrase phrase) {
		if(! wwdetector.classify(phrase))
			return false;
		Phrase wwPhrase = phrase.subPhrase(wwdetector.getWWIndex() + 1);
		wwStart = wwdetector.getWWIndex();

		if(! ldetector.classify(wwPhrase))
			return false;
		Phrase lPhrase = wwPhrase.subPhrase(ldetector.getEndIndex() + 1);
		lStart = wwStart + ldetector.getStartIndex() + 1;

		if(! idetector.classify(lPhrase))
			return false;
		Phrase iPhrase = lPhrase.subPhrase(idetector.getEndIndex() + 1);
		iStart = lStart - ldetector.getStartIndex() + 1 + ldetector.getEndIndex() + idetector.getStartIndex();

		if(! udetector.classify(iPhrase))
			return false;
		Phrase uPhrase = iPhrase.subPhrase(udetector.getEndIndex() + 1);
		uStart = iStart - idetector.getStartIndex() + 1 + idetector.getEndIndex() + udetector.getStartIndex();

		//if successfully parsed, store this as the most recent phrase
		recentPhrase = phrase;
		return true;
	}

	public String getWakeword() {
		String wakeword = recentPhrase.getWord(wwdetector.getWWIndex());
		return wakeword;
	}

	public String getLaunch() {
		int startIndex = ldetector.getStartIndex();
		int endIndex = ldetector.getEndIndex();
		return recentPhraseSubstring(lStart, lStart + endIndex - startIndex);
	}

	public String getInvocation() {
		int startIndex = idetector.getStartIndex();
		int endIndex = idetector.getEndIndex();
		return recentPhraseSubstring(iStart, iStart + endIndex - startIndex);
	}
	
	public String getIntent() {
		return idetector.getIntent();
	}

	public String getUtterance() {
		int startIndex = udetector.getStartIndex();
		int endIndex = udetector.getEndIndex();
		Log.d(TAG, startIndex + " " + endIndex + " " + uStart);
		return recentPhraseSubstring(uStart, uStart + endIndex - startIndex);
	}

	public List<String> getListEntities(){
		Log.d(TAG, "here");
		return udetector.getUtteranceList();
	}
	
	private String recentPhraseSubstring(int startIndex, int endIndex) {
		return recentPhrase.getSubphrase(startIndex, endIndex);
	}	
}
