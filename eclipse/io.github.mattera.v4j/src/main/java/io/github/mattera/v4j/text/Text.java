/**
 * 
 */
package io.github.mattera.v4j.text;

import io.github.mattera.v4j.Identifiable;
import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.util.Counter;
import io.github.mattera.v4j.util.StringUtil;

/**
 * An element of text, either composite (e.g. a paragraph) or a single token.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class Text implements Identifiable {

	protected Alphabet alphabet;

	/**
	 * @return the alphabet for this text element.
	 */
	public Alphabet getAlphabet() {
		return alphabet;
	}
	
	private CompositeText<?> parent = null;

	/**
	 * @return if this text is part of a ComposedText, returns the containing text.
	 */
	public CompositeText<?> getParent() {
		return parent;
	}

	/**
	 * Set the text containing this element, if any. If parent is not null, it means
	 * this text is part of the ComposedText passed as parameter.
	 * 
	 * @param parent
	 */
	public void setParent(CompositeText<?> parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return text contained in this element. This includes special characters as
	 *         <!..> comments for IVTFF files, or HTML tags for HTML files.
	 */
	public abstract String getText();

	/**
	 * The default implementation of this method simply calls Alphabeth.toPlainText().
	 * 
	 * @return plain text contained in this element; that is the text stripped off
	 *         of all meta-data and markup. For example <!..> comments in IVTFF files, or
	 *         HTML tags will be absent in the returned text.
	 *         In addition, sequence of word separator chars will be replaced by a single instance of Alphabet.getSpace().
	 */
	public String getPlainText() {
		return getAlphabet().toPlainText(getText());
	}

	/**
	 * Counts regular characters contained in the plain text.
	 * 
	 * @return all the character in this text, with their count.
	 */
	// TODO make sure it is tested
	public Counter<Character> getChars() {
		Counter<Character> result = new Counter<>();

		for (char c : getPlainText().toCharArray()) {
			if (getAlphabet().isRegular(c))
				result.count(c);
		}

		return result;
	}

	/**
	 * 
	 * @return the plain text split by words at word separators.
	 */
	public String[] splitWords() {
		return StringUtil.splitWords(getPlainText(), getAlphabet());
	}

	/**
	 * Return all words in the text; the plain text is split in words using the default space char.
	 * 
	 * @return all the words in this text, with their count.
	 * 
	 * @param readableOnly
	 *            if true, counts only the words that do not contain any unreadable characters.
	 */
	public Counter<String> getWords(boolean readableOnly) {
		Counter<String> result = new Counter<>();

		for (String w : splitWords()) {
			if (!readableOnly || getAlphabet().hasOnlyRegular(w))
				result.count(w);
		}

		return result;
	}

	@Override
	public String toString() {
		return getText();
	}
}