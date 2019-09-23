package com.example.speechclassifier.list_classifier;

public class WakewordDetectorImpl extends WakewordDetector {

	
	private String wakeword;
	private boolean classified;
	private int index;
	
	public WakewordDetectorImpl(String wakeword, ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		super(orchestrator, preprocessor);
		this.wakeword = this.preprocessor.process(wakeword);
		classified = false;
		index = -1;
	}
	
	@Override
	public boolean classify(Phrase phrase) {
		clear();
		int index = phrase.wordIndex(wakeword);
		if(index >= 0) {
			this.classified = true;
			this.index = index;
			return true;
		}
		return false;
	}
	
	public int getWWIndex() {
		if (!classified) {
			return -1;//maybe throw an error
		}
		return index;
	}

	@Override
	public void clear() {
		classified = false;
		index = -1;
	}
}
