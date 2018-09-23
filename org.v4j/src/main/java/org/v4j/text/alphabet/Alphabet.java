/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package org.v4j.text.alphabet;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines an alphabet of symbols used in a text.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public abstract class Alphabet {
	
	public final static Alphabet EVA = new Eva();

	public final static Alphabet ASCII = new Ascii();

	/**
	 * @return a string code for this alphabet, same as that used in the IVTFF file.
	 */
	public abstract String getCodeString();

	/**
	 * @return all valid chars in the alphabet (that is, all chars that can appear
	 * in texts using this alphabet).
	 * This include regular and special characters.
	 */
	public abstract char[] getAllChars();

	/**
	 * @return true if c is not a "valid" char, that is a character not in the
	 * alphabet.
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
	 * @return all regular chars in the alphabet.
	 * Regular chars are those forming words in text written with this alphabet.
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
	 * @return all special chars in the alphabet.
	 * Special chars are valid characters
	 * which are not regular characters. These are typically
	 * used to indicate comments, spaces, etc.
	 * Notice that word separators are special characters.
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
	// TODO rename
	public boolean isDiacritic(char c) {
		char[] chars = getSpecialChars();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c)
				return true;
	
		return false;
	}

	/**
	 * @return true if the string contains a special char.
	 */
	// TODO rename
	public boolean hasDiacritic(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isDiacritic(s.charAt(i)))
				return true;
	
		return false;
	}

	/**
	 * @return all word separators chars in the alphabet. By default, the first one
	 * is the one returned by getSpace().
	 * Word separators are special characters used to separate words in the text.
	 */
	public abstract char[] getWordSeparatorChars();

	/**
	 * @return a character that can be used as space.
	 */
	public char getSpace() {
		return getWordSeparatorChars()[0];
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
	public boolean isregularOrSeparator(char c) {
		return isRegular(c) || isWordSeparator(c);
	}

	/**
	 * 
	 * @return true if the string contains only 1) regular chars 2) word separators 3) unreadable chars.
	 */
	public boolean isPlain(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (!isRegular(s.charAt(i)) && !isWordSeparator(s.charAt(i)) && !isUreadableChar(s.charAt(i)))
				return false;

		return true;
	}

	/**
	 * 
	 * @return a list of special characters that can be used to mark one unreadable
	 * character in the text. By default, the first character is the one returned by
	 * getUnreadableChar().
	 */
	public abstract char[] getUnreadableChars();

	/**
	 * @return a character that can be used to mark one unreadable character.
	 */
	public char getUnreadable() {
		return getUnreadableChars()[0];
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
	public boolean isUreadable(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (isUreadableChar(s.charAt(i)))
				return true;

		return false;
	}
}