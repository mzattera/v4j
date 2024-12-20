/* Copyright (c) 2019-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.txt;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * This represents a line of text.
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextLine extends Text {

	private final String text;

	/* (non-Javadoc)
	 * @see io.github.mzattera.v4j.text.Text#getText()
	 */
	@Override
	public String getText() {
		return text;
	}

	private final int rowNumber;
	
	/**
	 * 
	 * @return row number for this line of text. Notice this is assigned at creation and never changes.
	 */
	public int getRowNumber() {
		return rowNumber;
	}
		
	@Override
	public String getId() {
		return String.valueOf(rowNumber);
	}

	public TextLine(int rowNumber, String text) {
		this.rowNumber = rowNumber;
		this.text = text;
	}

	@Override
	public Alphabet getAlphabet() {
		return getParent().getAlphabet();
	}
}
