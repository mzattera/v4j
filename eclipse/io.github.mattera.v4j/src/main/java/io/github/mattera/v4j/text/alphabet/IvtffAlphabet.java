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
	
	protected IvtffAlphabet() {
	}
}