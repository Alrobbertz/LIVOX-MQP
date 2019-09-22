package list_classifier;

public abstract class ClassifierStep {

	ListClassificationOrchestrator orchestrator;
	ListPreprocessor preprocessor;
	
	public ClassifierStep(ListClassificationOrchestrator orchestrator, ListPreprocessor preprocessor) {
		this.orchestrator = orchestrator;
		this.preprocessor = preprocessor;
	}
	
	//sets internal state based on the given phrase
	public abstract boolean classify(Phrase phrase);
	
	//clears internal state
	public abstract void clear();

}
