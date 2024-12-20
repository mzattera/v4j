/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.Identifiable;

/**
 * This describes a locus identifier in IVTFF format.
 *
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class LocusIdentifier implements Identifiable {

	private final String pageId;

	/**
	 * @return page ID.
	 */
	public String getPageId() {
		return pageId;
	}

	private final String number;

	/**
	 * @return line number.
	 */
	public String getNumber() {
		return number;
	}

	private final String locus;

	/**
	 * @return locus for this line (locator + locus type).
	 */
	public String getLocus() {
		return locus;
	}

	/**
	 * Locator is first character of locus:
	 * 
	 * @ The position of this locus is unrelated to the previous item, or not easily
	 * described by one of the following. This locator is always used for the first
	 * item on each page.
	 *
	 * + This locus is generally below the previous item. This is the most common
	 * case.
	 * 
	 * * The locus is at the start of the line below the previous item, but at the
	 * left margin, while the previous item was not.
	 * 
	 * - The locus is on the same line as the previous item, but across a drawing
	 * element (future extension, not yet used)
	 * 
	 * = The locus is on the same line as the previous item, but separated by some
	 * white space.
	 * 
	 * & Similar to = but along a circular line
	 * 
	 * ~ The same as - or =, but indicating that the vertical alignment is not good.
	 *
	 * @return locator for this line.
	 */
	public String getLocator() {
		return Character.toString(locus.charAt(0));
	}

	/**
	 * The locus type (complete type) consists of a generic type (capital letter)
	 * followed by a subtype. The valid subtypes depend on the generic type.
	 *
	 * @return locus type for this line (e.g. "P0", "La", etc.).
	 */
	public String getLocusType() {
		return locus.substring(1);
	}

	/**
	 * The generic locus type.
	 *
	 * @return generic locus type for this line (e.g. "P", "L", etc.).
	 */
	public String getGenericLocusType() {
		return Character.toString(getLocusType().charAt(0));
	}

	private final String transcriber;

	/**
	 * Return the transcriber for this line.
	 * 
	 * C: Currier plus new additions from members of the voynich list as found in
	 * the file voynich.now.
	 * 
	 *	public static final String MAJORITY_TRANSCRIBER = "m";
	 *
	 * c: (VoynichFactory.CONCORDANCE_TRANSCRIBER) "artificial" transcriber for lines that contain the "concordance" version. 
	 * 
	 * D: second choice from [|] in "C" lines.
	 * 
	 * F: First study group's (Friedman's) including various items as found in the
	 * file FSG.NEW.
	 * 
	 * G: second choice from [|] in "F" lines, mostly from [1609|16xx].
	 * 
	 * H: Takeshi Takahashi.
	 * 
	 * I: second choice from [|] in "J" lines.
	 * 
	 * J: Jim Reed.
	 * 
	 * K: Karl Kluge.
	 * 
	 * L: Don Latham'.
	 * 
	 * M: second choice from [|] in "L" lines.
	 * 
	 * m: (VoynichFactory.MAJORITY_TRANSCRIBER) "artificial" transcriber for lines that contain the "majority" version. 
	 * 
	 * N: Gabriel Landini.
	 * 
	 * P: Father Th. Petersen.
	 * 
	 * Q: second choice from [|] in "K" lines.
	 * 
	 * R: Mike Roe.
	 * 
	 * T: John Tiltman.
	 * 
	 * U: Jorge Stolfi.
	 * 
	 * V: John Grove.
	 * 
	 * X: Denis V. Mardle.
	 * 
	 * Z: Rene Zandbergen.
	 * 
	 * @return transcriber (e.g. "C").
	 * 
	 */
	public String getTranscriber() {
		return transcriber;
	}

	@Override
	public String getId() {
		StringBuilder builder = new StringBuilder();
		builder.append(pageId).append('.').append(number);
		if (!transcriber.isEmpty())
			builder.append(';').append(transcriber);
		return builder.toString();
	}

	/**
	 * Creates a new instance.
	 */
	public LocusIdentifier(String page, String number, String locus, String transcriber) {
		this.pageId = page;
		this.number = number;
		this.locus = locus;
		this.transcriber = transcriber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('<').append(pageId).append('.').append(number).append(',').append(locus);
		if (!transcriber.isEmpty())
			builder.append(';').append(transcriber);
		return builder.append('>').toString();
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