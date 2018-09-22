package org.v4j.text.ivtff;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.v4j.text.Text;
import org.v4j.text.Token;
import org.v4j.text.alphabet.Alphabet;

/**
 * This class represents a line in a document, together with its ID. If the line
 * does not come from Voynich, then its ID might be meaningless.
 * 
 * @author maxi
 */
public class IvtffLine extends IvtffElement<LocusIdentifier, Token> {

	/** Line text */
	private final String text;

	@Override
	public String getText() {
		return text;
	}

	// Normalized text
	private final String plainText;

	public String getNormalizedText() throws ParseException {
		return plainText;
	}

	// Page containing this line
	private Text<?> page = null;

	@Override
	public void setParent(Text<?> page) {
		if (!this.descriptor.getPageId().equals(page.getId()))
			throw new IllegalArgumentException("Line " + this.getId() + " cannot be added to page " + page.getId());

		this.page = page;
	}

	@Override
	public Text<?> getParent() {
		return page;
	}

	public IvtffPage getPage() {
		return (IvtffPage) getParent();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public IvtffLine(IvtffLine other) {
		this.descriptor = other.descriptor;
		this.text = other.text;
		this.plainText = other.plainText;
		this.page = other.page;
	}

	/**
	 * Locus identifiers have the following format:
	 * 
	 * < page . num , code >
	 * 
	 * Or : < page . num , code ; T >
	 * 
	 * Whitespace is not allowed inside locus identifiers, but it is used in the
	 * patterns above for clarity. The fields have the following meaning:
	 * 
	 * page The page name, which has to match the most recent page header.
	 * 
	 * num A sequence number, incrementing from 1 for each page. The highest number
	 * that presently occurs is 160.
	 * 
	 * code A 3-character code, which is a 1-character ‘locator’ followed by a
	 * 2-character locus type
	 * 
	 * T An optional single-character transcriber ID. Only used in interlinear files
	 * that include several parallel transcriptions.
	 */
	private final static Pattern locusIdentifier = Pattern
			.compile("<(f[0-9]{1,3}[rv][0-9]?|fRos)\\.([0-9]{1,3}[a-z]?),([\\+\\*\\-=&~@/][PLCR].)(;.)?>");

	/**
	 * Creates a new instance parsing input file.
	 * 
	 * @param row
	 *            a row as read from a text input file.
	 * @param rowNum
	 *            row number inside the file.
	 */
	protected IvtffLine(String row, int rowNum, Alphabet a) throws ParseException {
		if (!row.startsWith("<"))
			throw new ParseException("Missing locus indentifier", row, rowNum);

		row = row.trim();

		// TODO check right combination of generic and complete type for the locus type
		Matcher m = locusIdentifier.matcher(row);
		if (!m.find() || (m.start() != 0)) {
			throw new ParseException("Missing or malformed locus identifier", row, rowNum);
		}

		descriptor = new LocusIdentifier(m.group(1), m.group(2), m.group(3), m.group(4).substring(1));
		text = row.substring(m.end()).trim();
		plainText = normalizeText(text, a);
	}

	/**
	 * Normalize (and validate) text.
	 * 
	 * @throws ParseException
	 */
	private String normalizeText(String text, Alphabet a) throws ParseException {
		// TODO is this is the case? or should it be simply removed?
		String txt = text.replaceAll("<\\->", a.getSpace() + ""); // plant intrusion is replaced by a space
		
		txt = removeComments(txt);
		txt = removeHighAscii(txt);
		txt = removeLigatures(txt);
		txt = removeAlternativeReadings(txt);
		txt = txt.replaceAll("!", ""); // "null" char in interlinear
		txt = txt.replaceAll("%", a.getUnreadable() + ""); // big unreadable part char in interlinear
		txt = txt.trim();
		if (!a.isPlain(txt))
			throw new ParseException("Line contains invalid characters", text);

		// TODO normalize text...uniform spaces and removal of contol chars

		return txt;
	}

	/**
	 * 
	 * @param txt
	 * @return txt after stripping all comments off.
	 * @throws ParseException
	 *             if there are unmatched angle brackets in text.
	 */
	private static String removeComments(String txt) throws ParseException {
		// TODO decide if <-> should be replaced by a space or not (words continue across drawings?)
		String result = txt.replaceAll("<![^>]*>|<[^>]{1,2}>", "");
		if ((result.indexOf('<') != -1) || (result.indexOf('>') != -1))
			throw new ParseException("Text contains unmatched comment brackets: " + txt);

		return result;
	}

	/**
	 * Replace all "high-ASCII" characters in the text with the unreadable char.
	 * 
	 * @param txt
	 * @throws ParseException
	 *             if @ and ; are not used properly.
	 */
	private static String removeHighAscii(String txt) throws ParseException {
		String result = txt.replaceAll("@[0-9]{3};", "?");
		if ((result.indexOf('@') != -1) || (result.indexOf(';') != -1))
			throw new ParseException("Improper use of @nnn; : " + txt);

		return result;
	}

	/**
	 * Remove all ligatures.
	 * 
	 * @param txt
	 * @throws ParseException
	 *             if curly brackets are are not matched properly.
	 */
	private static String removeLigatures(String txt) throws ParseException {
		String result = txt.replaceAll("\\{([^\\}]*)\\}", "$1");
		if ((result.indexOf('{') != -1) || (result.indexOf('}') != -1))
			throw new ParseException("Unmatched ligature : " + txt);

		return result;
	}

	/**
	 * Remove all alternative readings by choosing the first version.
	 * 
	 * @param txt
	 * @throws ParseException
	 *             if alternative readings are not correctly specified.
	 */
	private static String removeAlternativeReadings(String txt) throws ParseException {
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

	@Override
	public String toString() {
		return descriptor.toString() + "\t" + text;
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
