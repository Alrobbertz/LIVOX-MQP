package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public class ListPreprocessor {
	
	public ListPreprocessor() {}
	
	public List<String> process(List<String> tokens) {
		ArrayList<String> processedTokens = new ArrayList<String>();
		for(String s: tokens) {
			processedTokens.add(process(s));
		}
		return processedTokens;
	}
	
	public String process(String token) {
		token = token.toLowerCase();
		return token;
	}
	
}
