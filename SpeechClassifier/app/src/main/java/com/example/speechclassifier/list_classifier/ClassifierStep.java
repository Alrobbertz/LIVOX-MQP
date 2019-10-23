package com.example.speechclassifier.list_classifier;

public abstract class ClassifierStep {

	ListClassificationOrchestrator orchestrator;

	public ClassifierStep(ListClassificationOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}
	
	//sets internal state based on the given phrase
	public abstract boolean classify(Phrase phrase);
	
	//clears internal state
	public abstract void clear();

}
