/**
 * 
 */
package org.v4j.text.ivtff;

import java.util.List;

import org.v4j.text.Text;
import org.v4j.text.alphabet.Alphabet;

/**
 * A page in the Voynich manuscript.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class IvtffPage extends IvtffElement<PageHeader, IvtffLine> {

	// Document containing this page
	private Text<?> document = null;

	@Override
	public Text<?> getParent() {
		return document;
	}

	@Override
	public void setParent(Text<?> document) {
		this.document = document;
	}

	public IvtffText getDocument() {
		return (IvtffText) getParent();
	}

	protected IvtffPage(PageHeader descriptor, Alphabet a) {
		super(descriptor, a);
	}

	/**
	 * Append a line to this page.
	 * 
	 * @param line
	 */
	protected void addLine(IvtffLine line) {
		if (!line.getDescriptor().getPageId().equals(this.getId()))
			throw new IllegalArgumentException(
					"Line " + line.getDescriptor().toString() + " cannot be added to page " + this.getId());

		super.addElement(line);
	}

	/**
	 * @return lines in page. Notice that for performance reasons we do not clone
	 *         the list, so altering the list will impact the document.
	 */
	public List<IvtffLine> getLines() {
		// TODO return an unmodifiable list instead?
		return (List<IvtffLine>) elements;
	}
}
