/**
 * 
 */
package io.github.mzattera.v4j.text;

/**
 * An ElementFilter is used to filter elements in a text.
 * The method Text<>.filter() will build a new document containing all and only element el for which ElementFilter<>(el) returned true.
 *
 * @author Massimiliano "Maxi" Zattera
 */
public interface ElementFilter<T extends Text> {

	/**
	 * 
	 * @param element
	 * @return true if during filtering this element has to be kept.
	 */
	public boolean keep(T element);
}
