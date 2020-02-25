package list_classifier;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ListClassificationOrchestratorTest {

	ListClassificationOrchestrator orchestrator = new ListClassificationOrchestrator();
	Phrase phrase = new Phrase("John what do you want to have for lunch hamburgers or chicken");
	
	@Before
	public void classifierPassesTest() {
		assertTrue(orchestrator.classify(phrase));
	}

	@Test
	public void wakewordTest() {
		assertEquals(phrase.getSubphrase(0, 0), orchestrator.getWakeword());
	}
	
	@Test
	public void launchTest() {
		assertEquals(phrase.getSubphrase(1, 4), orchestrator.getLaunch());
	}
	
	@Test
	public void invocationTest() {
		assertEquals(phrase.getSubphrase(5, 8), orchestrator.getInvocation());
		assertEquals("lunch", orchestrator.getIntent());
	}
	
	@Test
	public void utteranceTest() {
		assertEquals(phrase.getSubphrase(9, 11), orchestrator.getUtterance());
	}
}
