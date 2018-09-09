/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package org.v4j.document.alphabet;

/**
 * 
 * The ASCII alphabet. TODO: double check and refine.
 * 
 * @author maxi
 */
public final class Ascii extends Alphabet {

	@Override
	public String getCodeString() {
		return "ASCII";
	}

	private static final char[] allChars;
	static {
		allChars = new char[256];
		for (char c = 0; c < allChars.length; ++c)
			allChars[c] = c;
	}

	@Override
	public char[] getAllChars() {
		return allChars;
	}

	private static final char[] regularChars;
	static {
		// TODO add accented letters, even if for our tests they might not be needed
		regularChars = new char[('Z' - 'A' + 1) * 2 + 10];
		int i = 0;
		for (char c = 'a'; c <= 'z'; ++c)
			regularChars[i++] = c;
		for (char c = 'A'; c <= 'Z'; ++c)
			regularChars[i++] = c;
		for (char c = '0'; c <= '9'; ++c)
			regularChars[i++] = c;
	}

	@Override
	public char[] getRegularChars() {
		return regularChars;
	}

	private static final char[] wordSeparators = { ' ', '.', ',', '-', ';', '\n' };

	@Override
	public char[] getWordSeparatorChars() {
		return wordSeparators;
	}

	private static final char[] unreadableChars = { '\0' };

	@Override
	public char[] getUnreadableChars() {
		return unreadableChars;
	}

	protected Ascii() {
	}
}