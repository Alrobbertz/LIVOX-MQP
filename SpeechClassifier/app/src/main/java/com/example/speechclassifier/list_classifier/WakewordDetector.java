package com.example.speechclassifier.list_classifier;

import java.util.List;

public abstract class WakewordDetector extends ClassifierStep{
	
	public WakewordDetector() {
	}
	
	public abstract int getWWIndex();

}
