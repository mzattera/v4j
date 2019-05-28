/**
 * 
 */
package org.v4j.text;

import org.v4j.Identifiable;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.util.Counter;

/**
 * An element of text, either composite (e.g. a paragraph) or a single token.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public interface TextElement extends Identifiable {

	/**
	 * @return the alphabet for this text element.
	 */
	public Alphabet getAlphabet();

	/**
	 * 
	 * @return text contained in this element. This includes special chracters as
	 *         <!..> comments for IVTFF files, or HTML tags for HTML files.
	 */
	public String getText();

	/**
	 * 
	 * @return pain text contained in this element; that is the text stripped off
	 *         all special chracters. For example <!..> comments in IVTFF files, or
	 *         HTML tags will be absent in the returned text.
	 */
	public String getPlainText();

	/**
	 * @return all the words in this element, with their count.
	 * 
	 * @param regularOnly counts only the words that contain only regular chars.
	 */
	public Counter<String> getWords(boolean regularOnly);
	
	/**
	 * 
	 * @param parent
	 *            get the text containing this element, if any.
	 */
	public Text<?> getParent();

	/**
	 * Set the text containing this element, if any.
	 * 
	 * @param parent
	 */
	public void setParent(Text<?> parent);
}
