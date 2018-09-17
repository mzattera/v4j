/**
 * 
 */
package org.v4j.text.ivtff;

import org.v4j.text.ElementSplitter;

/**
 * Split lines based on any combination of parameters in the locus identifier.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class LineSplitter implements ElementSplitter<IvtffLine> {

	private final boolean byPage;
	private final boolean byNumber;
	private final boolean byLocus;
	private final boolean byTranscriber;

	public class Builder {

		private boolean byPage = false;
		private boolean byNumber = false;
		private boolean byLocus = false;
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

		public Builder byTranscriber() {
			this.byTranscriber = true;
			return this;
		};

		public LineSplitter build() {
			return new LineSplitter(byPage, byNumber, byLocus, byTranscriber);
		}
	}

	public LineSplitter(boolean byPage, boolean byNumber, boolean byLocus, boolean byTranscriber) {
		this.byPage = byPage;
		this.byNumber = byNumber;
		this.byLocus = byLocus;
		this.byTranscriber = byTranscriber;
	}

	@Override
	public String getCategory(IvtffLine element) {
		LocusIdentifier h = element.getDescriptor();
		StringBuilder result = new StringBuilder();

		if (byPage)
			result.append(h.getPageId());
		if (byNumber)
			result.append('.').append(h.getNumber());
		if (byLocus)
			result.append(',').append(h.getLocus());
		if (byTranscriber)
			result.append(';').append(h.getPageId());

		return result.toString();
	}
}