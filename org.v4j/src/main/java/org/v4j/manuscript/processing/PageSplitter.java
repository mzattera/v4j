package org.v4j.manuscript.processing;

import org.v4j.text.ivtff.PageHeader;

/**
 * PageSplitters are used to split a document into several parts using
 * Document.splitPages(). Each document part will consist of the pages in the
 * original document (and their lines) with same key, the key is the value
 * returned by the PageSplitter.getKey() method. To define a split rule, simply
 * implement this interface and have getKey() return same value for the pages
 * that must go into the same part.
 *
 * @author maxi
 */
public abstract class PageSplitter {
	public abstract String getKey(PageHeader page);
}
