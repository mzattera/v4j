/**
 * 
 */
package org.v4j.text;

import java.util.regex.Pattern;

import org.v4j.Identifiable;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.ParseException;
import org.v4j.util.Counter;

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

	public void setAlphabet(Alphabet a) {
		this.alphabet = a;
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
	 * @return text contained in this element. This includes special chracters as
	 *         <!..> comments for IVTFF files, or HTML tags for HTML files.
	 */
	public abstract String getText();

	/**
	 * The default implementation of this method simply calls Alphabeth.toPlainText().
	 * 
	 * @return plain text contained in this element; that is the text stripped off
	 *         all special characters. For example <!..> comments in IVTFF files, or
	 *         HTML tags will be absent in the returned text.
	 *         In addition, sequence of word separator chars will be replaced by a single instance of Alphabet.getSpace().
	 *         
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
			if (alphabet.isRegular(c))
				result.count(c);
		}

		return result;
	}

	/**
	 * The plain text is split in words using the default space char.
	 * 
	 * @return all the words in this text, with their count.
	 * 
	 * @param regularOnly
	 *            counts only the words that contain only regular chars (e.g. avoid
	 *            words with unreadable chars).
	 */
	// TODO make sure it is tested
	public Counter<String> getWords(boolean regularOnly) {
		Counter<String> result = new Counter<>();

		for (String w : getPlainText().split(Pattern.quote(alphabet.getSpace() + ""))) {
			if (!regularOnly || alphabet.hasOnlyRegular(w))
				result.count(w);
		}

		return result;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Text)) return false;
		return ((Text)o).getId().equals(this.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return getText();
	}
}