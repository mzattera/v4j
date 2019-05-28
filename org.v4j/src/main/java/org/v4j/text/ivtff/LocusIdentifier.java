package org.v4j.text.ivtff;

import org.v4j.Identifiable;

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
	 * @return locus for this line (locus code + descriptor).
	 */
	// TODO getters fro the code and the decriptor separately
	public String getLocus() {
		return locus;
	}

	private final String transcriber;

	/**
	 * @return transcriber (i.e. the letter after ; in LL part of ID).
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