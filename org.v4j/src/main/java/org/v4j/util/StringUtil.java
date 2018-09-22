/**
 * 
 */
package org.v4j.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	 * Grows length of a string by adding an extra '!' next to already existing '!'.
	 * This is used when aligning lines from different transcriptions so they can be
	 * merged.
	 * 
	 * @param s
	 * @param len
	 *            Number of characters to add.
	 * @return a set of strings corresponding to all possible ways of augmenting
	 *         length of given string (by inserting '!' at different positions).
	 */
	public static Collection<String> growLength(String s, int len) {
		Set<String> result = new HashSet<>();

		if (len <= 0) {
			// end recursion
			result.add(s);
			return result;
		}

		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == '!') {
				// expand length by adding an extra ! and store the new string
				result.add(new StringBuilder(s).insert(i, '!').toString());

				// skip other ! in same block
				for (int j = i + 1; j < s.length() && (s.charAt(j) == '!'); ++j)
					;
			}
		}

		return growLength(result, len - 1);
	}

	private static Collection<String> growLength(Collection<String> in, int len) {

		Set<String> result = new HashSet<>();

		for (String s : in) {
			result.addAll(growLength(s, len));
		}

		return result;
	}

}
