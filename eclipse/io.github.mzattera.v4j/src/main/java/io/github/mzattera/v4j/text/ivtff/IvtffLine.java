package io.github.mzattera.v4j.text.ivtff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.text.CompositeText;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.StringUtil;

/**
 * This class represents a line in a document, together with its ID. If the line
 * does not come from Voynich, then its ID might be meaningless.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class IvtffLine extends IvtffElement<LocusIdentifier, Text> {

	/** Line text */
	private String text;

	@Override
	public String getText() {
		return text;
	}

	public void setText(String txt) throws ParseException {
		text = txt.trim();
		plainText = normalizeText(text);
	}

	// Plain text
	private String plainText;

	@Override
	public String getPlainText() {
		return plainText;
	}

	public IvtffPage getPage() {
		return (IvtffPage) getParent();
	}

	@Override
	public void setParent(CompositeText<?> page) {
		// Page should never be null, but it might happen in some cases when we do
		// partial processing (e.g. when building concordance version).
		if ((page != null) && !this.descriptor.getPageId().equals(page.getId()))
			throw new IllegalArgumentException("Line " + this.getId() + " cannot be added to page " + page.getId());

		super.setParent(page);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public IvtffLine(IvtffLine other) {
		super(other.descriptor, other.getAlphabet());
		this.text = other.text;
		this.plainText = other.plainText;
		setParent(other.getParent());
	}

	/**
	 * Creates a new instance parsing given input string.
	 * 
	 * Same as calling IvtffLine(txt, -1, Alphabet.EVA)
	 * 
	 * Notice that the parsing proces
	 */
	public IvtffLine(String txt) throws ParseException {
		this(txt, -1, Alphabet.EVA);
	}

	/**
	 * Creates a new instance parsing input file.
	 * 
	 * Notice this process:
	 * <ul>
	 * <li>Remove IVTFF comments; that is text in angle brackets. txt =
	 * removeHighAscii(txt); txt = removeLigatures(txt); txt =
	 * removeAlternativeReadings(txt); txt = txt.replaceAll("!", ""); // "null" char
	 * in interlinear txt = txt.replaceAll("%",
	 * getAlphabet().getUnreadableAsString()); // big unreadable part char in
	 * interlinear txt = txt.trim();IvtffLine(String row, int rowNum, Alphabet a)
	 * 
	 * @param row    a row number if the line is read from a text input file. This
	 *               is mostly used for debugging and is optional.
	 * @param rowNum row number inside the file.
	 */
	public IvtffLine(String row, int rowNum, Alphabet a) throws ParseException {
		super(a);

		// TODO check right combination of generic and complete type for the locus type
		Matcher m = IvtffText.LOCUS_IDENTIFIER_PATTERN.matcher(row);
		if (!m.find() || (m.start() != 0)) {
			throw new ParseException("Missing or malformed locus identifier", row, rowNum);
		}

		descriptor = new LocusIdentifier(m.group(1), m.group(2), m.group(3), m.group(4).substring(1));
		setText(row.substring(m.end()).trim());
	}

	public IvtffLine(LocusIdentifier descriptor, String txt, Alphabet a) throws ParseException {
		super(a);
		this.descriptor = descriptor;
		setText(txt.trim());
	}

	/**
	 * Normalize (and validate) text. This process removes comments, special symbols
	 * ("high ASCII"), ligatures, alternative readings, "!" spacers from text; in
	 * addition it replaces "%" with unreadable characters.
	 * 
	 * @throws ParseException
	 */
	private String normalizeText(String text) throws ParseException {

		String txt = text.replaceAll("<\\->", getAlphabet().getSpaceAsString()); // plant intrusion is replaced by a
																					// space
																					// TODO verify
		txt = removeComments(txt);
		txt = removeHighAscii(txt);
		txt = removeLigatures(txt);
		txt = removeAlternativeReadings(txt);
		txt = txt.replaceAll("!", ""); // "null" char in interlinear
		txt = txt.replaceAll("%", getAlphabet().getUnreadableAsString()); // big unreadable part char in interlinear
		txt = txt.trim();

		// Checks that the above has processed and remove all control sequences
		for (int i = 0; i < txt.length(); ++i)
			if (!alphabet.isRegular(txt.charAt(i)) && !alphabet.isWordSeparator(txt.charAt(i))
					&& !alphabet.isUreadableChar(txt.charAt(i)))
				throw new ParseException("Line contains invalid characters", text + " ['" + txt.charAt(i) + "']");

		return getAlphabet().toPlainText(txt);
	}

	/**
	 * 
	 * @return txt after removing ALL comments.
	 * @throws ParseException if there are unmatched angle brackets in text.
	 */
	public static String removeComments(String txt) throws ParseException {
		String result = replaceComments(txt, "");
		return result;
	}

	/**
	 * 
	 * @return txt after replacing all comments with replace.
	 * @throws ParseException if there are unmatched angle brackets in text.
	 */
	public static String replaceComments(String txt, String replace) throws ParseException {
		String result = replaceInlineComments(txt, replace);
		result = result.replaceAll("<[^>]{1,2}>", replace);
		if ((result.indexOf('<') != -1) || (result.indexOf('>') != -1))
			throw new ParseException("Text contains unmatched comment brackets: " + txt);

		return result;
	}

	/**
	 * 
	 * @return txt after replacing all in-line comments with replace.
	 * @throws ParseException if there are unmatched angle brackets in text.
	 */
	public static String replaceInlineComments(String txt, String replace) throws ParseException {
		return txt.replaceAll("<![^>]*>", replace);
	}

	/**
	 * Replace all "high-ASCII" characters in the text with "unreadable" char.
	 * 
	 * @throws ParseException if @ and ; are not used properly.
	 */
	public static String removeHighAscii(String txt) throws ParseException {
		String result = txt.replaceAll("@[0-9]{3};", "?");
		if ((result.indexOf('@') != -1) || (result.indexOf(';') != -1))
			throw new ParseException("Improper use of @nnn; : " + txt);

		return result;
	}

	/**
	 * Remove all ligatures.
	 * 
	 * @param txt
	 * @throws ParseException if curly brackets are are not matched properly.
	 */
	public static String removeLigatures(String txt) throws ParseException {
		String result = txt.replaceAll("\\{([^\\}]*)\\}", "$1");
		if ((result.indexOf('{') != -1) || (result.indexOf('}') != -1))
			throw new ParseException("Unmatched ligature : " + txt);

		return result;
	}

	/**
	 * Remove all alternative readings by choosing the first version.
	 * 
	 * @param txt
	 * @throws ParseException if alternative readings are not correctly specified.
	 */
	public static String removeAlternativeReadings(String txt) throws ParseException {
		Matcher m = Pattern.compile("\\[[^\\]]*\\]").matcher(txt);
		while (m.matches()) {
			String[] readings = m.group(1).split(":");
			if (readings.length < 2)
				throw new ParseException("No alternative readings provided in [] : " + txt);

			// TODO we should check correctness of all possible alternatives....maybe a bit
			// too much ;)
		}

		return txt.replaceAll("\\[([^\\]:]*)(:[^\\]:]*)+\\]", "$1");
	}

	/**
	 * Changes text of given lines such that they are aligned and ready to be
	 * merged.
	 * 
	 * @return true if the lines could be aligned.
	 */
	public static boolean align(List<IvtffLine> group) throws ParseException {

		if (group.size() == 0)
			return false; // guard
		if (group.size() == 1)
			return true; // guard

		Alphabet a = group.get(0).getAlphabet();

		// Check if lines are already aligned
		String longest = group.get(0).getText();
		int maxLen = longest.length(); // target length of text for lines in this group
		boolean aligned = true; // true if text is aligned for all lines
		for (IvtffLine line : group) {
			String txt = line.getText();

			if (txt.length() != maxLen) {
				aligned = false;
				if (txt.length() > maxLen) {
					longest = txt;
					maxLen = longest.length();
				}
			}
		}

		if (aligned)
			return true;

		// Not aligned; try to "normalize" text; note that we typically not remove but
		// instead replace "blocks" of control chars with '!' so there is flexibility
		// left
		// to align the lines
		maxLen = -1;
		boolean expandAtEnd = false; // true if at least one line ends with '!'
		for (IvtffLine line : group) {

			String txt = line.getText().replaceAll("<\\->", a.getSpaceAsString()); // plant intrusion is replaced by a
																					// space TODO verify
			txt = replaceInlineComments(txt, "!");

			// handling of <-><!...> or "ain <!Grove's...>" at end of line
			StringBuilder spaces = new StringBuilder("(\\s");
			for (char s : a.getWordSeparatorChars())
				spaces.append('|').append(Pattern.quote(s + "")); // todo use /q /E instead
			spaces.append(")*!+\\z");
			txt = txt.replaceAll(spaces.toString(), "!");

			txt = removeHighAscii(txt);
			txt = txt.replaceAll("\\{|\\}", "!"); // remove ligatures
			txt = txt.replaceAll("\\[([^\\]:]*)(:[^\\]:]*)+\\]", "!$1!"); // remove alternative readings
			txt = txt.replaceAll("%+", "!"); // big unreadable part char in interlinear
			txt = txt.trim();
			if (Pattern.matches("!+", txt)) {
				txt = "!"; // Guard: some lines are made only of %%%%%%% we then make them a single comment
							// that can fit any length
			}

			if (maxLen == -1) {
				longest = txt;
				maxLen = longest.length();
			} else if (txt.length() != maxLen) {
				aligned = false;
				if (txt.length() > maxLen) {
					longest = txt;
					maxLen = longest.length();
				}
			}

			if (txt.endsWith("!"))
				expandAtEnd = true;

			line.setText(txt);
		}

		if (aligned)
			return true;

		// Not aligned, make sure that if any has a ! at the end, then all have
		for (IvtffLine line : group) {

			String txt = line.getText();
			if (expandAtEnd && !txt.endsWith("!")) {
				txt += "!";
				line.setText(txt);
				if (txt.length() > maxLen) {
					longest = txt;
					maxLen = longest.length();
				}
			}
		}

		// Now do the alignment
		aligned = true;
		for (IvtffLine line : group) {

			String txt = line.getText();

			if (txt.length() < maxLen) {
				txt = IvtffLine.align(txt, longest);
				line.setText(txt);
			}

			if (txt.length() != maxLen)
				aligned = false;
		}

		return aligned;
	}

	/**
	 * Aligns s1 to s2 by adding extra '!'. This is used when merging lines from
	 * different transcriptions.
	 * 
	 * @param s1 the string to align; cannot be longer than s2. Notice that s1
	 *           should have been pre-processed by calling align.
	 * @param s2 the reference string.
	 * @return best alignment of s1 to s2 by adding extra '!'. Notice that if adding
	 *         '!' does not provide a better alignment, then s1 is returned.
	 */
	private static String align(String s1, String s2) {
		if (s1.length() >= s2.length())
			throw new IllegalArgumentException("String too short.");

		// set of all possible matches
		Set<String> set = new HashSet<>();
		set.add(s1);

		// try all different lengths;
		Set<String> last = new HashSet<>(set);
		for (int i = 1; i <= (s2.length() - s1.length()); ++i) { //
			last = growLength(last, 1);
			set.addAll(last);
		}

		String result = s1;
		int best = StringUtil.countMatchingChars(result, s2);
		for (String s : set) {
			int c = StringUtil.countMatchingChars(s, s2);
			if ((c > best) || ((c == best) && (s.length() > result.length())) // prefer longer strings
			) {

				best = c;
				result = s;
			}
		}
		return result;
	}

	/**
	 * Grows length of a string by adding an extra '!' next to already existing '!'.
	 * This is used when aligning lines from different transcriptions so they can be
	 * merged.
	 * 
	 * @param s   string to grow, notice it assumes it has been pre-processed by
	 *            align()
	 * @param len Number of characters to add.
	 * @return a set of strings corresponding to all possible ways of augmenting
	 *         length of given string (by inserting '!' at different positions).
	 */
	private static Set<String> growLength(String s, int len) {
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
				for (; i < s.length() && (s.charAt(i) == '!'); ++i)
					;
			}
		}

		return growLength(result, len - 1);
	}

	/**
	 * Grows length of a group of strings by adding an extra '!' next to already
	 * existing '!'. This is used when aligning lines from different transcriptions
	 * so they can be merged.
	 * 
	 * @param in  strings to grow, notice it assumes they have been pre-processed by
	 *            align()
	 * @param len Number of characters to add.
	 * @return a set of strings corresponding to all possible ways of augmenting
	 *         length of given strings (by inserting '!' at different positions).
	 */
	private static Set<String> growLength(Collection<String> in, int len) {

		Set<String> result = new HashSet<>();

		for (String s : in) {
			result.addAll(growLength(s, len));
		}

		return result;
	}

	/**
	 * Merges together some transcriptions.
	 * 
	 * @param lines a group of lines, they are assumes to refer to the same text,
	 *              but for different transcribers.
	 * @param type  the type of transcription we need to return with the merge.
	 * @return merged text as new line.
	 * @throws ParseException if lines cannot be merged.
	 */
	public static IvtffLine merge(List<IvtffLine> lines, TranscriptionType type) throws ParseException {

		if (lines.size() == 0)
			throw new IllegalArgumentException("Cannot process an empty group of lines.");

		// System.out.println(group.get(0).getDescriptor().toString());

		List<IvtffLine> copy = new ArrayList<>();
		for (IvtffLine l : lines)
			copy.add(new IvtffLine(l));

		if (!align(copy)) {

			// TODO remove debug code
			for (IvtffLine l : lines)
				System.out.println(l);
			
			throw new ParseException("Cannot align the transcriptions.");
		}

		IvtffLine merged = null;
		switch (type) {
		case MAJORITY:
			merged = getMajorityVersion(copy);
			break;
		case CONCORDANCE:
			merged = getConcordanceVersion(copy);
			break;
		default:
			throw new IllegalArgumentException("Unsupported transcription type: " + type);
		}

		// Find a line of typical length in the original text (notice the original lines
		// might not be aligned, we use the most frequent length here
		Counter<Integer> counter = new Counter<>();
		for (IvtffLine line : lines)
			counter.count(line.getText().length());
		IvtffLine example = null;
		for (IvtffLine line : lines) {
			if (line.getText().length() == counter.getHighestCounted()) {
				example = line;
				break;
			}
		}

		// align the result with what we have
		if (merged.getText().length() < example.getText().length()) {
			List<IvtffLine> tmp = new ArrayList<>();
			tmp.add(new IvtffLine(example));
			tmp.add(merged);
			align(tmp);
		}

		return merged;
	}

	/**
	 * Given a set of lines, returns the majority version of their text.
	 * 
	 * @param lines
	 * @return Majority version returns the most frequent characters over the
	 *         available lines/transcriptions.
	 * @throws ParseException if an error happens while merging lines.
	 */
	private static IvtffLine getMajorityVersion(List<IvtffLine> lines) throws ParseException {

		StringBuilder result = new StringBuilder();
		Alphabet a = lines.get(0).getAlphabet();

		for (int c = 0; c < lines.get(0).getText().length(); ++c) {

			// Count characters in a given position
			Counter<Character> counter = new Counter<>();
			for (int l = 0; l < lines.size(); ++l) {
				counter.count(lines.get(l).getText().charAt(c));
			}

			char ch = '!';
			int max = counter.getHighestCount();
			for (char c1 : counter.itemSet()) {
				if (counter.getCount(c1) < max)
					continue;

				if (ch == '!') {
					ch = c1; // prefer something over nothing / initialization
					continue;
				}

				// if we are here, then there is a tie; since c1 occurs max times and ch was already initialised
				
				// if the conflict is between spaces, we keep the "weak" space
				if (a.isWordSeparator(c1) && a.isWordSeparator(ch)) {
					ch = ',';
					continue;
				}

				if (a.isRegularOrSeparator(c1) || a.isUreadableChar(c1)) {
					// conflicting "printable" chars, we return unreadable
					ch = a.getUnreadable(); 
					break;
				}

				// in all other cases returns the first most occurring char
			}

			result.append(ch);
		} // for each char

		LocusIdentifier id = new LocusIdentifier(lines.get(0).getDescriptor().getPageId(),
				lines.get(0).getDescriptor().getNumber(), lines.get(0).getDescriptor().getLocus(),
				VoynichFactory.MAJORITY_TRANSCRIBER);
		return new IvtffLine(id, result.toString(), a);
	}

	/**
	 * Given a set of lines, returns the concordance version of their text.
	 * 
	 * @param lines
	 * @return Concordance version returns only the characters that matches in all
	 *         lines/transcriptions.
	 * @throws ParseException if an error happens while merging lines.
	 */
	@SuppressWarnings("unused")
	private static IvtffLine getConcordanceVersion(List<IvtffLine> lines) throws ParseException {
		
		StringBuilder result = new StringBuilder();
		Alphabet a = lines.get(0).getAlphabet();

		for (int c = 0; c < lines.get(0).getText().length(); ++c) {

			char ch = lines.get(0).getText().charAt(c);
			boolean conflict = false;
			for (int l = 1; l < lines.size(); ++l) {
				char c1 = lines.get(l).getText().charAt(c);
				if ((c1 != ch) && !(a.isUreadableChar(c1) && a.isUreadableChar(ch))) { // % and ? should not be considered different 
					conflict = true;
					break;
				}
			} // for each line

			// The below is a bit clunky; maybe there is a better way tp cpde this behavior.
			Character found = null;
			if (conflict) {
				// Ok not all chars are the same, we handle special cases here

				// If conflict is only between spaces AND ! then put down a dubious space
				boolean atLeastOneSpace = false;
				boolean allVoid = true;
				for (IvtffLine ln : lines) {
					char c1 = ln.getText().charAt(c);
					if (a.isWordSeparator(c1)) {
						atLeastOneSpace = true;
					} else if (c1 != '!') {
						allVoid = false;
						break;
					}
				}
				if (atLeastOneSpace && allVoid) {
					found = ',';
				} else {
				// If a conflict includes an actual character, word separator or unreadable char
				// ('?')
				// Then we acknowledge this is unreadable
					for (IvtffLine ln : lines) {
						char c1 = a.asPlain(ln.getText().charAt(c));
						if (a.isRegularOrSeparator(c1) || a.isUreadableChar(c1)) {
							found = a.getUnreadable();
							break;
						}
					}
				}

				if (found == null)
					found = '!';
			} else {
				found = ch;
			}

			// Guard
			if (found == null)
				throw new ParseException();

			result.append(found);
		} // for each char

		LocusIdentifier id = new LocusIdentifier(lines.get(0).getDescriptor().getPageId(),
				lines.get(0).getDescriptor().getNumber(), lines.get(0).getDescriptor().getLocus(),
				VoynichFactory.CONCORDANCE_TRANSCRIBER);
		return new IvtffLine(id, result.toString(), a);
	}

	@Override
	public String toString() {
		return (descriptor == null ? "" : descriptor.toString()) + "\t" + text; // silly guard
	}

	/**
	 * Returns true when two lines have same id.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof IvtffLine))
			return false;

		IvtffLine other = (IvtffLine) o;
		return this.getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
