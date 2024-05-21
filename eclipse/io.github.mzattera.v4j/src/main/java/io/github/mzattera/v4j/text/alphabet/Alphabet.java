/* Copyright (c) 2018-2022 Massimiliano "Maxi" Zattera */

/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package io.github.mzattera.v4j.text.alphabet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines an alphabet of symbols used in a text.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public abstract class Alphabet {

	/** The Basic EVA alphabet */
	public final static EvaAlphabet EVA = new EvaAlphabet();

	/** The Java charset */
	public final static JavaCharset UTF_16 = new JavaCharset();

	/** The Slot alphabet (see https://mzattera.github.io/v4j/005/) */
	public final static SlotAlphabet SLOT = new SlotAlphabet();

	// All available alphabets, by code
	private static final Map<String, Alphabet> ALPHABETS = new HashMap<>();
	static {
		ALPHABETS.put(EVA.getCodeString(), EVA);
		ALPHABETS.put(UTF_16.getCodeString(), UTF_16);
		ALPHABETS.put(SLOT.getCodeString(), SLOT);
	}

	/**
	 * @param codeString a string code for this alphabet, same used in IVTFF file
	 *                   header for the alphabet.
	 * @return The Alphabet with given code, or null if it cannot be found.
	 */
	public static Alphabet getAlphabet(String codeString) {
		return ALPHABETS.get(codeString);
	}

	/**
	 * @return All available alphabets.
	 */
	public static Collection<Alphabet> getAlphabets() {
		return ALPHABETS.values();
	}

	/**
	 * @return a string code for this alphabet, same used in IVTFF file header for
	 *         the alphabet.
	 */
	public abstract String getCodeString();

	/**
	 * @return all valid chars in the alphabet (that is, all chars that can appear
	 *         in texts using this alphabet). This include regular and special
	 *         characters.
	 */
	public abstract char[] getAllChars();

	/**
	 * @return true if c is not a "valid" char, that is a character not in the
	 *         alphabet.
	 */
	public boolean isInvalid(char c) {
		char[] chars = getAllChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return false;

		return true;
	}

	/**
	 * @return true if the string contains an invalid char.
	 */
	public boolean hasInvalid(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isInvalid(s.charAt(i)))
				return true;

		return false;
	}

	/**
	 * @return all regular chars in the alphabet. Regular chars are those forming
	 *         words in text written with this alphabet.
	 */
	public abstract char[] getRegularChars();

	/**
	 * @return true if c is a "regular" char.
	 */
	public boolean isRegular(char c) {
		char[] chars = getRegularChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return true;

		return false;
	}

	/**
	 * @return true if the string contains only regular chars.
	 */
	public boolean hasOnlyRegular(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (!isRegular(s.charAt(i)))
				return false;

		return true;
	}

	/**
	 * @return all special chars in the alphabet. Special chars are valid characters
	 *         which are not regular characters. These are typically used to
	 *         indicate comments, spaces, etc. Notice that word separators are
	 *         special characters.
	 */
	public char[] getSpecialChars() {
		List<Character> result = new ArrayList<>();
		char[] all = getAllChars();
		for (int i = 0; i < all.length; ++i) {
			if (!isRegular(all[i]))
				result.add(all[i]);
		}

		char[] r = new char[result.size()];
		int i = 0;
		for (char c : result)
			r[i++] = c;

		return r;
	}

	/**
	 * @return true if c is a special char.
	 */
	public boolean isSpecialChar(char c) {
		char[] chars = getSpecialChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return true;

		return false;
	}

	/**
	 * @return true if the string contains a special char.
	 */
	public boolean hasSpecialChars(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isSpecialChar(s.charAt(i)))
				return true;

		return false;
	}

	/**
	 * @return all word separators chars in the alphabet. By default, the first one
	 *         is the one returned by getSpace(). Word separators are special
	 *         characters used to separate words in the text.
	 */
	public abstract char[] getWordSeparatorChars();

	/**
	 * @return "default" character to be used as space.
	 */
	public char getSpace() {
		return getWordSeparatorChars()[0];
	}

	/**
	 * 
	 * @return space character as a string of lenght 1. This is because in some
	 *         methods a string parameter is required.
	 */
	public String getSpaceAsString() {
		return Character.toString(getSpace());
	}

	/**
	 * @return true if c is a word separator char.
	 */
	public boolean isWordSeparator(char c) {
		char[] chars = getWordSeparatorChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return true;

		return false;
	}

	/**
	 * @return true if the string contains a word separator char.
	 */
	public boolean hasWordSeparator(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isWordSeparator(s.charAt(i)))
				return true;

		return false;
	}

	/**
	 * 
	 * @eturn true if the char is regular or word separator.
	 */
	public boolean isRegularOrSeparator(char c) {
		return isRegular(c) || isWordSeparator(c);
	}

	/**
	 * 
	 * @return the input string after removing all chars that are not regular
	 *         characters or word separators and replacing any sequence of one or
	 *         more word separators with a single Alphabeth.getSpace() char. Notice
	 *         that the algorithm works at single character level.
	 */
	public String toPlainText(String txt) {
		if ((txt == null) || (txt.length() == 0))
			return txt;

		StringBuilder result = new StringBuilder();
		boolean spaced = true;
		for (int i = 0; i < txt.length(); ++i) {
			char c = txt.charAt(i);
			if (isWordSeparator(c) && !spaced) {
				result.append(getSpace());
				spaced = true;
			} else if (isRegular(c) || isUreadableChar(c)) {
				result.append(c);
				spaced = false;
			}
		}

		if ((result.length() > 0) && isWordSeparator(result.charAt(result.length() - 1))) {
			// trim
			return result.toString().substring(0, result.length() - 1);
		}

		return result.toString();
	}

	/**
	 * 
	 * @return a list of special characters that can be used to mark one unreadable
	 *         character in the text. By default, the first character is the one
	 *         returned by getUnreadableChar().
	 */
	public abstract char[] getUnreadableChars();

	/**
	 * @return a "default" character to be used to mark one unreadable character.
	 */
	public char getUnreadable() {
		return getUnreadableChars()[0];
	}

	/**
	 * @return unreadable character as a string, this because some methods need
	 *         string parameters.
	 */
	public String getUnreadableAsString() {
		return Character.toString(getUnreadable());
	}

	/**
	 * @return true if c is a used to indicate a "unreadable" char.
	 */
	public boolean isUreadableChar(char c) {
		char[] chars = getUnreadableChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return true;

		return false;
	}

	/**
	 * @return true if the string contains one or more unreadable char.
	 */
	public boolean isUnreadable(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isUreadableChar(s.charAt(i)))
				return true;

		return false;
	}

	/**
	 * 
	 * @param c
	 * @return a "normalized" version of c, where all word separators are replaced
	 *         by getSpace() and all unreadable chars by getUnreadable().
	 */
	public char asPlain(char c) {
		return (isWordSeparator(c) ? getSpace() : (isUreadableChar(c) ? getUnreadable() : c));
	}

	/**
	 * @return upper case version of given text. Text is assumed to be using this
	 *         Alphabet. Notice that not all alphabets support this method.
	 * 
	 * @exception UnsupportedOperationException if the alphabet does not support
	 *                                          this.
	 */
	public String toUpperCase(String txt) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return lower case version of given text. Text is assumed to be using this
	 *         Alphabet. Notice that not all alphabets support this method.
	 */
	public String toLowerCase(String txt) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "Alphabet [" + getCodeString() + "]";
	}
}