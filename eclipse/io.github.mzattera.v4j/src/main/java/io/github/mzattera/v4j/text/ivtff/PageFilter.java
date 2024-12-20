/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.text.ElementFilter;

/**
 * Filters a page based on any combination of parameters in the page descriptor.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class PageFilter implements ElementFilter<IvtffPage> {

	private final String illustrationType;
	private final String quire;
	private final String pageInQuire;
	private final int parchment;
	private final String language;
	private final String hand;
	private final Boolean hasKey;
	private final String extraneousWriting;
	private final String cluster;

	public static class Builder {
		private String illustrationType = null;
		private String quire = null;
		private String pageInQuire = null;
		private int parchment = -1;
		private String language = null;
		private String hand = null;
		private Boolean hasKey = null;
		private String extraneousWriting = null;
		private String cluster = null;

		public Builder() {
		}

		public Builder illustrationType(String illustrationType) {
			this.illustrationType = illustrationType;
			return this;
		}

		public Builder quire(String quire) {
			this.quire = quire;
			return this;
		}

		public Builder pageInQuire(String pageInQuire) {
			this.pageInQuire = pageInQuire;
			return this;
		}

		public Builder parchment(int parchment) {
			this.parchment = parchment;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public Builder hand(String hand) {
			this.hand = hand;
			return this;
		}

		public Builder hasKey(Boolean hasKey) {
			this.hasKey = hasKey;
			return this;
		}

		public Builder extraneousWriting(String extraneousWriting) {
			this.extraneousWriting = extraneousWriting;
			return this;
		}

		public Builder cluster(String cluster) {
			this.cluster = cluster;
			return this;
		}

		public PageFilter build() {
			return new PageFilter(illustrationType, quire, pageInQuire, parchment, language, hand, hasKey,
					extraneousWriting, cluster);
		}
	}

	public PageFilter(String illustrationType, String quire, String pageInQuire, int parchment, String language,
			String hand, Boolean hasKey, String extraneousWriting, String cluster) {
		this.illustrationType = illustrationType;
		this.quire = quire;
		this.pageInQuire = pageInQuire;
		this.parchment = parchment;
		this.language = language;
		this.hand = hand;
		this.hasKey = hasKey;
		this.extraneousWriting = extraneousWriting;
		this.cluster = cluster;
	}

	@Override
	public boolean keep(IvtffPage element) {
		PageHeader h = element.getDescriptor();

		return (illustrationType == null || illustrationType.equals(h.getIllustrationType()))
				&& (quire == null || quire.equals(h.getQuire()))
				&& (pageInQuire == null || pageInQuire.equals(h.getPageInQuire()))
				&& (parchment == -1 || parchment == h.getParchment())
				&& (language == null || language.equals(h.getLanguage())) && (hand == null || hand.equals(h.getHand()))
				&& (hasKey == null || hasKey == h.hasSequenceLikeKey())
				&& (extraneousWriting == null || extraneousWriting.equals(h.getExtraneousWriting()))
				&& (cluster == null || cluster.equals(h.getCluster()));
	}

	@Override
	public String toString() {
		return "PageFilter [" + (illustrationType != null ? "illustrationType=" + illustrationType + ", " : "")
				+ (quire != null ? "quire=" + quire + ", " : "")
				+ (pageInQuire != null ? "pageInQuire=" + pageInQuire + ", " : "")
				+ (parchment != -1 ? "parchment=" + parchment + ", " : "")
				+ (language != null ? "language=" + language + ", " : "") + (hand != null ? "hand=" + hand + ", " : "")
				+ (hasKey != null ? "hasKey=" + hasKey + ", " : "")
				+ (extraneousWriting != null ? "extraneousWriting=" + extraneousWriting + ", " : "")
				+ (cluster != null ? "cluster=" + cluster : "") + "]";
	}
}
