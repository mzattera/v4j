package io.github.mattera.v4j.text.alphabet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.ParseException;

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
		 * Decomposition of part2 into slots, with the exception of UNSTRUCTURED terms,
		 * where this is always empty.
		 */
		public String[] slots2 = new String[SLOTS.size()];

		private TermDecomposition(String term) {
			this.term = term;
			slots1 = new String[SLOTS.size()];
			for (int i = 0; i < slots1.length; ++i)
				slots1[i] = "";
			slots2 = new String[SLOTS.size()];
			for (int i = 0; i < slots2.length; ++i)
				slots2[i] = "";
		}

		private String prefix = null;

		/**
		 * Prefix for this term
		 * 
		 * @return Content of slots 0-2 for the first part of this term decomposition.
		 */
		public String getPrefix() {
			if (prefix == null) {
				StringBuffer result = new StringBuffer();
				for (int i = 0; i < 3; ++i)
					result.append(slots1[i]);
				prefix = result.toString();
			}

			return prefix;
		}

		private String suffix = null;

		/**
		 * Suffix for this term
		 * 
		 * @return For REGULAR terms, returns content of of slots 8-11 for the first
		 *         part of this term decomposition. For SEPARABLE terms, returns content
		 *         of of slots 8-11 for the second part of this term decomposition. For
		 *         UNSTRUCTURED terms returns empty string.
		 */
		public String getSuffix() {
			if (suffix == null) {
				StringBuffer result = new StringBuffer();
				switch (classification) {
				case REGULAR:
					for (int i = 8; i < slots1.length; ++i)
						result.append(slots1[i]);
					suffix = result.toString();
					break;
				case SEPARABLE:
					for (int i = 8; i < slots2.length; ++i)
						result.append(slots2[i]);
					suffix = result.toString();
					break;
				case UNSTRUCTURED:
					suffix = "";
					break;
				default:
					throw new IllegalArgumentException("Unrecognized term classification: " + classification);

				}
			}

			return suffix;
		}

		/**
		 * Root for this term
		 * 
		 * @return The part of the term that it is not its prefix or suffix..
		 */
		public String getRoot() {
			return term.substring(getPrefix().length(), term.length() - getSuffix().length());
		}

		@Override
		public String toString() {
			return "TermDecomposition [term=" + term + ", classification=" + classification + ", part1=" + part1
					+ ", part2=" + part2 + "]";
		}
	}

	@Override
	public String getCodeString() {
		return "Slot";
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
	 * Converts a text from Basic EVA alphabet.
	 * 
	 * @param txt text to be converted.
	 * @throws ParseException if text is not proper IVTFF text.
	 */
	public static String fromEva(String txt) throws ParseException {

		// plant intrusion is replaced by a space
		// TODO verify
		txt = txt.replaceAll("<\\->", Alphabet.SLOT.getSpaceAsString());

		// Remove comments as they might interfere with replacement
		txt = IvtffLine.removeComments(txt);

		// These might impact how unreadable characters are handled
		txt = txt.replaceAll("!", ""); // "null" char in interlinear
		txt = txt.replaceAll("%", Alphabet.SLOT.getUnreadableAsString());

		// WHen transliterating we use 0 to denote an EVA character that transliterates
		// into a dubious Slot character; this happen because multiple EVA transliterate
		// into a single Slot.

		txt = txt.replace("ckh", "K");
		txt = txt.replace("cfh", "F");
		txt = txt.replace("cth", "T");
		txt = txt.replace("cph", "P");

		// kh and ck are not single chars in SLOT.
		// We replace with ??? so we can revert back to EVA and be aligned
		txt = txt.replace("?kh", "?00");
		txt = txt.replace("?fh", "?00");
		txt = txt.replace("?th", "?00");
		txt = txt.replace("?ph", "?00");
		txt = txt.replace("c?h", "0?0");
		txt = txt.replace("ck?", "00?");
		txt = txt.replace("cf?", "00?");
		txt = txt.replace("ct?", "00?");
		txt = txt.replace("cp?", "00?");

		// These might be K or ?k?
		txt = txt.replace("?k?", "?0?");
		txt = txt.replace("?f?", "?0?");
		txt = txt.replace("?t?", "?0?");
		txt = txt.replace("?p?", "?0?");

		txt = txt.replace("ch", "C");
		txt = txt.replace("sh", "S");

		// These might be S or s?
		txt = txt.replace("s?", "0?");

		txt = txt.replace("c", "0");
		txt = txt.replace("h", "0");

		txt = txt.replace("eee", "B");
		// Can be B or E?
		txt = txt.replace("?ee", "?00");
		txt = txt.replace("ee?", "00?");

		txt = txt.replace("ee", "E");
		// Can be B or E? or e??
		txt = txt.replace("e??", "0??");
		txt = txt.replace("?e?", "?0?");
		txt = txt.replace("??e", "??0");
		txt = txt.replace("e?e", "0?0");
		txt = txt.replace("?e", "?0");
		txt = txt.replace("e?", "0?");

		txt = txt.replace("iii", "U");
		// Can be U or J?
		txt = txt.replace("?ii", "?00");
		txt = txt.replace("ii?", "00?");

		txt = txt.replace("ii", "J");
		// Can be U or J? or i??
		txt = txt.replace("i??", "0??");
		txt = txt.replace("?i?", "?0?");
		txt = txt.replace("??i", "??0");
		txt = txt.replace("i?i", "0?0");
		txt = txt.replace("?i", "?0");
		txt = txt.replace("i?", "0?");

		txt = txt.replace("g", "?");
		txt = txt.replace("v", "?");
		txt = txt.replace("x", "?");
		txt = txt.replace("u", "?");
		txt = txt.replace("j", "?");
		txt = txt.replace("b", "?");
		txt = txt.replace("z", "?");
		txt = txt.replace("'", "?");

		txt = txt.replace("0", "?");

		return txt;
	}

	/**
	 * Decompose a word into "slots".
	 * 
	 * @param term Term to decompose, in Alphabet.SLOT alphabet; currently this does
	 *             not support words with unreadable characters.
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

		// TODO add support for words with unreadable characters (and an "unreadable"
		// flag to TermDecomposition).
		// Notice that fromEva() might introduce multiple ? where a single Slot
		// character
		// might be present. When decomposing a sequence of ? can occupy one or more
		// slots.
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
