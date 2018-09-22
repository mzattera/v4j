package org.v4j.text.ivtff;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.v4j.Identifiable;

/**
 * This describes data about one page, it does not contain the page lines, which
 * are part of a document.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class PageHeader implements Identifiable {
	
	// TODO transformed these fields in page (text element) properties

	public static final String TYPE_MISSING = "M";
	public static final String TYPE_TEXT = "T";
	public static final String TYPE_HERBAL = "H";
	public static final String TYPE_ASTRONOMICAL = "A";
	public static final String TYPE_ZODIAC = "Z";
	public static final String TYPE_BIOLOGICAL = "B";
	public static final String TYPE_COSMOLOGICAL = "C";
	public static final String TYPE_PHARMACEUTICAL = "P";
	public static final String TYPE_STARS = "S";

	private final String id;

	@Override
	public String getId() {
		return id;
	}

	private final String illustrationType;

	/**
	 * Illustration type (T,H,A,Z,B,C,P,S). Text, Herbal, Astronomical, Zodiac,
	 * Biological, Cosmological, Pharmaceutical or Stars.
	 */
	// TODO rename
	public String getType() {
		return illustrationType;
	}

	private final String quire;

	/**
	 * Quire (A-T).
	 */
	public String getQuire() {
		return quire;
	}

	private final String pageInQuire;

	/**
	 * Page in quire (A-X).
	 */
	public String getPageInQuire() {
		return pageInQuire;
	}

	private final String language;

	/**
	 * Currier's language (A,B).
	 */
	public String getLanguage() {
		return language;
	}

	private final String hand;

	/**
	 * Currier's hand (1,2,3,4,5,X,Y).
	 */
	public String getHand() {
		return hand;
	}

	private final boolean hasKey;

	/**
	 * True if page has a key-like sequence.
	 * 
	 * @return
	 */
	public boolean hasSequenceLikeKey() {
		return hasKey;
	}

	private final String extraneousWriting;

	/**
	 * C A colour annotation
	 * 
	 * M A month name
	 * 
	 * O Other
	 * 
	 * S A sequence of characters or numbers
	 * 
	 * V Various (a combination of the above)
	 * 
	 * Y Any kind (legacy files only)
	 */
	public String getExtraneousWriting() {
		return extraneousWriting;
	}

	/**
	 * The notation used to identify a page in the Voynich MS is the character f
	 * (for folio) followed by the folio number, followed by r (for recto - the
	 * front) or v (for verso - the reverse).
	 * 
	 * For the foldout folios, fnr1, fnr2, etc, fnv1, fnv2, ...
	 * 
	 * <f15v> <! $I=H $Q=B $P=L $L=A $H=1>
	 */
	private final Pattern pageHeader = Pattern
			.compile("<(f[0-9]{1,3}(r|v)[0-9]?|fRos)>(\\s+<!((\\s+\\$[QPIKLHX]=.)*)\\s*>)?");

	/**
	 * Parses a file row to get a PageDescriptor
	 * 
	 * @param rowNum
	 *            line number in the inpur file.
	 */
	protected PageHeader(String row, int rowNum) throws ParseException {
		if (!row.startsWith("<"))
			throw new ParseException("Missing page indentifier", row, rowNum);

		row = row.trim();

		Matcher m = pageHeader.matcher(row);
		if (!m.matches())
			throw new ParseException("Row does not contain a valid page header", row, rowNum);

		id = m.group(1);

		// TODO validate only possible values

		if ((m.group(4) != null) && (m.group(4).length() > 0)) {
			quire = extractParameter("Q", m.group(4));
			pageInQuire = extractParameter("P", m.group(4));
			illustrationType = extractParameter("I", m.group(4));
			hasKey = extractParameter("K", m.group(4)).equals("Y");
			language = extractParameter("L", m.group(4));
			hand = extractParameter("H", m.group(4));
			extraneousWriting = extractParameter("X", m.group(4));
		} else {
			quire = "?";
			pageInQuire = "?";
			illustrationType = "?";
			hasKey = false;
			language = "?";
			hand = "?";
			extraneousWriting = "?";
		}
	}

	/**
	 * Creates a page descriptor only from the page id.
	 * 
	 * @param id
	 */
	protected PageHeader(String id) {
		this.id = id;
		quire = "?";
		pageInQuire = "?";
		illustrationType = "?";
		hasKey = false;
		language = "?";
		hand = "?";
		extraneousWriting = "?";
	}

	/**
	 * Extracts one parameter from a page descriptor: <f7v>
	 */
	private String extractParameter(String p, String row) {
		String param = "$" + p + "=";
		int pos = row.indexOf(param);
		if (pos == -1)
			return "?";
		return row.substring(pos + 3, pos + 4);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (!(obj instanceof PageHeader))
			return false;
		
		PageHeader p = (PageHeader) obj;
		return p.id.equals(this.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		StringBuffer txt = new StringBuffer();
		txt.append("<").append(id).append("> <! ");
		txt.append("$I=").append(illustrationType).append(" ");
		txt.append("$Q=").append(quire).append(" ");
		txt.append("$P=").append(pageInQuire).append(" ");
		txt.append("$L=").append(language).append(" ");
		txt.append("$H=").append(hand).append(" ");
		txt.append("$K=").append(hasKey).append(" ");
		txt.append("$X=").append(extraneousWriting).append(">");
		return txt.toString();
	}
}
