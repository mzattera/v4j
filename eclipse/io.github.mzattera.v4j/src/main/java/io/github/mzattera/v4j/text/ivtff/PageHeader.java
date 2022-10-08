package io.github.mzattera.v4j.text.ivtff;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.Identifiable;

/**
 * This describes data about one page, it does not contain the page lines, which
 * are part of a document.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class PageHeader implements Identifiable {

	/**
	 * These are pages we know look different from others (see
	 * https://mzattera.github.io/v4j/003/)
	 */
	public static final String[] OUTLIER_PAGES = { "f27v", "f53r", "f57v", "f65r", "f68r1", "f68r2", "f72v1", "f116v" };

	/**
	 * These are parchment we know look different from others (see
	 * https://mzattera.github.io/v4j/003/)
	 */
	public static final int[] OUTLIER_PARCHMENTS = { 29, 30, 31, 32, 40 };
	
	/**
	 * List of clusters I defined, based on cluster analysis (see
	 * https://mzattera.github.io/v4j/003/)
	 */
	public static String[] CLUSTERS = { "HA", // Herbal A
			"PA", // Pharmaceutical
			"HB", // Herbal B
			"BB", // Biological
			"SB", // Stars
	};

	// Maps each folio in its corresponding parchment (or "parchment").
	private static final Map<String, Integer> parchment = new HashMap<>();
	static {
		parchment.put("f1r", 1);
		parchment.put("f1v", 1);
		parchment.put("f2r", 2);
		parchment.put("f2v", 2);
		parchment.put("f3r", 3);
		parchment.put("f3v", 3);
		parchment.put("f4r", 4);
		parchment.put("f4v", 4);
		parchment.put("f5r", 4);
		parchment.put("f5v", 4);
		parchment.put("f6r", 3);
		parchment.put("f6v", 3);
		parchment.put("f7r", 2);
		parchment.put("f7v", 2);
		parchment.put("f8r", 1);
		parchment.put("f8v", 1);
		parchment.put("f9r", 5);
		parchment.put("f9v", 5);
		parchment.put("f10r", 6);
		parchment.put("f10v", 6);
		parchment.put("f11r", 7);
		parchment.put("f11v", 7);
		parchment.put("f13r", 8);
		parchment.put("f13v", 8);
		parchment.put("f14r", 7);
		parchment.put("f14v", 7);
		parchment.put("f15r", 6);
		parchment.put("f15v", 6);
		parchment.put("f16r", 5);
		parchment.put("f16v", 5);
		parchment.put("f17r", 9);
		parchment.put("f17v", 9);
		parchment.put("f18r", 10);
		parchment.put("f18v", 10);
		parchment.put("f19r", 11);
		parchment.put("f19v", 11);
		parchment.put("f20r", 12);
		parchment.put("f20v", 12);
		parchment.put("f21r", 12);
		parchment.put("f21v", 12);
		parchment.put("f22r", 11);
		parchment.put("f22v", 11);
		parchment.put("f23r", 10);
		parchment.put("f23v", 10);
		parchment.put("f24r", 9);
		parchment.put("f24v", 9);
		parchment.put("f25r", 13);
		parchment.put("f25v", 13);
		parchment.put("f26r", 14);
		parchment.put("f26v", 14);
		parchment.put("f27r", 15);
		parchment.put("f27v", 15);
		parchment.put("f28r", 16);
		parchment.put("f28v", 16);
		parchment.put("f29r", 16);
		parchment.put("f29v", 16);
		parchment.put("f30r", 15);
		parchment.put("f30v", 15);
		parchment.put("f31r", 14);
		parchment.put("f31v", 14);
		parchment.put("f32r", 13);
		parchment.put("f32v", 13);
		parchment.put("f33r", 17);
		parchment.put("f33v", 17);
		parchment.put("f34r", 18);
		parchment.put("f34v", 18);
		parchment.put("f35r", 19);
		parchment.put("f35v", 19);
		parchment.put("f36r", 20);
		parchment.put("f36v", 20);
		parchment.put("f37r", 20);
		parchment.put("f37v", 20);
		parchment.put("f38r", 19);
		parchment.put("f38v", 19);
		parchment.put("f39r", 18);
		parchment.put("f39v", 18);
		parchment.put("f40r", 17);
		parchment.put("f40v", 17);
		parchment.put("f41r", 21);
		parchment.put("f41v", 21);
		parchment.put("f42r", 22);
		parchment.put("f42v", 22);
		parchment.put("f43r", 23);
		parchment.put("f43v", 23);
		parchment.put("f44r", 24);
		parchment.put("f44v", 24);
		parchment.put("f45r", 24);
		parchment.put("f45v", 24);
		parchment.put("f46r", 23);
		parchment.put("f46v", 23);
		parchment.put("f47r", 22);
		parchment.put("f47v", 22);
		parchment.put("f48r", 21);
		parchment.put("f48v", 21);
		parchment.put("f49r", 25);
		parchment.put("f49v", 25);
		parchment.put("f50r", 26);
		parchment.put("f50v", 26);
		parchment.put("f51r", 27);
		parchment.put("f51v", 27);
		parchment.put("f52r", 28);
		parchment.put("f52v", 28);
		parchment.put("f53r", 28);
		parchment.put("f53v", 28);
		parchment.put("f54r", 27);
		parchment.put("f54v", 27);
		parchment.put("f55r", 26);
		parchment.put("f55v", 26);
		parchment.put("f56r", 25);
		parchment.put("f56v", 25);
		parchment.put("f57r", 29);
		parchment.put("f57v", 29);
		parchment.put("f58r", 30);
		parchment.put("f58v", 30);
		parchment.put("f65r", 30);
		parchment.put("f65v", 30);
		parchment.put("f66r", 29);
		parchment.put("f66v", 29);
		parchment.put("f67r1", 31);
		parchment.put("f67r2", 31);
		parchment.put("f67v2", 31);
		parchment.put("f67v1", 31);
		parchment.put("f68r1", 31);
		parchment.put("f68r2", 31);
		parchment.put("f68r3", 31);
		parchment.put("f68v3", 31);
		parchment.put("f68v2", 31);
		parchment.put("f68v1", 31);
		parchment.put("f69r", 32);
		parchment.put("f69v", 32);
		parchment.put("f70r1", 32);
		parchment.put("f70r2", 32);
		parchment.put("f70v2", 32);
		parchment.put("f70v1", 32);
		parchment.put("f71r", 33);
		parchment.put("f71v", 33);
		parchment.put("f72r1", 33);
		parchment.put("f72r2", 33);
		parchment.put("f72r3", 33);
		parchment.put("f72v3", 33);
		parchment.put("f72v2", 33);
		parchment.put("f72v1", 33);
		parchment.put("f73r", 34);
		parchment.put("f73v", 34);
		parchment.put("f75r", 35);
		parchment.put("f75v", 35);
		parchment.put("f76r", 36);
		parchment.put("f76v", 36);
		parchment.put("f77r", 37);
		parchment.put("f77v", 37);
		parchment.put("f78r", 38);
		parchment.put("f78v", 38);
		parchment.put("f79r", 39);
		parchment.put("f79v", 39);
		parchment.put("f80r", 39);
		parchment.put("f80v", 39);
		parchment.put("f81r", 38);
		parchment.put("f81v", 38);
		parchment.put("f82r", 37);
		parchment.put("f82v", 37);
		parchment.put("f83r", 36);
		parchment.put("f83v", 36);
		parchment.put("f84r", 35);
		parchment.put("f84v", 35);
		parchment.put("f85r1", 40);
		parchment.put("f85r2", 40);
		parchment.put("fRos", 40);
		parchment.put("f86v4", 40);
		parchment.put("f86v6", 40);
		parchment.put("f86v5", 40);
		parchment.put("f86v3", 40);
		parchment.put("f87r", 41);
		parchment.put("f87v", 41);
		parchment.put("f88r", 42);
		parchment.put("f88v", 42);
		parchment.put("f89r1", 42);
		parchment.put("f89r2", 42);
		parchment.put("f89v2", 42);
		parchment.put("f89v1", 42);
		parchment.put("f90r1", 41);
		parchment.put("f90r2", 41);
		parchment.put("f90v2", 41);
		parchment.put("f90v1", 41);
		parchment.put("f93r", 43);
		parchment.put("f93v", 43);
		parchment.put("f94r", 44);
		parchment.put("f94v", 44);
		parchment.put("f95r1", 44);
		parchment.put("f95r2", 44);
		parchment.put("f95v2", 44);
		parchment.put("f95v1", 44);
		parchment.put("f96r", 43);
		parchment.put("f96v", 43);
		parchment.put("f99r", 45);
		parchment.put("f99v", 45);
		parchment.put("f100r", 46);
		parchment.put("f100v", 46);
		parchment.put("f101r", 46);
		parchment.put("f101v", 46);
		parchment.put("f102r1", 45);
		parchment.put("f102r2", 45);
		parchment.put("f102v2", 45);
		parchment.put("f102v1", 45);
		parchment.put("f103r", 47);
		parchment.put("f103v", 47);
		parchment.put("f104r", 48);
		parchment.put("f104v", 48);
		parchment.put("f105r", 49);
		parchment.put("f105v", 49);
		parchment.put("f106r", 50);
		parchment.put("f106v", 50);
		parchment.put("f107r", 51);
		parchment.put("f107v", 51);
		parchment.put("f108r", 52);
		parchment.put("f108v", 52);
		parchment.put("f111r", 52);
		parchment.put("f111v", 52);
		parchment.put("f112r", 51);
		parchment.put("f112v", 51);
		parchment.put("f113r", 50);
		parchment.put("f113v", 50);
		parchment.put("f114r", 49);
		parchment.put("f114v", 49);
		parchment.put("f115r", 48);
		parchment.put("f115v", 48);
		parchment.put("f116r", 47);
		parchment.put("f116v", 47);
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
	 * Illustration type (T,H,A,Z,B,C,P,S) for Text, Herbal, Astronomical, Zodiac,
	 * Biological, Cosmological, Pharmaceutical or Stars respectively.
	 */
	public String getIllustrationType() {
		return illustrationType;
	}

	private final String quire;

	/**
	 * Quire where the page resides (A-T).
	 */
	public String getQuire() {
		return quire;
	}

	private final String pageInQuire;

	/**
	 * Positoion of page in quire (A-X).
	 */
	public String getPageInQuire() {
		return pageInQuire;
	}

	/**
	 * Parchment in the Voynich for this page (or -1 if it cannot be found).
	 */
	public int getParchment() {
		return parchment.getOrDefault(id, -1);
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
	 * Based on some clustering analysis (see https://mzattera.github.io/v4j/003/),
	 * we split the Voynich in clusters:
	 * 
	 * HA: Herbal A
	 * 
	 * PA: Pharmaceutical
	 * 
	 * HB: Herbal B
	 * 
	 * BB: Biological
	 * 
	 * SB: Stars
	 * 
	 * @returns assigned cluster for the page, or "?" if the page wasn't assigned
	 *          any cluster (e.g. for outliers pages).
	 */
	public String getCluster() {

		// Page outliers
		for (String o : OUTLIER_PAGES) {
			if (getId().equals(o))
				return "?";
		}

		// Parchment outliers
		for (int p : OUTLIER_PARCHMENTS) {
			if ((getParchment() == -1) || (getParchment() == p))
				return "?";
		}

		String result = getIllustrationType() + getLanguage();
		for (String c : CLUSTERS)
			if (c.equals(result)) return result;
		
		return "?";
	}

	/**
	 * @returns Illustration type + Currier Language (e.g. "HA" or "Z?").
	 */
	public String getIllustrationAndLanguage() {
		return getIllustrationType() + getLanguage();
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
	 * @param rowNum line number in the input file.
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
		if (hasKey) txt.append("$K=Y ");
		txt.append("$X=").append(extraneousWriting).append(">");
		return txt.toString();
	}
}
