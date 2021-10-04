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
	 * The default implementation of this method simply calls
	 * Alphabeth.toPlainText().
	 * 
	 * @return plain text contained in this element; that is the text stripped off
	 *         of all meta-data and markup. For example <!..> comments in IVTFF
	 *         files, or HTML tags will be absent in the returned text. In addition,
	 *         sequence of word separator chars will be replaced by a single
	 *         instance of Alphabet.getSpace().
	 */
	public String getPlainText() {
		return getAlphabet().toPlainText(getText());
	}

	/**
	 * Counts regular characters contained in given string. The string is assumed to
	 * use given alphabet. *Notice* the string is not processed in any way,
	 * therefore all characters appearing as regular characters in the alphabet are
	 * counted; e.g. text of comments is counted.
	 * 
	 * @return all the regular character in given string, with their count.
	 */
	// TODO make sure it is tested
	public static Counter<Character> getChars(String txt, Alphabet a) {
				return getChars(txt, a, false);
	}

	/**
	 * Counts regular characters contained in given string. The string is assumed to
	 * use given alphabet. *Notice* the string is not processed in any way,
	 * therefore any character appearing as regular characters in the alphabet is
	 * counted; e.g. text of IVTFF comments is counted.
	 * 
	 * @param toUpper If true, will convert text to upper case, if supported, before
	 *                building the distribution.
	 * 
	 * @return all the regular character in given string, with their count.
	 */
	// TODO make sure it is tested
	public static Counter<Character> getChars(String txt, Alphabet a, boolean toUpper) {
		if (toUpper) {
			try {
				return  Text.getChars(a.toUpperCase(txt), a, false);
			} catch (UnsupportedOperationException e) {
				// toUpper() not supported in this alphabet
			}
		} 
		
		Counter<Character> result = new Counter<>();
		for (char c : txt.toCharArray()) {
			if (a.isRegular(c))
				result.count(c);
		}
		
		return result;
	}

	/**
	 * Counts regular characters contained in the plain text.
	 * 
	 * @return all the regular character in this text, with their count.
	 */
	// TODO make sure it is tested
	public Counter<Character> getChars() {
		return Text.getChars(getPlainText(), getAlphabet(), false);
	}

	/**
	 * Counts regular characters contained in the plain text.
	 * 
	 * @param toUpper If true, will convert text to upper case, if supported, before
	 *                building the distribution.
	 * 
	 * @return all the regular character in this text, with their count.
	 */
	// TODO make sure it is tested
	public Counter<Character> getChars(boolean toUpper) {
		return Text.getChars(getPlainText(), getAlphabet(), toUpper);
	}

	/**
	 * 
	 * @return the plain text split by words at word separators.
	 */
	public String[] splitWords() {
		return StringUtil.splitWords(getPlainText(), getAlphabet());
	}

	/**
	 * Return all words in the text; the plain text is split in words using the
	 * default space char.
	 * 
	 * @return all the words in this text, with their count.
	 * 
	 * @param readableOnly if true, counts only the words that do not contain any
	 *                     unreadable characters.
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