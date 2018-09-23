/**
 * 
 */
package org.v4j.util;

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
}
