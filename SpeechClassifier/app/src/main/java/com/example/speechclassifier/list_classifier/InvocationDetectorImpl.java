package com.example.speechclassifier.list_classifier;

import java.util.HashMap;
import java.util.Map.Entry;

public class InvocationDetectorImpl extends InvocationDetector {
	/*
	 * the key to an invocation is that it maps to a specific intent
	 * the intent should be the 'category' of the question aka dinner
	 */
	private HashMap<Phrase, String> invocationPhrases;
	public void setupInvocationPhrases() {
		invocationPhrases = new HashMap<Phrase, String>() {{
			put(new Phrase("to have"), "item");
			put(new Phrase("to have for lunch"), "lunch");
			put(new Phrase("for lunch"), "lunch");
			put(new Phrase("to eat for lunch"), "lunch");
		}};
	}

	//private attributes
	private boolean classified;
	private int startIndex, endIndex; //both inclusive
	private String intent;

	public InvocationDetectorImpl(ListClassificationOrchestrator orchestrator) {
		super(orchestrator);
		classified = false;
		startIndex = -1;
		endIndex = -1;
		setupInvocationPhrases();
	}

	@Override
	public void clear() {
		classified = false;
		startIndex = -1;
		endIndex = -1;
		intent = null;
	}

	@Override
	public boolean classify(Phrase phrase) {
		clear();
		for(Entry<Phrase, String> entry: invocationPhrases.entrySet()) {
			Phrase entryPhrase = entry.getKey();
			int index = phrase.phraseIndex(entryPhrase);
			if( index >= 0) {
				classified = true;
				startIndex = index;
				endIndex = index + entryPhrase.size() - 1;
				intent = entry.getValue();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getStartIndex() {
		if(!classified)
			return -1;
		return startIndex;
	}

	@Override
	public int getEndIndex() {
		if(!classified)
			return -1;
		return endIndex;
	}

	@Override
	public String getIntent() {
		if(!classified)
			return null;
		return intent;
	}
}

