package list_classifier;

import java.util.ArrayList;
import java.util.List;

public class Phrase {

	private String phrase;//stores the original phrase
	private List<String> tokens; //tokenizes and preprocesses the phrase

	public Phrase(String phrase) {
		this.phrase = phrase;
		this.tokens = getTokens(phrase);
	}

	public Phrase(String phrase, ListPreprocessor preprocessor) {
		this(phrase);
		setPreprocessor(preprocessor);
	}

	private List<String> getTokens(String str){
		return new ArrayList<String>() {{
			String[] tokenArray = phrase.split(" ");
			for(int i = 0; i < tokenArray.length;i++) {
				add(tokenArray[i]);
			}
		}};
	}

	public void setPreprocessor(ListPreprocessor preprocessor) {
		if(preprocessor != null) {
			List<String> tokens =  getTokens(this.phrase);
			tokens = preprocessor.process(tokens);
			this.tokens = tokens;
		}
	}

	public String getPhrase() {
		return phrase;
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
}
