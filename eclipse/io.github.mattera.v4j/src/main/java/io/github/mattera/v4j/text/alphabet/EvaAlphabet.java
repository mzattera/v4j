/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package io.github.mattera.v4j.text.alphabet;

/**
 * The EVA script alphabet.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
// TODO rename to EVA or EVA extended based on what we really support
public final class EvaAlphabet extends Alphabet {

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

	private static final char[] allChars;
	static {
		allChars = new char[regularChars.length + wordSeparators.length + unreadableChars.length + 11];

		int i = 0;
		for (int j=0; j < regularChars.length; ++j, ++i)
			allChars[i] = regularChars[j];
		for (int j=0; j < wordSeparators.length; ++j, ++i)
			allChars[i] = wordSeparators[j];
		for (int j=0; j < unreadableChars.length; ++j, ++i)
			allChars[i] = unreadableChars[j];

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
		allChars[i++] = '!'; // sort of "null" character used to align up interlinear text so all lines from
								// different transcribers have same length
		allChars[i++] = '%'; // sort of unreadable character used to align up interlinear text so all lines
								// from different transcribers have same length
	}
	
	protected EvaAlphabet() {
	}
}