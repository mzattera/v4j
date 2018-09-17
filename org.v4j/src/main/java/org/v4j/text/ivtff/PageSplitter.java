/**
 * 
 */
package org.v4j.text.ivtff;

import org.v4j.text.ElementSplitter;

/**
 * Splits page based on any combination of parameters in the page descriptor.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class PageSplitter implements ElementSplitter<IvtffPage> {

	private final boolean byIllustrationType;
	private final boolean byQuire;
	private final boolean byPageInQuire;
	private final boolean byLanguage;
	private final boolean byHand;
	private final boolean byKey;
	private final boolean byExtraneousWriting;

	public class Builder {
		private boolean byIllustrationType = false;
		private boolean byQuire = false;
		private boolean byPageInQuire = false;
		private boolean byLanguage = false;
		private boolean byHand = false;
		private boolean byKey = false;
		private boolean byExtraneousWriting = false;

		public Builder() {
		}

		public Builder byIllustrationType() {
			this.byIllustrationType = true;
			return this;
		}

		public Builder byQuire() {
			this.byQuire = true;
			return this;
		}

		public Builder byPageInQuire() {
			this.byPageInQuire = true;
			return this;
		}

		public Builder language() {
			this.byLanguage = true;
			return this;
		}

		public Builder byHand() {
			this.byHand = true;
			return this;
		}

		public Builder byKey() {
			this.byKey = true;
			return this;
		}

		public Builder byExtraneousWriting() {
			this.byExtraneousWriting = true;
			return this;
		}

		public PageSplitter build() {
			return new PageSplitter(byIllustrationType, byQuire, byPageInQuire, byLanguage, byHand, byKey,
					byExtraneousWriting);
		}
	}

	public PageSplitter(boolean byIllustrationType, boolean byQuire, boolean byPageInQuire, boolean byLanguage,
			boolean byHand, boolean byKey, boolean byExtraneousWriting) {
		this.byIllustrationType = byIllustrationType;
		this.byQuire = byQuire;
		this.byPageInQuire = byPageInQuire;
		this.byLanguage = byLanguage;
		this.byHand = byHand;
		this.byKey = byKey;
		this.byExtraneousWriting = byExtraneousWriting;
	}

	@Override
	public String getCategory(IvtffPage element) {
		PageHeader h = element.getDescriptor();
		StringBuilder result = new StringBuilder();

		if (byIllustrationType)
			result.append(" $I=").append(h.getType());
		if (byQuire)
			result.append(" $Q=").append(h.getQuire());
		if (byPageInQuire)
			result.append(" $P=").append(h.getPageInQuire());
		if (byHand)
			result.append(" $H=").append(h.getLanguage());
		if (byLanguage)
			result.append(" $L=").append(h.getLanguage());
		if (byKey)
			result.append(" $K=").append(h.hasSequenceLikeKey());
		if (byExtraneousWriting)
			result.append(" $X=").append(h.getExtraneousWriting());

		return result.toString().trim();
	}
}
