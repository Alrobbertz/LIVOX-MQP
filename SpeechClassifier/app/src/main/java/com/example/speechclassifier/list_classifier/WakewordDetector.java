package com.example.speechclassifier.list_classifier;

import java.util.List;

public abstract class WakewordDetector extends ClassifierStep{
	
	public WakewordDetector(ListClassificationOrchestrator orchestrator) {
		super(orchestrator);
	}
	
	public abstract int getWWIndex();

}
