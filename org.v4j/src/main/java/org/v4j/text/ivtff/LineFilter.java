/**
 * 
 */
package org.v4j.text.ivtff;

import org.v4j.text.ElementFilter;

/**
 * Filters lines based on any combination of parameters in the locus identifier.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class LineFilter implements ElementFilter<IvtffLine> {

	private final String page;
	private final String number;
	private final String locus;
	private final String locator;
	private final String locusType;
	private final String genericLocusType;
	private final String transcriber;

	public static class Builder {

		private String page = null;
		private String number = null;
		private String locus = null;
		private String locator = null;
		private String locusType = null;
		private String genericLocusType = null;		
		private String transcriber = null;

		public Builder() {
		}

		public Builder page(String page) {
			this.page = page;
			return this;
		};

		public Builder number(String number) {
			this.number = number;
			return this;
		}

		public Builder locus(String locus) {
			this.locus = locus;
			return this;
		};

		public Builder locator(String locator) {
			this.locator = locator;
			return this;
		};

		public Builder locusType(String locusType) {
			this.locusType = locusType;
			return this;
		};

		public Builder genericLocusType(String genericLocusType) {
			this.genericLocusType = genericLocusType;
			return this;
		};

		public Builder transcriber(String transcriber) {
			this.transcriber = transcriber;
			return this;
		};

		public LineFilter build() {
			return new LineFilter(page, number, locus, locator, locusType, genericLocusType, transcriber);
		}
	}

	public LineFilter(String page, String number, String locus, String locator, String locusType, String genericLocusType, String transcriber) {
		this.page = page;
		this.number = number;
		this.locus = locus;
		this.locator = locator;
		this.locusType = locusType;
		this.genericLocusType = genericLocusType;
		this.transcriber = transcriber;
	}

	@Override
	public boolean keep(IvtffLine element) {
		LocusIdentifier h = element.getDescriptor();

		return (page == null || page.equals(h.getPageId())) && (number == null || number.equals(h.getNumber()))
				&& (locus == null || locus.equals(h.getLocus()))
				&& (locator == null || locator.equals(h.getLocator()))
				&& (locusType == null || locusType.equals(h.getLocusType()))
				&& (genericLocusType == null || genericLocusType.equals(h.getGenericLocusType()))
				&& (transcriber == null || transcriber.equals(h.getTranscriber()));
	}
}