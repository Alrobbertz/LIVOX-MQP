package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;
import java.util.List;

public class Phrase {

	private List<String> tokens; //tokenizes and preprocesses the phrase

	public static final ListPreprocessor preprocessor = new ListPreprocessor();

	public Phrase(String phrase) {
		this.tokens = getTokens(phrase);
	}

	private Phrase(List<String> words){
		tokens = words;
	}

	private List<String> getTokens(String str){
		List<String> tokens = new ArrayList<String>() {{
			String[] tokenArray = str.split(" ");
			for(int i = 0; i < tokenArray.length;i++) {
				add(tokenArray[i]);
			}
		}};
		return preprocessor.process(tokens);
	}

	public Phrase subPhrase(int start){
		List<String> sublist = new ArrayList<String>(){{
			for(int i = start; i < tokens.size(); i++)
				add(tokens.get(i));
		}};


		Phrase subPhrase = new Phrase(sublist);
		return subPhrase;
	}

	public Phrase subPhrase(int start, int end){
		List<String> sublist = new ArrayList<String>(){{
			for(int i = start; i < end; i++)
				add(tokens.get(i));
		}};


		Phrase subPhrase = new Phrase(sublist);
		return subPhrase;
	}

	public List<String> getWords(){
		return tokens;
	}

	public String getWord(int index){
		return tokens.get(index);
	}

	public int wordIndex(String word) {
		return tokens.indexOf(word);
	}

	public int phraseIndex(Phrase phrase) {
		for(int i = 0; i < tokens.size(); i++) {
			for(int j = 0; j < phrase.tokens.size() && i+j < tokens.size(); j++) {
				if(! tokens.get(i+j).equals(phrase.tokens.get(j)))
					break;
				if(j + 1 == phrase.tokens.size())
					return i;
			}
		}
		return -1;
	}

	public int size() {
		return tokens.size();
	}

	public String getSubphrase(int startIndex, int endIndex) {
		String phraseString = "";
		for(int i = startIndex; i <= endIndex; i++) {
			phraseString += getWord(i);
			if(i != endIndex)
				phraseString += " ";
		}
		return phraseString;
	}

	public String toString(){
		return getSubphrase(0, tokens.size() - 1);
	}
}
