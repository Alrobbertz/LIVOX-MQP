package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public class UtteranceDetectorImpl extends UtteranceDetector {
	//private attributes
	private boolean classified;
	private int startIndex, endIndex; //both inclusive

	public UtteranceDetectorImpl(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		super(orchestrator, preprocessor);
		classified = false;
		startIndex = -1;
		endIndex = -1;
	}

	@Override
	public void clear() {
		classified = false;
		startIndex = -1;
		endIndex = -1;
	}

	@Override
	public boolean classify(Phrase phrase) {
		clear();
		String or = preprocessor.process("or");//honestly don't know if we need this.
		int index = phrase.wordIndex(or);
		if(index != phrase.size() - 1) {
			classified = true;
			startIndex = index - 1;
			endIndex = index + 1;
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
			utteranceList.add(orchestrator.recentPhrase.getWord(i));
		}
		return utteranceList;
	}
}
