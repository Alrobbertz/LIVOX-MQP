package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class InvocationDetector extends ClassifierStep{

	public InvocationDetector() {}

	public abstract int getStartIndex();

	public abstract int getEndIndex();

	public abstract String getIntent();
}