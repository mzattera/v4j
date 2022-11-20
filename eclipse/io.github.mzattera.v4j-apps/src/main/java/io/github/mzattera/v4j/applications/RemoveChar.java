package io.github.mzattera.v4j.applications;

import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.StringUtil;

/**
 * This code shows how, due to the "slot" nature of Voynichese, removing a
 * character in a word leads to another valid word.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class RemoveChar {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/**
	 * Which Alphabet type to use.
	 */
	public static final Alphabet ALPHABET = Alphabet.SLOT;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	private RemoveChar() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Alphabet          : " + ALPHABET);
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			Counter<String> words = doc.getWords(true);
			System.out.println("Tokens (readable) : " + doc.getWords(true).getTotalCounted());
			System.out.println("Terms (readable)  : " + doc.getWords(true).size());
			System.out.println();

			System.out.println("=== Any Word");

			// Test the hypothesys by removing any character
			int c = 0;
			int tot = 0;
			for (Entry<String, Integer> e : words.entrySet()) {
				String term = e.getKey();
				if (term.length() < 2)
					continue;
				for (int i = 0; i < term.length(); ++i) {
					String t2 = StringUtil.removeCharAt(term, i);
					if (words.itemSet().contains(t2))
						++c;
					++tot;
				}
			}
			System.out.println("Removing any character from a word leads to a valid word in " + ((double) c * 100 / tot)
					+ "% of cases");

			// Test the hypothesys by removing first character
			c = 0;
			tot = 0;
			for (Entry<String, Integer> e : words.entrySet()) {
				String term = e.getKey();
				if (term.length() < 2)
					continue;
				String t2 = StringUtil.removeCharAt(term, 0);
				if (words.itemSet().contains(t2))
					++c;
				++tot;
			}
			System.out.println("Removing first character from a word leads to a valid word in "
					+ ((double) c * 100 / tot) + "% of cases");

			System.out.println("=== Words appearing at least 5 times in text");

			// Test the hypothesys by removing any character
			c = 0;
			tot = 0;
			for (Entry<String, Integer> e : words.entrySet()) {
				String term = e.getKey();
				if (term.length() < 2)
					continue;
				if (words.getCount(term) < 5)
					continue;
				for (int i = 0; i < term.length(); ++i) {
					String t2 = StringUtil.removeCharAt(term, i);
					if (words.itemSet().contains(t2))
						++c;
					++tot;
				}
			}
			System.out.println("Removing any character from a word leads to a valid word in " + ((double) c * 100 / tot)
					+ "% of cases");

			// Test the hypothesys by removing first character
			c = 0;
			tot = 0;
			for (Entry<String, Integer> e : words.entrySet()) {
				String term = e.getKey();
				if (term.length() < 2)
					continue;
				if (words.getCount(term) < 5)
					continue;
				String t2 = StringUtil.removeCharAt(term, 0);
				if (words.itemSet().contains(t2))
					++c;
				++tot;
			}
			System.out.println("Removing first character from a word leads to a valid word in "
					+ ((double) c * 100 / tot) + "% of cases");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}
}
