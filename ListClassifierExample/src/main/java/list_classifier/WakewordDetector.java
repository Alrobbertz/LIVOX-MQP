package list_classifier;

import java.util.List;

public abstract class WakewordDetector extends ClassifierStep{
	
	public WakewordDetector(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		super(orchestrator, preprocessor);
	}
	
	public abstract int getWWIndex();

}
