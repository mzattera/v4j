/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.applications.slot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Based on a comparison of state machines for different clusters,
 * this looks at distribution of some character patterns in tokens to extract
 * possible features that differentiate clusters at word-structure level.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class ExtractGrammaticalFeatures {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.MAJORITY;
	
	/** 
	 * Number of pages in main clusters.
	 */
	public static final int NUM_PAGES = 178;
	
	/**
	 * Average number of words in a page.
	 */
	public static final int AVG_TOKEN_IN_PAGE = 168;
	
	/** 
	 * If a pattern appears less than this % in the entire text, ignore it
	 */
	public static final double THRESHOLD = 1.0d / AVG_TOKEN_IN_PAGE;
	
	/** Consider only readable words? */
	private static final boolean READABLE_ONLY = false;

	/**
	 * This is the list of regex to look for; you can use - at beginning or end of
	 * sequence to indicate a prefix or a suffix
	 */
	private static final String[] REGEXES = new String[] {
			// <BEGIN>
			"[sdqylrTPKFeEBo]-",
			// SLOT 0
			"[dqs]-", "d[eEBCS]-", "q[eEB]-",
			// SLOT 1
			"-o[lrCTPKFeEBdsao]-", "-y[tpkfCSda]?-", "y-",
			// SLOT 2
			"-l[tpkfCSdao]?-", "-r[CSao]?-",
			// SLOT 3
			"-[pf]-", "-[tpkf][CSal]-", "-[tpkf]",
			// SLOT 4
			"-C[eEBsaod]-", "-[CS]", "-S[eEBsodry]-",
			// SLOT 5
			"-[P]-", "-[TPKF][eEBaody]-",
			// SLOT 6
			"-[B]-", "-[eEB][sod]-", "-[eEB]",
			// SLOT 7
			"-[ds]-" + "-d[ol]-", "-s[ao]-", "-s",
			// SLOT 8
			"-an-", "-a", "-o[iJUdmr]-",
			// SLOT 9
			"-[i]-", "-[iJU]r-",
			// SLOT 10
			"-[lr]y", "-d" };

	private static final Path OUTPUT_FILE = Path.of("D:\\VoynichFeatures.csv");

	private static final Random RND = new Random(666);

	private ExtractGrammaticalFeatures() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println();

			IvtffText voynich = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.SLOT);
			Counter<String> words = voynich.getWords(READABLE_ONLY);
			List<String> patterns = extractPatterns(words);
			extractFeatures(voynich, patterns);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * From regular expressions in REGEXES, get a list of corresponding patterns
	 * found in words which appear above THRESHOLD. A pattern contains only single characters and -.
	 */
	private static List<String> extractPatterns(Counter<String> words) {

		List<String> patterns = new ArrayList<>();

		for (String pattern : REGEXES) {

			// Special handling of - for prefixes and suffixes
			String regex = pattern;
			boolean prefix = false;
			boolean suffix = false;
			if (regex.startsWith("-")) {
				prefix = true;
				regex = regex.substring(1);
			}
			if (regex.endsWith("-")) {
				suffix = true;
				regex = regex.substring(0, regex.length() - 1);
			}
			regex = "(" + regex + ")";
			if (prefix) {
				regex = "^.*" + regex;
			} else {
				regex = "^" + regex;
			}
			if (suffix) {
				regex = regex + ".*$";
			} else {
				regex = regex + "$";
			}
			Pattern p = Pattern.compile(regex);

			// A single regex might match several string patterns, we want to collect all of
			// them and display them separately
			for (String w : words.itemSet()) {
				Matcher m = p.matcher(w);
				if (m.matches()) {
					String g = m.group(1);
					if (prefix)
						g = "-" + g;
					if (suffix)
						g = g + "-";
					if (!patterns.contains(g)) {
						patterns.add(g);
					}
				}
			}
		}

		// Filter out patterns which do not appear frequently enough
		List<String> result = new ArrayList<>();
		for (String pattern : patterns)
			if (calculatePercentMatch(words, pattern) >= THRESHOLD)
				result.add(pattern);
		return result;
	}

	/**
	 * Creates a CSV file from given Text with a page per row and where each column
	 * has the % occurrence of each pattern.
	 * 
	 * @param patterns List of all patterns we want to use as column features.
	 * @throws IOException
	 */
	private static void extractFeatures(IvtffText voynich, List<String> patterns) throws IOException {

		try (CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(OUTPUT_FILE), CSVFormat.DEFAULT);) {

			// Header
			csvPrinter.print("Page");
			csvPrinter.print("Cluster");
			csvPrinter.print("Illustration");
			csvPrinter.print("Language");
			csvPrinter.print("IllustrationAndLanguage");
			csvPrinter.print("RandomGroup");
			csvPrinter.print("Tokens");
			for (String ptr : patterns)
				csvPrinter.print(ptr);
			csvPrinter.println();

			for (IvtffPage p : voynich.getElements()) {
				int tokenCount = p.getWords(READABLE_ONLY).getTotalCounted();
				PageHeader dsc = p.getDescriptor();
				csvPrinter.print(dsc.getId());
				csvPrinter.print(dsc.getCluster());
				csvPrinter.print(dsc.getIllustrationType());
				csvPrinter.print(dsc.getLanguage());
				csvPrinter.print(dsc.getIllustrationAndLanguage());
				csvPrinter.print(RND.nextInt(PageHeader.CLUSTERS.length));
				csvPrinter.print(tokenCount);
				for (String ptr : patterns) {
					csvPrinter.print(calculatePercentMatch(p, ptr));
				}
				csvPrinter.println();
			}

			csvPrinter.flush();
		}
	}

	/**
	 * @return % of tokens matching given pattern in text.
	 */
	public static double calculatePercentMatch(Text doc, String pattern) {
		return calculatePercentMatch(doc.getWords(READABLE_ONLY), pattern);
	}

	/**
	 * @return % of tokens matching given pattern in given list of words.
	 */
	public static double calculatePercentMatch(Counter<String> words, String pattern) {

		// TODO remove code duplication

		// Special handling of - for prefixes and suffixes
		String regex = pattern;
		boolean prefix = false;
		boolean suffix = false;
		if (regex.startsWith("-")) {
			prefix = true;
			regex = regex.substring(1);
		}
		if (regex.endsWith("-")) {
			suffix = true;
			regex = regex.substring(0, regex.length() - 1);
		}
		regex = "(" + regex + ")";
		if (prefix) {
			regex = "^.*" + regex;
		} else {
			regex = "^" + regex;
		}
		if (suffix) {
			regex = regex + ".*$";
		} else {
			regex = regex + "$";
		}

		double t = 0;

		Pattern p = Pattern.compile(regex);
		for (String w : words.itemSet()) {
			Matcher m = p.matcher(w);
			if (m.find())
				t += words.getCount(w);
		}

		return t / words.getTotalCounted();
	}

}
