/**
 * 
 */
package io.github.mattera.v4j.text;

/**
 * An ElementSplitter is used when splitting elements of a Text.
 * 
 * For each element, the spliter returns a "category" for the element, representes as a string value.
 * Elements are split accordingly to their category.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public interface ElementSplitter<T extends Text> {

	public String getCategory (T element);
}
