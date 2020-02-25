package list_classifier;

import java.util.ArrayList;
import java.util.List;

public abstract class UtteranceDetector extends ClassifierStep{


	public UtteranceDetector(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		super(orchestrator, preprocessor);
	}

	public abstract int getStartIndex();

	public abstract int getEndIndex();
	
	public abstract List<String> getUtteranceList();

}
