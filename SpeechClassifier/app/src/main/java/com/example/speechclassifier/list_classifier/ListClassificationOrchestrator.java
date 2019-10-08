package com.example.speechclassifier.list_classifier;

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
	ListPreprocessor preprocessor;
	WakewordDetector wwdetector;
	LaunchDetector ldetector;
	InvocationDetector idetector;
	UtteranceDetector udetector;
	Phrase recentPhrase;
	
	public ListClassificationOrchestrator() {
		preprocessor = new ListPreprocessor();
		wwdetector = new WakewordDetectorImpl("John", this, preprocessor);
		ldetector = new LaunchDetectorImpl(this, preprocessor);
		idetector = new InvocationDetectorImpl(this, preprocessor);
		udetector = new UtteranceDetectorImpl(this, preprocessor);
	}
	
	public boolean classify(Phrase phrase) {
		//make sure the preprocessor is correct
		phrase.setPreprocessor(preprocessor);
		
		if(! wwdetector.classify(phrase))
			return false;
		if(! ldetector.classify(phrase))
			return false;
		if(! idetector.classify(phrase))
			return false;
		if(! udetector.classify(phrase))
			return false;
		
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
		return recentPhraseSubstring(startIndex, endIndex);
	}

	public String getInvocation() {
		int startIndex = idetector.getStartIndex();
		int endIndex = idetector.getEndIndex();
		return recentPhraseSubstring(startIndex, endIndex);
	}
	
	public String getIntent() {
		return idetector.getIntent();
	}

	public String getUtterance() {
		int startIndex = udetector.getStartIndex();
		int endIndex = udetector.getEndIndex();
		return recentPhraseSubstring(startIndex, endIndex);
	}

	public List<String> getListEntities(){
		return udetector.getUtteranceList();
	}
	
	private String recentPhraseSubstring(int startIndex, int endIndex) {
		return recentPhrase.getSubphrase(startIndex, endIndex);
	}	
}
