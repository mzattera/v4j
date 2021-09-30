/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package io.github.mattera.v4j.text.alphabet;

/**
 * This is the abstract superclass for all alphabets that support IVTFF format.
 * These alphabets share metadata and markups, therefore a lot of non-regular characters are shared.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public abstract class IvtffAlphabet extends Alphabet {

	private static final char[] wordSeparators = { '.', ',' };

	@Override
	public char[] getWordSeparatorChars() {
		return wordSeparators;
	}

	private static char[] unreadableChars = { '?' };

	@Override
	public char[] getUnreadableChars() {
		return unreadableChars;
	}

	private static char[] allChars = null;
	
	@Override
	public char[] getAllChars() {
		if (allChars != null) return allChars;
		
		allChars = new char[getRegularChars().length + getWordSeparatorChars().length + getUnreadableChars().length + 11];

		int i = 0;
		char[] c = getRegularChars();
		for (int j=0; j < c.length; ++j, ++i)
			allChars[i] = c[j];
		c = getWordSeparatorChars();
		for (int j=0; j < c.length; ++j, ++i)
			allChars[i] = c[j];
		c = getUnreadableChars();
		for (int j=0; j < c.length; ++j, ++i)
			allChars[i] = c[j];

		// Special IVTFF characters
		allChars[i++] = '<';
		allChars[i++] = '>';
		allChars[i++] = '@';
		allChars[i++] = ';';
		allChars[i++] = '{';
		allChars[i++] = '}';
		allChars[i++] = '[';
		allChars[i++] = ':';
		allChars[i++] = ']';
		
		// These are UNDOCUMENTED in IVTFF format (only mentioned as 'Interlinear
		// Placeholders" in last page, probably because they conflict with v101 alphabet
		// TODO add a "filler" category? If you do, double check how you handle concordance and majority transcriptions.
		allChars[i++] = '!'; // sort of "null" character used to align up interlinear text so all lines from
								// different transcribers have same length
		allChars[i++] = '%'; // sort of filler/unreadable character used to align up interlinear text so all lines
								// from different transcribers have same length
		
		return allChars;
	}
	
	/**
	 * Split a word into prefix, infix and suffix.
	 * 
	 * It is recognized that there are patterns that tend to appear at the beginning or at the
	 * end of Voynich words. This method splits a word accordingly below rules.
	 * 
	 * <ul>
	 * <li> The prefix is a pattern found at the beginning of a word that matches the regular expression
	 * (Slot alphabet): [qsd]?[oy]?[lr]?
	 * <li> The suffix is a pattern found at the end of a word, once the prefix has been removed,
	 * that matches the regular expression (Slot alphabet): [oa]?[iJU]?[dlrmn]?[y]?
	 * <li> The infix is what remains after prefix and suffix has been removed from the word.
	 * </ul>
	 * 
	 * @param words A word written in this alphabet, to be split into its pre-, in- and suffix.
	 * 
	 * @return Three strings corresponding to the prefix, infix and suffix for given word.
	 */
	public abstract String[] getPreInSuffix(String word);
	
	/**
	 * @return Prefix for given word.
	 * @see #getPreInSuffix(String)
	 */
	public String getPrefix(String word) {
		return getPreInSuffix(word)[0];
	}
	
	/**
	 * @return Infix for given word.
	 * @see #getPreInSuffix(String)
	 */
	public String getInfix(String word) {
		return getPreInSuffix(word)[1];
	}
	
	/**
	 * @return Suffix for given word.
	 * @see #getPreInSuffix(String)
	 */
	public String getSuffix(String word) {
		return getPreInSuffix(word)[2];
	}
	
	protected IvtffAlphabet() {
	}
}