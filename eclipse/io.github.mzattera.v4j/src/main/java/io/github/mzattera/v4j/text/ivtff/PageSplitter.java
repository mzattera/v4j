/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.text.ElementSplitter;

/**
 * Splits page based on any combination of parameters in the page descriptor.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class PageSplitter implements ElementSplitter<IvtffPage> {

	private final boolean byIllustrationType;
	private final boolean byQuire;
	private final boolean byPageInQuire;
	private final boolean byParchment;
	private final boolean byLanguage;
	private final boolean byHand;
	private final boolean byKey;
	private final boolean byExtraneousWriting;
	private final boolean byCluster;

	public static class Builder {
		private boolean byIllustrationType = false;
		private boolean byQuire = false;
		private boolean byPageInQuire = false;
		private boolean byParchment = false;
		private boolean byLanguage = false;
		private boolean byHand = false;
		private boolean byKey = false;
		private boolean byExtraneousWriting = false;
		private boolean byCluster = false;

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

		public Builder byParchment() {
			this.byParchment = true;
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

		public Builder byCluster() {
			this.byCluster = true;
			return this;
		}

		public PageSplitter build() {
			return new PageSplitter(byIllustrationType, byQuire, byPageInQuire, byParchment, byLanguage, byHand, byKey,
					byExtraneousWriting, byCluster);
		}
	}

	public PageSplitter(boolean byIllustrationType, boolean byQuire, boolean byPageInQuire, boolean byParchment, boolean byLanguage,
			boolean byHand, boolean byKey, boolean byExtraneousWriting, boolean byCluster) {
		this.byIllustrationType = byIllustrationType;
		this.byQuire = byQuire;
		this.byPageInQuire = byPageInQuire;
		this.byParchment = byParchment;
		this.byLanguage = byLanguage;
		this.byHand = byHand;
		this.byKey = byKey;
		this.byExtraneousWriting = byExtraneousWriting;
		this.byCluster = byCluster;
	}

	@Override
	public String getCategory(IvtffPage element) {
		PageHeader h = element.getDescriptor();
		StringBuilder result = new StringBuilder();

		if (byIllustrationType)
			result.append(" $I=").append(h.getIllustrationType());
		if (byQuire)
			result.append(" $Q=").append(h.getQuire());
		if (byPageInQuire)
			result.append(" $P=").append(h.getPageInQuire());
		if (byParchment)
			result.append(" Parchment=").append(h.getParchment());
		if (byHand)
			result.append(" $H=").append(h.getLanguage());
		if (byLanguage)
			result.append(" $L=").append(h.getLanguage());
		if (byKey)
			result.append(" $K=").append(h.hasSequenceLikeKey());
		if (byExtraneousWriting)
			result.append(" $X=").append(h.getExtraneousWriting());
		if (byCluster)
			result.append(" Cluster=").append(h.getCluster());

		return result.toString().trim();
	}

	@Override
	public String toString() {
		return "PageSplitter [byIllustrationType=" + byIllustrationType + ", byQuire=" + byQuire + ", byPageInQuire="
				+ byPageInQuire + ", byParchment=" + byParchment + ", byLanguage=" + byLanguage + ", byHand=" + byHand
				+ ", byKey=" + byKey + ", byExtraneousWriting=" + byExtraneousWriting + ", byCluster=" + byCluster
				+ "]";
	}	
}
