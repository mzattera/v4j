package io.github.mattera.v4j.text.alphabet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * "Slot" alphabet based on "slot" theory.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class SlotAlphabet extends IvtffAlphabet {

	/**
	 * Possible classification of words, based on their structure.
	 */
	public enum TermClassification {
		REGULAR, // Can be decomposed in slots
		SEPARABLE, // Can be split in two parts, each longer then 1 char, that can be decomposed in
					// slots
		UNSTRUCTURED // all others
	}

	/**
	 * This describes how a word can be decomposed into "slots" and classified
	 * accordingly.
	 */
	// TODO use getters/setters?
	public static class TermDecomposition {

		/**
		 * How the term is classified, accordingly its decomposition into slots.
		 */
		public TermClassification classification = TermClassification.UNSTRUCTURED;

		/**
		 * The term described here.
		 */
		public String term = null;

		/**
		 * First part of term decomposition. For REGULAR terms, this correspond to the
		 * whole term. For SEPARABLE terms, this describes the first part of
		 * decomposition. For UNSTRUCTURED terms, this correspond the initial part of
		 * the term that has been decomposed.
		 */
		public String part1 = "";

		/**
		 * Decomposition of part1 into slots.
		 */
		public String[] slots1 = new String[SLOTS.size()];

		/**
		 * Second part of term decomposition. For REGULAR terms, this is empty. For
		 * SEPARABLE, this describes the second part of decomposition. For UNSTRUCTURED
		 * terms, this correspond the ending part where decomposition failed.
		 */
		public String part2 = "";

		/**
		 * Decomposition of part2 into slots.
		 */
		public String[] slots2 = new String[SLOTS.size()];

		TermDecomposition(String term) {
			this.term = term;
			slots1 = new String[SLOTS.size()];
			for (int i = 0; i < slots1.length; ++i)
				slots1[i] = "";
			slots2 = new String[SLOTS.size()];
			for (int i = 0; i < slots2.length; ++i)
				slots2[i] = "";
		}

		@Override
		public String toString() {
			return "TermDecomposition [term=" + term + ", classification=" + classification + ", part1=" + part1
					+ ", part2=" + part2 + "]";
		}
	}

	@Override
	public String getCodeString() {
		return "Slt-";
	}

	private final static char[] regularChars = { 'o', 'e', 'E', 'B', 'C', 'S', 'y', 'a', 'd', 'i', 'J', 'U', 'k', 'K',
			'l', 'r', 's', 't', 'T', 'n', 'q', 'p', 'P', 'm', 'f', 'F' };

	@Override
	public char[] getRegularChars() {
		return regularChars;
	}

	/*
	 * private static char[] allChars = null;
	 * 
	 * @Override public char[] getAllChars() { if (allChars != null) return
	 * allChars;
	 * 
	 * // We have a special char to denote an empty slot; we add it to the list of
	 * // IVTFF standard characters allChars = new char[super.getAllChars().length +
	 * 1]; int i = 0; for (; i < super.getAllChars().length; ++i) allChars[i] =
	 * super.getAllChars()[i]; allChars[i] = '=';
	 * 
	 * return allChars; }
	 */

	/**
	 * "Slots" for words; each element is a list of character combinations admitted
	 * in that slot.
	 */
	public final static List<List<String>> SLOTS = new ArrayList<List<String>>(12);

	static {
		//// 0 //////
		List<String> p = new ArrayList<String>();
		p.add("q");
		p.add("s");
		p.add("d");
		SLOTS.add(p);

		//// 1 //////
		p = new ArrayList<String>();
		p.add("o");
		p.add("y");
		SLOTS.add(p);

		//// 2 //////
		p = new ArrayList<String>();
		p.add("l");
		p.add("r");
		SLOTS.add(p);

		//// 3 //////
		p = new ArrayList<String>();
		p.add("k");
		p.add("p");
		p.add("t");
		p.add("f");
		SLOTS.add(p);

		//// 4 //////
		p = new ArrayList<String>();
		p.add("S");
		p.add("C");
		SLOTS.add(p);

		//// 5 //////
		p = new ArrayList<String>();
		p.add("K");
		p.add("P");
		p.add("T");
		p.add("F");
		SLOTS.add(p);

		//// 6 //////
		p = new ArrayList<String>();
		p.add("e");
		p.add("E");
		p.add("B");
		SLOTS.add(p);

		//// 7 //////
		p = new ArrayList<String>();
		p.add("s");
		p.add("d");
		p.add("K");
		p.add("k");
		p.add("T");
		p.add("t");
		p.add("P");
		p.add("p");
		p.add("F");
		p.add("f");
		// p.add("x");
		SLOTS.add(p);

		//// 8 //////
		p = new ArrayList<String>();
		p.add("o");
		p.add("a");
		SLOTS.add(p);

		//// 9 //////
		p = new ArrayList<String>();

		p.add("i");
		p.add("J");
		p.add("U");

		// p.add("V");
		// p.add("W");
		// p.add("Z");

		SLOTS.add(p);

		//// 10 //////
		p = new ArrayList<String>();
		p.add("d");
		p.add("l");
		p.add("r");
		p.add("m");
		p.add("n");
		SLOTS.add(p);

		//// 11 //////
		p = new ArrayList<String>();
		p.add("y");
		// p.add("g");
		SLOTS.add(p);
	}

	protected SlotAlphabet() {
	}

	/**
	 * Converts a text from Basic EVA alphabet. It only works for plain texts (see
	 * Text.getPlainText()).
	 * 
	 * @param txt Plain text to be converted.
	 */
	public static String fromEva(String txt) {
		for (char c : txt.toCharArray())
			if (!Alphabet.EVA.isRegularOrSeparator(c) && !Alphabet.EVA.isUreadableChar(c))
				throw new IllegalArgumentException("Text is not a plain EVA text.");

		// TODO add support for illegible words

		txt = txt.replace("ckh", "K");
		txt = txt.replace("cfh", "F");
		txt = txt.replace("cth", "T");
		txt = txt.replace("cph", "P");

		txt = txt.replace("ch", "C");
		txt = txt.replace("sh", "S");

		txt = txt.replace("c", "?");
		txt = txt.replace("h", "?");

		txt = txt.replace("eee", "B");
		txt = txt.replace("ee", "E");

		// txt = txt.replace("aiii","Z");
		// txt = txt.replace("aii", "W");
		// txt = txt.replace("ai", "V");

		txt = txt.replace("iii", "U");
		txt = txt.replace("ii", "J");

		txt = txt.replace("g", "?");
		txt = txt.replace("v", "?");
		txt = txt.replace("x", "?");
		txt = txt.replace("u", "?");
		txt = txt.replace("'", "?");

		// TODO test - REMOVEME
		for (char c : txt.toCharArray())
			if (!Alphabet.SLOT.isRegularOrSeparator(c) && !Alphabet.SLOT.isUreadableChar(c))
				throw new UnsupportedOperationException("Something went wrong in conversion");

		return txt;
	}

	/**
	 * Decompose a word into "slots".
	 * 
	 * @param term Term to decompose; currently does not support words with
	 *             unreadable characters.
	 * @return Two strings, the first is a decomposition of the word into slots, the
	 *         second is any remaining part of the word that could not be
	 *         successfully decomposed, or null.
	 */
	public static TermDecomposition decompose(String term) {

		TermDecomposition result = internalDecompose(term);

		if (result.classification != TermClassification.REGULAR) {
			if ((result.part1.length() > 1) && (result.part2.length() > 1)) {
				// Decompose the second part of the term
				TermDecomposition rest = internalDecompose(result.part2);
				switch (rest.classification) {
				case REGULAR:
					result.classification = TermClassification.SEPARABLE;
					result.slots2 = rest.slots1;
					result.part2 = rest.part1;
					break;
				case UNSTRUCTURED:
					result.classification = TermClassification.UNSTRUCTURED;
					break;
				default:
					throw new IllegalArgumentException();
				}
			}
		}

//		if (term.equals("qokeCy"))
//			System.out.println("COMPLETE");

		return result;
	}

	/**
	 * Decompose a word into "slots". This is an internal version that only
	 * decomposes the first part of the term, it does not try to decompose the
	 * reminder to assess if the term is a doublet.
	 * 
	 * @param term Term to decompose; currently does not support words with
	 *             unreadable characters.
	 */
	private static TermDecomposition internalDecompose(String term) {

		String s = term;

		// TODO add support for words with unreadable characters (and an "unreadable" flag to TermDecomposition).
		for (char c : s.toCharArray()) {
			if (Alphabet.SLOT.isUreadableChar(c))
				throw new IllegalArgumentException("Decomposition of unreadable texts not yet supported.");
			if (!Alphabet.SLOT.isRegularOrSeparator(c))
				throw new IllegalArgumentException("Term is not a plain SLOT text.");
		}

		TermDecomposition result = new TermDecomposition(term);

		for (int i = 0; (s != null) && (i < SLOTS.size()); ++i) {
			List<String> combinations = SLOTS.get(i);
			boolean found = false;
			for (int j = 0; (!found) && (s != null) && (j < combinations.size()); ++j) {
				String combo = combinations.get(j);

				if (s.startsWith(combo)) {
					result.slots1[i] = combo;
					result.part1 += combo;

					if (combo.length() == s.length())
						s = null;
					else
						s = s.substring(combo.length());

					found = true;
				}
			}
			if (!found) {
				result.slots1[i] = "";
			}
		}

		result.part2 = s;
		if (s == null) {
			result.classification = TermClassification.REGULAR;
		} else {
			result.classification = TermClassification.UNSTRUCTURED;
		}

		return result;
	}

	/**
	 * Decompose (and classify) all given terms, based whether they can be
	 * decomposed in slot.
	 * 
	 * @param terms Terms to classify.
	 */
	public static Map<String, TermDecomposition> decompose(Collection<String> terms) {

		Map<String, TermDecomposition> result = new HashMap<>(terms.size());
		for (String t : terms)
			result.put(t, decompose(t));

		return result;
	}
}
