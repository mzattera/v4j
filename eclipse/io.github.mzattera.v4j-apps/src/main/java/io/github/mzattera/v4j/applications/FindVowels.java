/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.applications;

import java.util.HashMap;
import java.util.Map;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.text.txt.BibleFactory;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.StringUtil;

/**
 * Applies Sukhotin"s algorithm (exhaustive) to divide letters in vowels and
 * consonants.
 * 
 * See http://voynich.net/Arch/2001/01/msg00108.html
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class FindVowels {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			process(BibleFactory.getDocument("latin"),true);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	/**
	 * Searches given regular expression
	 */
	public static void process(Text doc, boolean toUpperCase) {

		Alphabet a = doc.getAlphabet();
		String txt = toUpperCase ? a.toUpperCase(doc.getPlainText()) : doc.getPlainText();
		Counter<Character> charCount = StringUtil.countChars(txt);

		// Maps a char to an index 1..N and vice versa
		Map<Character, Integer> index = new HashMap<>();
		Map<Integer, Character> reverse = new HashMap<>();
		int p = 0;
		for (char c : charCount.itemSet()) {
			if (a.isRegular(c)) {
				index.put(c, p);
				reverse.put(p, c);
				p++;
			}
		}

		// preceeds[i][j] is the number of times chars[i] precedes
		// chars[j] in txt
		int[][] preceeds = new int[index.size()][index.size()];
		char prev = txt.charAt(0);
		for (int i = 1; i < txt.length(); ++i) {
			char curr = txt.charAt(i);
			if (a.isRegular(prev) && a.isRegular(curr) && !a.isWordSeparator(prev) && !a.isWordSeparator(curr)) {
				preceeds[index.get(prev)][index.get(curr)]++;
				prev = curr;
			}
		}

		// this is true char at index i a "Vowel"
		boolean[] isVowel = new boolean[index.size()];

		// exhaustively see the best fit
		boolean hasNext = true;
		int iteration = 0;
		int bestScore = Integer.MIN_VALUE;
		boolean[] bestSolution = new boolean[isVowel.length];
		double combos = Math.pow(2, index.size());
		long millisStart = System.currentTimeMillis();
		while (hasNext) {

			int score = 0; // score for this combination
			for (int i = 0; i < isVowel.length; ++i) {
				for (int j = 0; j < isVowel.length; ++j) {
					if (isVowel[i] != isVowel[j]) {
						score += preceeds[i][j];
					}
				}
			}

			if (score > bestScore) {
				// Record best solution so far
				bestScore = score;
				System.arraycopy(isVowel, 0, bestSolution, 0, isVowel.length);
			}

			// Moves to next combination
			hasNext = false;
			for (int i = 0; (i < isVowel.length) && (!hasNext); ++i) {
				if (!isVowel[i]) {
					isVowel[i] = true;
					hasNext = true;
				} else {
					isVowel[i] = false;
				}
			}

			if ((iteration++ % 666000) == 0) {
				long elapsed = System.currentTimeMillis() - millisStart;
				double estimated = ((double) elapsed / iteration) * (combos - iteration);
				long secondsLeft = (long) (estimated / 1000);
				long minutes = (long) Math.floor(secondsLeft / 60);
				long seconds = secondsLeft - minutes * 60;
				System.out.println(
						"Estimate to completition (mm:ss): " + Math.round(minutes) + ":" + Math.round(seconds));
			}
		} // for all possible combinations

		// Writes solution
		for (int i = 0; i < bestSolution.length; ++i) {
			System.out.println(reverse.get(i) + " -> " + (bestSolution[i] ? "V" : "C"));
		}
	}
}
