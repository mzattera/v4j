/**
 * 
 */
package org.v4j.manuscript.processing;

import org.v4j.text.ivtff.PageHeader;

/**
 * Splits a document by Courier language of the page (A, B or ?).
 * 
 * @author maxi
 *
 */
public final class LanguagePageSplitter extends PageSplitter {

	/**
	 * 
	 */
	public LanguagePageSplitter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see voynich.document.processing.PageSplitter#getKey(voynich.document.
	 * PageDescriptor)
	 */
	@Override
	public String getKey(PageHeader page) {
		return page.getLanguage();
	}
}
