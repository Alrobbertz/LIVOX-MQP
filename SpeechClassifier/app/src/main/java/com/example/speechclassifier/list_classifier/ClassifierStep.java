package com.example.speechclassifier.list_classifier;

public abstract class ClassifierStep {

	public ClassifierStep() {}
	
	//sets internal state based on the given phrase
	public abstract boolean classify(Phrase phrase);
	
	//clears internal state
	public abstract void clear();

}
