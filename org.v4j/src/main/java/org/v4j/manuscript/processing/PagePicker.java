package org.v4j.manuscript.processing;

import org.v4j.text.ivtff.PageHeader;

/**
 * PagePickers are used to select pages from a Document.
 * Document.pickPages(PagePicker) will return a new document with all and only
 * the pages fro wich the PagePicker returned true. To define a pick rule,
 * simply implement this interface.
 *
 * @author maxi
 */
public abstract class PagePicker {

	/***
	 * Returns true if the page has to be picked and put in the returned document.
	 * 
	 * @param page
	 * @return
	 */
	public abstract boolean pickPage(PageHeader page);
}
