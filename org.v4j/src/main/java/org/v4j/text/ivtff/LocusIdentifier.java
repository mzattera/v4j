package org.v4j.text.ivtff;

import org.v4j.Identifiable;

/**
 * This describes a locus identifier in IVTFF format.
 *
 * 
 * @author Massimiliano "Maxi" Zattera
 */
// TODO rename
public class LocusIdentifier implements Identifiable {

	// string version of this
	private String asString;

	/** The four main ID components */
	private String[] _part;

	/**
	 * Creates a new instance starting from a string in format <page name>.<num>
	 */
	protected LocusIdentifier(String id) throws ParseException {
		String[] parts = id.split("\\.");
		if (parts.length != 2)
			throw new ParseException("Wrong page identifier: " + id);

		init(parts[0], parts[1], "", "");
	}

	/**
	 * Creates a new instance.
	 */
	protected LocusIdentifier(String page, String number) {
		init(page, number, "", "");
	}

	/**
	 * Creates a new instance.
	 */
	public LocusIdentifier(String page, String number, String locus, String transcriber) {
		init(page, number, locus, transcriber);
	}

	private void init(String page, String number, String locus, String transcriber) {
		_part = new String[4];
		_part[0] = page;
		_part[1] = number;
		_part[2] = locus;
		_part[3] = transcriber;
		asString = "<" + _part[0] + "." + _part[1] + "," + _part[2] + (transcriber.isEmpty() ? "" : ";" + _part[3])
				+ ">";
	}

	/**
	 * @return page ID; same as getPageLocator() but without angle brackets.
	 */
	public String getPageId() {
		return _part[0];
	}

	/**
	 * @return line number.
	 */
	public String getNumber() {
		return _part[1];
	}

	/**
	 * @return locus for this line (locus code + descriptor).
	 */
	// TODO getters fro the code and the decriptor separately
	public String getLocus() {
		return _part[2];
	}

	/**
	 * @return transcriber (i.e. the letter after ; in LL part of ID).
	 */
	public String getTranscriber() {
		return _part[3];
	}

	@Override
	// TODO use StringBuffer
	public String getId() {
		return getPageId() + "." + getNumber() + "," + getLocus() + (getTranscriber().isEmpty() ? "" : ";" + getTranscriber());
	}

	@Override
	public String toString() {
		return asString;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof LocusIdentifier))
			return false;

		LocusIdentifier other = (LocusIdentifier) o;
		return this.getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
