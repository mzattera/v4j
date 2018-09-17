/**
 * 
 */
package org.v4j.text;

/**
 * An ElementFilter is used to filter elements in a text.
 * The method Text<>.filter() will build a new document containing all and only element el for which ElementFilter<>(el) returned true.
 *
 * @author maxi
 */
public interface ElementFilter<T extends TextElement> {

	/**
	 * 
	 * @param element
	 * @return true if during filtering this element has to be kept.
	 */
	public boolean keep(T element);
}
