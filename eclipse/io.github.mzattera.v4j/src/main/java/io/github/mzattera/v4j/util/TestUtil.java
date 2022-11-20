/**
 * 
 */
package io.github.mzattera.v4j.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map.Entry;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffText;

/**
 * Utility method to implement JUnit tests.
 * 
 * @author Massimilino "Maxi" Zattera
 *
 */
public final class TestUtil {

	private TestUtil() {
	}

	/**
	 * Checks given Text has exactly same lines of text lines provided in test.
	 */
	public static void testMatch(IvtffText text, String[][] test) {
		List<IvtffLine> lines = text.getLines();
		assertEquals(lines.size(), test.length);
		for (int i = 0; i < test.length; ++i)
			testMatch(lines.get(i), test[i]);
	}

	/**
	 * Checks given Text has exactly text provided in test.
	 */
	public static void testMatch(Text text, String[] test) {
		String[] s = text.splitWords();
		assertEquals(s.length, test.length);
		for (int i = 0; i < test.length; ++i)
			assertEquals(s[i], test[i]);
	}

	/**
	 * Checks given Text contains the same words (in any order) than given array
	 */
	public static void testWordCount(Text text, String[] test) {
		testWordCount(new Counter<>(test), test);
	}

	/**
	 * Checks the words listed in test match the count in cnt.
	 */
	public static void testWordCount(Counter<String> cnt, String[] test) {
		Counter<String> words = new Counter<>(test);
		assertEquals(cnt.getTotalCounted(), words.getTotalCounted());
		assertEquals(cnt.itemSet().size(), words.itemSet().size());
		for (Entry<String, Integer> e : cnt.entrySet()) {
			assertEquals(e.getValue(), words.getCount(e.getKey()), "Unmatched count for " + e.getKey());
		}
	}

	/**
	 * Checks the words listed in test match the count in cnt and outputs a verbose
	 * debug message.
	 */
	public static void testWordCountVerbose(Counter<String> cnt, String[] test) {
		boolean ok = true;

		Counter<String> words = new Counter<>(test);
		if (cnt.getTotalCounted() != words.getTotalCounted()) {
			System.out.println("!!! Counter<> counted " + cnt.getTotalCounted() + " items but String[] contains "
					+ words.getTotalCounted());
			ok = false;
		}
		if (cnt.itemSet().size() != words.itemSet().size()) {
			System.out.println("!!! Counter<> itemset is  " + cnt.itemSet().size() + " but String[] itemset is "
					+ words.itemSet().size());
			ok = false;
		}
		System.out.println("\tCounter<>\tString[]");
		for (Entry<String, Integer> e : cnt.entrySet()) {
			int c = words.getCount(e.getKey());
			if (e.getValue() != c) {
				System.out.println(e.getKey() + "\t" + e.getValue() + "\t\t" + c + " <- !!!");
				ok = false;
			}
		}
		for (Entry<String, Integer> e : words.entrySet()) {
			int c = cnt.getCount(e.getKey());
			if (c == 0) {
				System.out.println(e.getKey() + "\t" + c + "\t\t" + e.getValue() + " <- !!!");
				ok = false;
			}
		}

		assertTrue(ok);
	}
}
