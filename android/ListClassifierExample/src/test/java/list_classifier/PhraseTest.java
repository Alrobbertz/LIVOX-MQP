package list_classifier;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import list_classifier.Phrase;

public class PhraseTest {

	Phrase p1 = new Phrase("Hello John how are you");
	Phrase p2 = new Phrase("how are you");
	Phrase p3 = new Phrase("how are you doing");
			
	@Test
	public void getPhrase() {
		assertEquals("Hello John how are you", p1.getPhrase());
		List<String> words = p1.getWords();
		assertEquals(5, words.size());
		assertEquals("Hello", words.get(0));
		assertEquals("John", words.get(1));
		assertEquals("how", words.get(2));
		assertEquals("are", words.get(3));
		assertEquals("you", words.get(4));
	}
	
	@Test
	public void wordIndex() {
		assertEquals(1, p1.wordIndex("John"));
		assertEquals(-1, p1.wordIndex("shoe"));
	}
	
	@Test
	public void phraseIndex() {
		assertEquals(2, p1.phraseIndex(p2));
		assertEquals(-1, p1.phraseIndex(p3));
	}
	
	

}
