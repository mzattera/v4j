/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/*
 * Gliph.java
 *
 * Created on 12 novembre 2005, 20.53
 */

package io.github.mzattera.v4j.text.alphabet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Basic EVA script alphabet (see http://www.voynich.nu/transcr.html).
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public final class EvaAlphabet extends IvtffAlphabet {

	@Override
	public String getCodeString() {
		return "Eva-";
	}

	private static final char[] regularChars;
	static {

		regularChars = new char['z' - 'a' + 1];
		int i = 0;
		for (char c = 'a'; c <= 'z'; ++c)
			if (c != 'w')
				regularChars[i++] = c;

		// We add this, however it doesn't seem to be used in the transcriptions we use
		regularChars[i++] = '\'';
	}

	@Override
	public char[] getRegularChars() {
		return regularChars;
	}
	
	/**
	 * Split a word into prefix, infix and suffix.
	 * 
	 * @param words A word written in this alphabet, to be split into its pre-, in- and suffix.
	 * 
	 * @return Three strings corresponding to the prefix, infix and suffix for given word.
	 */
	@Override
	public String[] getPreInSuffix(String word) {
		String[] result = new String[3];
		
		// Prefix
		Pattern p = Pattern.compile("^([qsd]?[oy]?[lr]?).*");
		Matcher m = p.matcher(word);
		if (m.matches()) {
			result[0] = m.group(1);
			if (result[0].length() == word.length()) {
				result[1] = "";
				result[2] = "";
				return result;
			}
		} else {
			result[0] = "";			
		}

		// Suffix
		p = Pattern.compile("^([y]?[dlrmn]?[i]{0,3}[oa]?).*");
		m = p.matcher(new StringBuilder(word.substring(result[0].length())).reverse().toString());
		if (m.matches()) {
			result[2] = new StringBuilder(m.group(1)).reverse().toString();
		} else {
			result[2] = "";			
		}

		// Infix
		result[1] = word.substring(result[0].length(), word.length()-result[2].length());
		
		return result;
	}

	protected EvaAlphabet() {
	}
}