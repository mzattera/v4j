/**
 * 
 */
package org.v4j.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * Grows length of a string by adding an extra '!' next to already existing '!'
	 * or '%'. This is used when aligning lines from different transcriptions so
	 * they can be merged.
	 * 
	 * @param s
	 * @param len
	 *            Number of characters to add.
	 * @return a set of strings corresponding to all possible ways of augmenting
	 *         length of given string (by inserting '!' at different positions).
	 */
	public static Set<String> growLength(String s, int len) {
		Set<String> result = new HashSet<>();

		if (len <= 0) {
			// end recursion
			result.add(s);
			return result;
		}

		for (int i = 0; i < s.length(); ++i) {
			if ((s.charAt(i) == '%') || ((s.charAt(i) == '!') && ((i == 0) || (s.charAt(i - 1) != '<')))) { // make sure
																											// we not
																											// expand
																											// <!..>
				// expand length by adding an extra ! and store the new string
				result.add(new StringBuilder(s).insert(i, '!').toString());

				// skip other ! in same block
				for (; i < s.length() && ((s.charAt(i) == '!') || (s.charAt(i) == '%')); ++i)
					;
			}
		}

		return growLength(result, len - 1);
	}

	private static Set<String> growLength(Collection<String> in, int len) {

		Set<String> result = new HashSet<>();

		for (String s : in) {
			result.addAll(growLength(s, len));
		}

		return result;
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
	 * Aligns s1 to s2 by adding extra '!'. This is used when merging lines from
	 * different transcriptions.
	 * 
	 * @param s1
	 *            the string to align; cannot be longer than s2.
	 * @param s2
	 *            the reference string.
	 * @return best alignment of s1 to s2 by adding extra '!'. Notice that if adding
	 *         '!' does not provide a better alignment, then s1 is returned.
	 */
	public static String align(String s1, String s2) {
		if (s1.length() > s2.length())
			throw new IllegalArgumentException("String too short.");

		Set<String> set = new HashSet<>();
		set.add(s1);

		// covr the case where we have a picture intrusion matching a space
		//
		// <f8v.12,+P0;H> pchar.cho.rol.dal<-><!plant>shear.cheeotaiin.chal.daiin
		// <f8v.12,+P0;C> pchar.cho.rol.dal<-><!plant>shear.chchotaiin.chal.daiin
		// <f8v.12,+P0;F> pchar.cho.rol.dal<-><!plant>shear.cheeotaiin.chal.daiin
		// <f8v.12,+P0;U> pchar.cho.rol.dal<-><!plant>shear<->chchotaiin.chal.daiin
		Matcher m = Pattern.compile("<\\->").matcher(s2);
		while (m.find()) {
			if ((m.start() < s1.length()) && (s1.charAt(m.start()) == '.'))
				set.add(new StringBuilder(s1).insert(m.start(), '!').toString());
		}

		// try all different lengths; sometimes the longer string has a comment that it
		// just can't be matched
		//
		// <f72r3.28,&Lz;H> yfary
		// <f72r3.28,&Lz;V> ypary <!Grove's #3>
		// <f72r3.28,&Lz;U> ypary
		Set<String> last = new HashSet<>(set);
		for (int i = 1; i <= (s2.length() - s1.length()); ++i) {
			last = growLength(set, 1);
			set.addAll(last);
		}

		String result = s1;
		int best = countMatchingChars(result, s2);
		for (String s : set)
			if ((countMatchingChars(s, s2) > best)
					|| ((countMatchingChars(s, s2) == best) && (s.length() > result.length())) // prefer longer strings
			) {

				best = countMatchingChars(s, s2);
				result = s;
			}

		return result;
	}
}
