package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public abstract class LaunchDetector extends ClassifierStep{

	public LaunchDetector(ListClassificationOrchestrator orchestrator) {
		super(orchestrator);
	}

	public abstract int getStartIndex();

	public abstract int getEndIndex();

}
