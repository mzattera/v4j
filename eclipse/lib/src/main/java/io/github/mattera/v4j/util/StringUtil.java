/**
 * 
 */
package io.github.mattera.v4j.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * Set of string processing Util
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class StringUtil {

	/**
	 * Return true if s is null or has only spaces.
	 * 
	 * @param s
	 * @return
	 */
	public final static boolean isEmpty(String s) {
		if (s == null)
			return true;
		else
			return s.trim().equals("");
	}

	/**
	 * 
	 * @return numbers of characters that strings have in common at same positions.
	 */
	public static int countMatchingChars(String s1, String s2) {
		int t = 0;
		for (int i = 0; i < Math.min(s1.length(), s2.length()); ++i)
			if (s1.charAt(i) == s2.charAt(i))
				++t;
		return t;
	}

	/**
	 * Split a string by using default space char for the given alphabet.
	 * 
	 * @param txt a plain text in the given alphabet.
	 * @return result of splitting txt around a.getSpace().
	 */
	public static String[] splitWords(String txt, Alphabet a) {
		return txt.split(Pattern.quote(a.getSpaceAsString()));
	}

	/**
	 * Shuffle words in given plain text.
	 * 
	 * @param txt A plain text in the given alphabet.
	 * @return The text with words randomly shuffled.
	 */
	public static String shuffleWords(String txt, Alphabet a) {
		return shuffleWords(txt, a, new Random(System.currentTimeMillis()));
	}

	/**
	 * Shuffle words in given plain text.
	 * 
	 * @param txt A plain text in the given alphabet.
	 * @return The text with words randomly shuffled.
	 */
	public static String shuffleWords(String txt, Alphabet a, Random rnd) {
		List<String> words = Arrays.asList(StringUtil.splitWords(txt, a));
		Collections.shuffle(words, rnd);
		return String.join(a.getSpaceAsString(), words);
	}
}
