/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.util.Counter;

/**
 * @author maxi
 * 
 */
public final class GetWordsTest {

	@Test
	@DisplayName("Text.getWords(boolean) and therefore CountWords work")
	public void doTest() throws Exception {
		String in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1>\n"
				+ "<f1r.1,@P0;H>       fachyy.ykal,a!!ataiin,shol,sho?y.fachyy.,shory,ct%%%ys.ct%%%ys.y.kor.sholdy!\n";

		IvtffText doc = new IvtffText(in.toString());

		List<String> words = new ArrayList<String>();
		words.add("fachyy");
		words.add("ykal");
		words.add("aataiin");
		words.add("shol");
		words.add("shory");
		words.add("kor");
		words.add("y");
		words.add("sholdy");

		List<String> wordsUnreadable = new ArrayList<String>();
		wordsUnreadable.addAll(words);
		wordsUnreadable.add("sho?y");
		wordsUnreadable.add("ct???ys");

		// Gets all words
		Counter<String> m = doc.getWords(false);
		assertEquals(m.size(), wordsUnreadable.size());
		for (String w : wordsUnreadable) {
			assertTrue(m.itemSet().contains(w));
			if (w.equals("fachyy") || w.equals("ct???ys")) {
				assertEquals(m.getCount(w), 2);
			} else {
				assertEquals(m.getCount(w), 1);
			}
		}

		// Gets onle readable words
		m = doc.getWords(true);
		assertEquals(m.size(), words.size());
		for (String w : words) {
			assertTrue(m.itemSet().contains(w));
			if (w.equals("fachyy") || w.equals("ct???ys")) {
				assertEquals(m.getCount(w), 2);
			} else {
				assertEquals(m.getCount(w), 1);
			}
		}
	}
}