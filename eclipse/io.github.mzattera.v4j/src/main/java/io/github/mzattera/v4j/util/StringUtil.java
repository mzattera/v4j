/**
 * 
 */
package io.github.mzattera.v4j.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.txt.TextString;

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
	 */
	public static String[] splitWords(String txt, Alphabet a) {
		return txt.split(Pattern.quote(a.getSpaceAsString()));
	}

	/**
	 * Shuffle words in given <code>Text</code> plain text.
	 * 
	 * @return The text with words randomly shuffled.
	 */
	public static String shuffleWords(Text txt) {
		return shuffleWords(txt.getPlainText(), txt.getAlphabet(), new Random(System.currentTimeMillis()));
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

	/**
	 * Extract a random piece of text.
	 * 
	 * @param txt Source text.
	 * @param len Length of the random sample to take.
	 *
	 * @return A piece of plain text from <code>txt</code> of given length.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static String extractRandom(Text txt, int len) {
		return extractRandom(txt, len, new Random(System.currentTimeMillis()));
	}

	/**
	 * Extract a random piece of text.
	 * 
	 * @param txt Source text.
	 * @param len Length of the random sample to take.
	 * @param rnd Random number generator.
	 *
	 * @return A piece of plain text from <code>txt</code> of given length.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static String extractRandom(Text txt, int len, Random rnd) {
		String s = txt.getPlainText();
		if (len >= s.length())
			throw new IllegalArgumentException();

		int pos = rnd.nextInt(s.length() - len);

		return s.substring(pos, pos + len);
	}

	/**
	 * Extract a random piece of text containing given number of words.
	 * 
	 * @param txt Source text.
	 * @param len Length (tokens) of the random sample to take.
	 *
	 * @return A piece of plain text from <code>txt</code> with given number of
	 *         tokens.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static TextString extractRandomWords(Text txt, int len) {
		return extractRandomWords(txt, len, new Random(System.currentTimeMillis()));
	}

	/**
	 * Extract a random piece of text containing given number of words.
	 * 
	 * @param txt Source text.
	 * @param len Length (tokens) of the random sample to take.
	 * @param rnd Random number generator.
	 *
	 * @return A piece of plain text from <code>txt</code> with given number of
	 *         tokens.
	 * 
	 * @throws IllegalArgumentException if len is too big.
	 */
	public static TextString extractRandomWords(Text txt, int len, Random rnd) {
		String space = txt.getAlphabet().getSpaceAsString();
		String[] tokens = txt.getPlainText().split(Pattern.quote(space));

		if (len >= tokens.length)
			throw new IllegalArgumentException();

		int pos = rnd.nextInt(tokens.length - len + 1);

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < len; ++i) {
			if (i > 0)
				b.append(space);
			b.append(tokens[pos + i]);
		}

		return new TextString(b.toString(), txt.getAlphabet());
	}

	/**
	 * Remove character at given position in a string.
	 */
	public static String removeCharAt(String s, int pos) {
		StringBuilder sb = new StringBuilder(s);
		sb.deleteCharAt(pos);
		return sb.toString();
	}
}
