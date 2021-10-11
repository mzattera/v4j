/**
 * 
 */
package io.github.mzattera.v4j.text.alphabet;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents the Java char set (all unicode Basic Multilingual Plane (BMP)
 * characters that can be represented by a Java char).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class JavaCharset extends Alphabet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getCodeString()
	 */
	@Override
	public String getCodeString() {
		return "Java_UTF-16";
	}

	private char[] allChars = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getAllChars()
	 */
	@Override
	public char[] getAllChars() {
		if (allChars != null)
			return allChars;

		// Create a list of all UNICODE characters that can be represented as Java char
		// (UTF-16)
		List<Character> l = new ArrayList<>();
		for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
			if (Character.isDefined(i))
				l.add((char) i);
		}

		allChars = new char[l.size()];
		for (int i = 0; i < l.size(); ++i)
			allChars[i] = l.get(i);

		return allChars;
	}

	private char[] regularChars = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getRegularChars()
	 */
	@Override
	public char[] getRegularChars() {
		if (regularChars != null)
			return regularChars;

		// Create a list of all characters that are letters or numbers
		List<Character> l = new ArrayList<>();
		for (char c : getAllChars()) {
			if (Character.isAlphabetic(c))
				l.add(c);
		}

		regularChars = new char[l.size()];
		for (int i = 0; i < l.size(); ++i)
			regularChars[i] = l.get(i);

		return regularChars;
	}

	private char[] wordSeparatorChars = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getWordSeparatorChars()
	 */
	@Override
	public char[] getWordSeparatorChars() {
		if (wordSeparatorChars != null)
			return wordSeparatorChars;

		// Create a list of all characters that are letters or numbers
		List<Character> l = new ArrayList<>();
		for (char c : getAllChars()) {
			int t = Character.getType(c);
			if (Character.isWhitespace(c) || t == Character.CONNECTOR_PUNCTUATION || t == Character.DASH_PUNCTUATION
					|| t == Character.END_PUNCTUATION || t == Character.FINAL_QUOTE_PUNCTUATION
					|| t == Character.INITIAL_QUOTE_PUNCTUATION || t == Character.OTHER_PUNCTUATION
					|| t == Character.START_PUNCTUATION)
				l.add(c);
		}

		wordSeparatorChars = new char[l.size()];
		for (int i = 0; i < l.size(); ++i)
			wordSeparatorChars[i] = l.get(i);

		return wordSeparatorChars;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getSpace()
	 */
	@Override
	public char getSpace() {
		return ' ';
	}

	private final char[] unreadableChars = { '\0' };

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.text.alphabet.Alphabet#getUnreadableChars()
	 */
	@Override
	public char[] getUnreadableChars() {
		return unreadableChars;
	}

	@Override
	public String toUpperCase(String txt) {
		return txt.toUpperCase();
	}

	@Override
	public String toLowerCase(String txt) {
		return txt.toLowerCase();
	}
}
