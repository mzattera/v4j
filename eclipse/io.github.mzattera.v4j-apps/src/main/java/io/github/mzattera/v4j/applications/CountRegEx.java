/* Copyright (c) 2020-2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.applications;

import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Counts all the matches of a given regular expression (regex). It then prints
 * a list of the matches found together with their count.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountRegEx {

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
//	public static final Alphabet ALPHABET = Alphabet.EVA;
	public static final Alphabet ALPHABET = Alphabet.SLOT;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = new PageFilter.Builder().cluster("SB").build();

	// The RegEx to look for.
	
	// Words with rare characters
//	private final static String REGEX = "\\.[dqs]?[oy]?l(ch|sh)";
//	private final static String REGEX = "\\.[dqs]?[oy]?l(ch|sh)[^\\.]*\\.";
//	private final static String REGEX = "\\.[dqs]?[oy]?ra[^\\.]*\\.";
//	private final static String REGEX = "\\.[^\\.]*ly\\.";
//	private final static String REGEX = "a[^\\.]*\\.";
//	private final static String REGEX = "[^\\.]*[ktpfd]a[^\\.iJUnmlr]*\\.";
	private final static String REGEX = "(a[iJU]?[mnlr]|d?y)\\.";
	
	// Total rare characters
//	private final static String REGEX = "[gxvujbz]";


	private CountRegEx() {
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
			System.out.println("RegEx             : " + REGEX);
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			System.out.println("Tokens (all)      : " + doc.getWords(false).getTotalCounted());
			System.out.println("Terms (all)       : " + doc.getWords(false).size());
			System.out.println("Tokens (readable) : " + doc.getWords(true).getTotalCounted());
			System.out.println("Terms (readable)  : " + doc.getWords(true).size());
			System.out.println();

			Counter<String> c = process("." + doc.getPlainText() + ".", REGEX);

			for (Entry<String, Integer> e : c.reversed()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	public static Counter<String> process(String s, String regex) {
		Counter<String> result = new Counter<>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while (m.find()) {
			result.count(m.group());
		}

		return result;
	}
}
