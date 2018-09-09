/**
 * 
 */
package org.v4j.util;

/**
 * Set of string processing Util
 * 
 * @author maxi
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

}
