/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.text.ElementSplitter;

/**
 * Split lines based on any combination of parameters in the locus identifier.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class LineSplitter implements ElementSplitter<IvtffLine> {

	private final boolean byPage;
	private final boolean byNumber;
	private final boolean byLocus;
	private final boolean byLocator;
	private final boolean byLocusType;
	private final boolean byGenericLocusType;
	private final boolean byTranscriber;

	public static class Builder {

		private boolean byPage = false;
		private boolean byNumber = false;
		private boolean byLocus = false;
		private boolean byLocator = false;
		private boolean byLocusType = false;
		private boolean byGenericLocusType = false;
		private boolean byTranscriber = false;

		public Builder() {
		}

		public Builder byPage() {
			this.byPage = true;
			return this;
		};

		public Builder byNumber() {
			this.byNumber = true;
			return this;
		}

		public Builder byLocus() {
			this.byLocus = true;
			return this;
		};

		public Builder byLocator() {
			this.byLocator = true;
			return this;
		};

		public Builder byLocusType() {
			this.byLocusType = true;
			return this;
		};

		public Builder byGenericLocusType() {
			this.byGenericLocusType = true;
			return this;
		};

		public Builder byTranscriber() {
			this.byTranscriber = true;
			return this;
		};

		public LineSplitter build() {
			return new LineSplitter(byPage, byNumber, byLocus, byLocator, byLocusType, byGenericLocusType, byTranscriber);
		}
	}

	public LineSplitter(boolean byPage, boolean byNumber, boolean byLocus, boolean byLocator, boolean byLocusType, boolean byGenericLocusType, boolean byTranscriber) {
		this.byPage = byPage;
		this.byNumber = byNumber;
		this.byLocus = byLocus;
		this.byLocator = byLocator;
		this.byLocusType = byLocusType;
		this.byGenericLocusType = byGenericLocusType;
		this.byTranscriber = byTranscriber;
	}

	@Override
	public String getCategory(IvtffLine element) {
		LocusIdentifier h = element.getDescriptor();
		StringBuilder result = new StringBuilder();

		// TODO probably use same approach used by PgeSplitter, which makes easier to get back a specific group
		if (byPage)
			result.append(h.getPageId());
		if (byNumber)
			result.append('.').append(h.getNumber());
		if (byLocus)
			result.append(',').append(h.getLocus());
		if (byLocator)
			result.append(',').append(h.getLocator());
		if (byLocusType)
			result.append(',').append(h.getLocusType());
		if (byGenericLocusType)
			result.append(',').append(h.getGenericLocusType());
		if (byTranscriber)
			result.append(';').append(h.getTranscriber());

		return result.toString();
	}

	@Override
	public String toString() {
		return "LineSplitter [byPage=" + byPage + ", byNumber=" + byNumber + ", byLocus=" + byLocus + ", byLocator="
				+ byLocator + ", byLocusType=" + byLocusType + ", byGenericLocusType=" + byGenericLocusType
				+ ", byTranscriber=" + byTranscriber + "]";
	}	
}