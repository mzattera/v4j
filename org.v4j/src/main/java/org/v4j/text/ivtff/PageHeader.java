package org.v4j.text.ivtff;

import java.util.HashMap;
import java.util.Map;
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
	
	/**
	 * List of clusters I defined, based on cluster analysis.
	 */
	public static String[] CLUSTERS = {"HA1", "PHA", "HB", "BB1", "SB1", "SB2"};

	// Maps each folio in its corresponding parchment (or "bifolio").
	// TODO: an alternative approach would be to add a new header field and store
	// this information in the transscription file.
	private static final Map<String, Integer> bifolio = new HashMap<>();
	static {
		bifolio.put("f1r", 1);
		bifolio.put("f1v", 1);
		bifolio.put("f2r", 2);
		bifolio.put("f2v", 2);
		bifolio.put("f3r", 3);
		bifolio.put("f3v", 3);
		bifolio.put("f4r", 4);
		bifolio.put("f4v", 4);
		bifolio.put("f5r", 4);
		bifolio.put("f5v", 4);
		bifolio.put("f6r", 3);
		bifolio.put("f6v", 3);
		bifolio.put("f7r", 2);
		bifolio.put("f7v", 2);
		bifolio.put("f8r", 1);
		bifolio.put("f8v", 1);
		bifolio.put("f9r", 5);
		bifolio.put("f9v", 5);
		bifolio.put("f10r", 6);
		bifolio.put("f10v", 6);
		bifolio.put("f11r", 7);
		bifolio.put("f11v", 7);
		bifolio.put("f13r", 8);
		bifolio.put("f13v", 8);
		bifolio.put("f14r", 7);
		bifolio.put("f14v", 7);
		bifolio.put("f15r", 6);
		bifolio.put("f15v", 6);
		bifolio.put("f16r", 5);
		bifolio.put("f16v", 5);
		bifolio.put("f17r", 9);
		bifolio.put("f17v", 9);
		bifolio.put("f18r", 10);
		bifolio.put("f18v", 10);
		bifolio.put("f19r", 11);
		bifolio.put("f19v", 11);
		bifolio.put("f20r", 12);
		bifolio.put("f20v", 12);
		bifolio.put("f21r", 12);
		bifolio.put("f21v", 12);
		bifolio.put("f22r", 11);
		bifolio.put("f22v", 11);
		bifolio.put("f23r", 10);
		bifolio.put("f23v", 10);
		bifolio.put("f24r", 9);
		bifolio.put("f24v", 9);
		bifolio.put("f25r", 13);
		bifolio.put("f25v", 13);
		bifolio.put("f26r", 14);
		bifolio.put("f26v", 14);
		bifolio.put("f27r", 15);
		bifolio.put("f27v", 15);
		bifolio.put("f28r", 16);
		bifolio.put("f28v", 16);
		bifolio.put("f29r", 16);
		bifolio.put("f29v", 16);
		bifolio.put("f30r", 15);
		bifolio.put("f30v", 15);
		bifolio.put("f31r", 14);
		bifolio.put("f31v", 14);
		bifolio.put("f32r", 13);
		bifolio.put("f32v", 13);
		bifolio.put("f33r", 17);
		bifolio.put("f33v", 17);
		bifolio.put("f34r", 18);
		bifolio.put("f34v", 18);
		bifolio.put("f35r", 19);
		bifolio.put("f35v", 19);
		bifolio.put("f36r", 20);
		bifolio.put("f36v", 20);
		bifolio.put("f37r", 20);
		bifolio.put("f37v", 20);
		bifolio.put("f38r", 19);
		bifolio.put("f38v", 19);
		bifolio.put("f39r", 18);
		bifolio.put("f39v", 18);
		bifolio.put("f40r", 17);
		bifolio.put("f40v", 17);
		bifolio.put("f41r", 21);
		bifolio.put("f41v", 21);
		bifolio.put("f42r", 22);
		bifolio.put("f42v", 22);
		bifolio.put("f43r", 23);
		bifolio.put("f43v", 23);
		bifolio.put("f44r", 24);
		bifolio.put("f44v", 24);
		bifolio.put("f45r", 24);
		bifolio.put("f45v", 24);
		bifolio.put("f46r", 23);
		bifolio.put("f46v", 23);
		bifolio.put("f47r", 22);
		bifolio.put("f47v", 22);
		bifolio.put("f48r", 21);
		bifolio.put("f48v", 21);
		bifolio.put("f49r", 25);
		bifolio.put("f49v", 25);
		bifolio.put("f50r", 26);
		bifolio.put("f50v", 26);
		bifolio.put("f51r", 27);
		bifolio.put("f51v", 27);
		bifolio.put("f52r", 28);
		bifolio.put("f52v", 28);
		bifolio.put("f53r", 28);
		bifolio.put("f53v", 28);
		bifolio.put("f54r", 27);
		bifolio.put("f54v", 27);
		bifolio.put("f55r", 26);
		bifolio.put("f55v", 26);
		bifolio.put("f56r", 25);
		bifolio.put("f56v", 25);
		bifolio.put("f57r", 29);
		bifolio.put("f57v", 29);
		bifolio.put("f58r", 30);
		bifolio.put("f58v", 30);
		bifolio.put("f65r", 30);
		bifolio.put("f65v", 30);
		bifolio.put("f66r", 29);
		bifolio.put("f66v", 29);
		bifolio.put("f67r1", 31);
		bifolio.put("f67r2", 31);
		bifolio.put("f67v2", 31);
		bifolio.put("f67v1", 31);
		bifolio.put("f68r1", 31);
		bifolio.put("f68r2", 31);
		bifolio.put("f68r3", 31);
		bifolio.put("f68v3", 31);
		bifolio.put("f68v2", 31);
		bifolio.put("f68v1", 31);
		bifolio.put("f69r", 32);
		bifolio.put("f69v", 32);
		bifolio.put("f70r1", 32);
		bifolio.put("f70r2", 32);
		bifolio.put("f70v2", 32);
		bifolio.put("f70v1", 32);
		bifolio.put("f71r", 33);
		bifolio.put("f71v", 33);
		bifolio.put("f72r1", 33);
		bifolio.put("f72r2", 33);
		bifolio.put("f72r3", 33);
		bifolio.put("f72v3", 33);
		bifolio.put("f72v2", 33);
		bifolio.put("f72v1", 33);
		bifolio.put("f73r", 34);
		bifolio.put("f73v", 34);
		bifolio.put("f75r", 35);
		bifolio.put("f75v", 35);
		bifolio.put("f76r", 36);
		bifolio.put("f76v", 36);
		bifolio.put("f77r", 37);
		bifolio.put("f77v", 37);
		bifolio.put("f78r", 38);
		bifolio.put("f78v", 38);
		bifolio.put("f79r", 39);
		bifolio.put("f79v", 39);
		bifolio.put("f80r", 39);
		bifolio.put("f80v", 39);
		bifolio.put("f81r", 38);
		bifolio.put("f81v", 38);
		bifolio.put("f82r", 37);
		bifolio.put("f82v", 37);
		bifolio.put("f83r", 36);
		bifolio.put("f83v", 36);
		bifolio.put("f84r", 35);
		bifolio.put("f84v", 35);
		bifolio.put("f85r1", 40);
		bifolio.put("f85r2", 40);
		bifolio.put("fRos", 40);
		bifolio.put("f86v4", 40);
		bifolio.put("f86v6", 40);
		bifolio.put("f86v5", 40);
		bifolio.put("f86v3", 40);
		bifolio.put("f87r", 41);
		bifolio.put("f87v", 41);
		bifolio.put("f88r", 42);
		bifolio.put("f88v", 42);
		bifolio.put("f89r1", 42);
		bifolio.put("f89r2", 42);
		bifolio.put("f89v2", 42);
		bifolio.put("f89v1", 42);
		bifolio.put("f90r1", 41);
		bifolio.put("f90r2", 41);
		bifolio.put("f90v2", 41);
		bifolio.put("f90v1", 41);
		bifolio.put("f93r", 43);
		bifolio.put("f93v", 43);
		bifolio.put("f94r", 44);
		bifolio.put("f94v", 44);
		bifolio.put("f95r1", 44);
		bifolio.put("f95r2", 44);
		bifolio.put("f95v2", 44);
		bifolio.put("f95v1", 44);
		bifolio.put("f96r", 43);
		bifolio.put("f96v", 43);
		bifolio.put("f99r", 45);
		bifolio.put("f99v", 45);
		bifolio.put("f100r", 46);
		bifolio.put("f100v", 46);
		bifolio.put("f101r", 46);
		bifolio.put("f101v", 46);
		bifolio.put("f102r1", 45);
		bifolio.put("f102r2", 45);
		bifolio.put("f102v2", 45);
		bifolio.put("f102v1", 45);
		bifolio.put("f103r", 47);
		bifolio.put("f103v", 47);
		bifolio.put("f104r", 48);
		bifolio.put("f104v", 48);
		bifolio.put("f105r", 49);
		bifolio.put("f105v", 49);
		bifolio.put("f106r", 50);
		bifolio.put("f106v", 50);
		bifolio.put("f107r", 51);
		bifolio.put("f107v", 51);
		bifolio.put("f108r", 52);
		bifolio.put("f108v", 52);
		bifolio.put("f111r", 52);
		bifolio.put("f111v", 52);
		bifolio.put("f112r", 51);
		bifolio.put("f112v", 51);
		bifolio.put("f113r", 50);
		bifolio.put("f113v", 50);
		bifolio.put("f114r", 49);
		bifolio.put("f114v", 49);
		bifolio.put("f115r", 48);
		bifolio.put("f115v", 48);
		bifolio.put("f116r", 47);
		bifolio.put("f116v", 47);
	}

	// TODO transform page header fields in page (text element) properties ? so text
	// elements can be tagged with properties

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
	public String getIllustrationType() {
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

	/**
	 * Parchment in the Voynich for this page (or -1 if it cannot be found).
	 */
	public int getParchment() {
		return bifolio.getOrDefault(id, -1);
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
	 * Based on some clustering analysis, the Voynich can be split in some
	 * clusters which are different from the Illustration Type. This returns the
	 * following:
	 * 
	 * HA1 - Huge group of Herbal A at beginning of text.
	 * 
	 * PHA - Pharma + Herbal A in same quires.
	 * 
	 * SB1 - Half of stars pages.
	 * 
	 * SB2 - Other half of stars pages.
	 * 
	 * HB - Herbal B pages.
	 * 
	 * BB1 - Biological pages.
	 * 
	 * Same as getIllustrationType() for other pages.
	 * 
	 * @param section
	 */
	public String getCluster() {
		if (getLanguage().equals("A") && getParchment() >= 1 && getParchment() <= 25)
			return "HA1";
		if (getLanguage().equals("A") && getParchment() >= 27 && getParchment() != 30)
			return "PHA";
		if (getParchment() == 47 || getParchment() == 51 || getParchment() == 52)
			return "SB1";
		if (getParchment() >= 48 && getParchment() <= 50)
			return "SB2";
		if (getIllustrationType().equals("H") && getLanguage().equals("B"))
			return "HB";
		if (getIllustrationType().equals("B"))
			return "BB1";

		return getIllustrationType();
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
	 * @param rowNum line number in the inpur file.
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
		if (obj == null)
			return false;

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
