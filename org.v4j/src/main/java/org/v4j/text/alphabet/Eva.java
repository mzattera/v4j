/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package org.v4j.text.alphabet;

/**
 * The EVA script alphabet.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
// TODO rename to EVA or EVA extended based on what we really support
public final class Eva extends Alphabet {

	@Override
	public String getCodeString() {
		return "Eva-";
	}

	private static final char[] regularChars;
	static {
		// TODO verify these are all and only EVA chars
		regularChars = new char['z' - 'a'];
		int i = 0;
		for (char c = 'a'; c <= 'z'; ++c)
			if (c != 'w')
				regularChars[i++] = c;
	}

	@Override
	public char[] getRegularChars() {
		return regularChars;
	}

	private static final char[] allChars;
	static {
		allChars = new char[regularChars.length + 14];

		int i = 0;
		for (; i < regularChars.length; ++i)
			allChars[i] = regularChars[i];

		allChars[i++] = '.';
		allChars[i++] = ',';
		allChars[i++] = '<';
		allChars[i++] = '>';
		allChars[i++] = '@';
		allChars[i++] = ';';
		allChars[i++] = '{';
		allChars[i++] = '}';
		allChars[i++] = '[';
		allChars[i++] = ':';
		allChars[i++] = ']';
		allChars[i++] = '?';

		// These are UNDOCUMENTED in IVTFF format (only mentioned as 'Interlinear
		// Placeholders" in last page, probably because they conflict with v101 alphabet
		allChars[i++] = '!'; // sort of "null" character used to align up interlinear text so all lines from
								// different transcribers have same length
		allChars[i++] = '%'; // sort of unreadable character used to align up interlinear text so all lines
								// from different transcribers have same length
	}

	@Override
	public char[] getAllChars() {
		return allChars;
	}

	private static final char[] wordSeparators = { '.', ',' };

	@Override
	public char[] getWordSeparatorChars() {
		return wordSeparators;
	}

	private static final char[] unreadableChars = { '?' };

	@Override
	public char[] getUnreadableChars() {
		return unreadableChars;
	}

	protected Eva() {
	}
}