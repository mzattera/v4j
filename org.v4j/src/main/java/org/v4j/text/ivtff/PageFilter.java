/**
 * 
 */
package org.v4j.text.ivtff;

import org.v4j.text.ElementFilter;

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
	private final String language;
	private final String hand;
	private final Boolean hasKey;
	private final String extraneousWriting;

	public class Builder {
		private String illustrationType = null;
		private String quire = null;
		private String pageInQuire = null;
		private String language = null;
		private String hand = null;
		private Boolean hasKey = null;
		private String extraneousWriting = null;

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

		public PageFilter build() {
			return new PageFilter(illustrationType, quire, pageInQuire, language, hand, hasKey, extraneousWriting);
		}
	}

	public PageFilter(String illustrationType, String quire, String pageInQuire, String language, String hand,
			Boolean hasKey, String extraneousWriting) {
		this.illustrationType = illustrationType;
		this.quire = quire;
		this.pageInQuire = pageInQuire;
		this.language = language;
		this.hand = hand;
		this.hasKey = hasKey;
		this.extraneousWriting = extraneousWriting;
	}

	@Override
	public boolean keep(IvtffPage element) {
		PageHeader h = element.getDescriptor();

		return (illustrationType == null || illustrationType.equals(h.getType()))
				&& (quire == null || quire.equals(h.getQuire()))
				&& (pageInQuire == null || pageInQuire.equals(h.getPageInQuire()))
				&& (language == null || language.equals(h.getLanguage())) && (hand == null || hand.equals(h.getHand()))
				&& (hasKey == null || hasKey == h.hasSequenceLikeKey())
				&& (extraneousWriting == null || extraneousWriting.equals(h.getExtraneousWriting()));
	}
}
