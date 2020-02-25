package list_classifier;

import java.util.ArrayList;
import java.util.List;

public abstract class LaunchDetector extends ClassifierStep{

	public LaunchDetector(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		super(orchestrator, preprocessor);
	}

	public abstract int getStartIndex();

	public abstract int getEndIndex();

}
