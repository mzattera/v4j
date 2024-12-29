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

	protected EvaAlphabet() {
	}
}