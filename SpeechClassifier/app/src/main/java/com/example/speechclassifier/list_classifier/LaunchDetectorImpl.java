package com.example.speechclassifier.list_classifier;

import java.util.ArrayList;

public class LaunchDetectorImpl extends LaunchDetector {
	//initialize the launchPhrases
		private String[] launchStrings  = {
				"what do you want"
		};
		private ArrayList<Phrase> launchPhrases;
		private void setupLaunchPhrases() {
			launchPhrases = new ArrayList<Phrase>() {{
				for(int i = 0; i < launchStrings.length; i++)
					add(new Phrase(launchStrings[i], preprocessor));
			}};
		}

		//private attributes
		private boolean classified;
		private int startIndex, endIndex; //both inclusive

		public LaunchDetectorImpl(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
			super(orchestrator, preprocessor);
			classified = false;
			startIndex = -1;
			endIndex = -1;
			setupLaunchPhrases();
		}

		@Override
		public boolean classify(Phrase phrase) {
			clear();
			for(Phrase lp: launchPhrases) {
				int index = phrase.phraseIndex(lp);
				if( index >= 0) {
					classified = true;
					startIndex = index;
					endIndex = index + lp.size() - 1;
					return true;
				}
			}
			return false;
		}

		public int getStartIndex() {
			if(!classified)
				return -1;
			return startIndex;
		}

		public int getEndIndex() {
			if(!classified)
				return -1;
			return endIndex;
		}

		@Override
		public void clear() {
			classified = false;
			startIndex = -1;
			endIndex = -1;
		}
}