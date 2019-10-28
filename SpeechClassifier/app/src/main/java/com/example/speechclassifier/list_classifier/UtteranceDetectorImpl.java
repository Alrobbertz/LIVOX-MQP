package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public class UtteranceDetectorImpl extends UtteranceDetector {
	//private attributes
	private boolean classified;
	private Phrase recentPhrase;
	private int startIndex, endIndex; //both inclusive

	public UtteranceDetectorImpl() {
		classified = false;
		recentPhrase = null;
		startIndex = -1;
		endIndex = -1;
	}

	@Override
	public void clear() {
		classified = false;
		recentPhrase = null;
		startIndex = -1;
		endIndex = -1;
	}

	@Override
	public boolean classify(Phrase phrase) {
		clear();
		int index = phrase.wordIndex("or");
		if(index != phrase.size() - 1) {
			classified = true;
			startIndex = index - 1;
			endIndex = index + 1;
			recentPhrase = phrase;
			return true;
		}
		return false;
	}

	public int getStartIndex() {
		if(!classified)
			return -1;
		return startIndex;
	}

	public int getEndIndex() {
		if(!classified)
			return -1;
		return endIndex;
	}

	public List<String> getUtteranceList(){
		ArrayList<String> utteranceList = new ArrayList<String>();
		for(int i = startIndex; i<= endIndex; i++) {
			utteranceList.add(recentPhrase.getWord(i));//TODO double check this still works
		}
		utteranceList.remove(1);//remove the or
		return utteranceList;
	}
}
