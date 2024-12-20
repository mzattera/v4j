/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * A page in the Voynich manuscript.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class IvtffPage extends IvtffElement<PageHeader, IvtffLine> {

	public IvtffText getDocument() {
		return (IvtffText) getParent();
	}

	public IvtffPage(PageHeader descriptor, Alphabet a) {
		super(descriptor, a);
	}

	/**
	 * Append a line to this page.
	 * 
	 * @param line
	 */
	@Override
	public void addElement(IvtffLine line) {
		if (!line.getDescriptor().getPageId().equals(this.getId()))
			throw new IllegalArgumentException(
					"Line " + line.getDescriptor().toString() + " cannot be added to page " + this.getId());

		super.addElement(line);
	}
}
