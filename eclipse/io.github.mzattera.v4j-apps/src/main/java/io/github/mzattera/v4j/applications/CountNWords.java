/**
 * 
 */
package io.github.mzattera.v4j.applications;

import java.util.Map.Entry;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.StringUtil;

/**
 * Takes given text and counts the occurrences of all sequences of N words.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountNWords {

	private CountNWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			
			Counter<String> c = process(doc, 2, true);

			System.out.println("Most repeated: " + c.getHighestCounted() + " = " + c.getHighestCount());
			
			for (Entry<String, Integer> e : c.reversed()) {
				System.out.println(e.getKey() + ": " + e.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * Take the plain text for the given Text and counts the occurrences of all
	 * sequences of n words.
	 * 
	 * @param regularOnly if true, consider only sequences of words that are all
	 *                    readable.
	 * 
	 * @return The count for all sequences of N words in the text.
	 */
	public static Counter<String> process(Text txt, int n, boolean readableOnly) {
		return process(txt.getPlainText(), txt.getAlphabet(), n, readableOnly);
	}

	/**
	 * Takes given text and counts the occurrences of all sequences of n words.
	 * Notice txt is supposed to be a plain (normalized) text that uses given
	 * Alphabet.
	 * 
	 * @param txt         a plain (normalized) text that uses given Alphabet.
	 * @param a           Alphabet ued in txt.
	 * @param regularOnly if true, consider only sequences of words that are all
	 *                    readable.
	 * 
	 * @return The count for all sequences of N words in the text.
	 */
	public static Counter<String> process(String txt, Alphabet a, int n, boolean readableOnly) {
		String space = a.getSpaceAsString();
		String[] words = StringUtil.splitWords(txt, a);
		Counter<String> result = new Counter<>(words.length);

		for (int i = 0; i <= words.length - n; ++i) {
			StringBuffer nw = new StringBuffer();
			for (int j = 0; j < n; ++j) {
				if (j > 0)
					nw.append(space);
				nw.append(words[i + j]);
			}

			String s = nw.toString();
			if (!readableOnly || !a.isUreadable(s))
				result.count(s);
		}

		return result;
	}
}
