/**
 * 
 */
package io.github.mzattera.v4j.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.utils.Lists;

import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * Set of string processing Util
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class StringUtil {

	private StringUtil() {
	}

	/**
	 * Return true if s is null or has only spaces.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
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
	 * Notice empty "words" between spaces are removed.
	 */
	public static String[] splitWords(String txt, Alphabet a) {
	
		// Make sure no empty words are returned
		String[] t = txt.split(Pattern.quote(a.getSpaceAsString()));
		List<String> l = new ArrayList<>(t.length);
		for (int i=0; i<t.length; ++i) 
			if (t[i].length() > 0)
				l.add(t[i]);
		
		return l.toArray(new String[0]);
	}

	/**
	 * Shuffle words in given plain text.
	 * 
	 * @param txt A plain text in the given alphabet.
	 * @return The text with words randomly shuffled.
	 */
	public static String shuffledWords(String txt, Alphabet a) {
		return shuffledWords(txt, a, new Random(System.currentTimeMillis()));
	}

	/**
	 * Shuffle words in given plain text.
	 * 
	 * @param txt A plain text in the given alphabet.
	 * @return The text with words randomly shuffled.
	 */
	public static String shuffledWords(String txt, Alphabet a, Random rnd) {
		List<String> words = Arrays.asList(StringUtil.splitWords(txt, a));
		Collections.shuffle(words, rnd);
		return String.join(a.getSpaceAsString(), words);
	}

	/**
	 * Extract a substring of given length, taken from a random position. Notice
	 * this does not starts at words boundaries.
	 * 
	 * @param txt Source text.
	 * @param len Length of the random sample to take.
	 *
	 * @return a substring of given length, taken from a random position.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static String randomSubstring(String txt, int len) {
		return randomSubstring(txt, len, new Random(System.currentTimeMillis()));
	}

	/**
	 * Extract a substring of given length, taken from a random position. Notice
	 * this does not starts at words boundaries.
	 * 
	 * @param txt Source text.
	 * @param len Length of the random sample to take.
	 * @param rnd Random number generator.
	 *
	 * @return a substring of given length, taken from a random position.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static String randomSubstring(String txt, int len, Random rnd) {
		if (len >= txt.length())
			throw new IllegalArgumentException();

		int pos = rnd.nextInt(txt.length() - len);

		return txt.substring(pos, pos + len);
	}

	/**
	 * Remove character at given position in a string.
	 */
	public static String removeCharAt(String s, int pos) {
		StringBuilder sb = new StringBuilder(s);
		sb.deleteCharAt(pos);
		return sb.toString();
	}

	/**
	 * @return Count of chars in a string.
	 */
	public static Counter<Character> countChars(String txt) {
		Counter<Character> result = new Counter<>();
		for (int i = 0; i < txt.length(); ++i)
			result.count(txt.charAt(i));
		return result;
	}

	/**
	 * Performs replacements described in given Map over txt. The replacement is
	 * such that a string in Map is a substring of another string in Map, only the
	 * longer replacement is used. In addition, any piece of the original text that
	 * is not replaced, is replace later by mask.
	 * 
	 * NOTICE the order in which replacements of same length are applied, could
	 * still made some of them invalid.
	 * 
	 * @param mask         A character used to mask chars not replaced in txt.
	 * @param replacements A map from substrings in txt to their replacement.
	 * @return
	 */
	public static String smartReplace(String txt, Map<String, String> replacements, char mask) {

		boolean[] masked = new boolean[txt.length()]; // true if corresponding char in txt has already been replaced
		String[] replaced = new String[txt.length()]; // records what we replaced at each position.

		// Strings to replace, from longest
		List<String> keys = new ArrayList<>(replacements.keySet());
		keys.sort(new Comparator<>() {

			@Override
			public int compare(String o1, String o2) {
				return -Integer.compare(o1.length(), o2.length());
			}
		});

		for (String key : keys) {
			Pattern p = Pattern.compile(Pattern.quote(key));
			Matcher m = p.matcher(txt);
			while (m.find()) { // found a match
				int pos = m.start();

				// Check if this part has been replaced already (so the match should not happen)
				boolean b = false;
				for (int i = pos; !b && i < m.end(); ++i)
					b = masked[i];
				if (b)
					continue;

				replaced[pos] = replacements.get(key);
				masked[pos] = true;

				// mark other positions as being replaced already
				for (int i = pos + 1; i < m.end(); ++i) {
					replaced[i] = "";
					masked[i] = true;
				}
			}
		}

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < replaced.length; ++i) {
			if (replaced[i] == null)
				result.append(mask);
			else
				result.append((replaced[i]));
		}

		return result.toString();
	}
}
