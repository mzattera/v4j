package org.v4j.manuscript.processing;

import org.v4j.text.ivtff.IvtffLine;

/**
 * DocumentSplitters are used to split a document into several parts using
 * Document.splitLines(). Each document part will consist of the lines in the
 * original document with same key, the key is the value returned by the
 * DocumentSplitter.getKey() method. To define a split rule, simply implement
 * this interface and have getKey() return same value for the lines that must go
 * into the same part.
 *
 * @author maxi
 */
public abstract class DocumentSplitter {
	public abstract String getKey(IvtffLine line);
}
