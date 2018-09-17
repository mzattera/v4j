/**
 * 
 */
package org.v4j.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.v4j.text.alphabet.Alphabet;

/**
 * A text, as a sequence of text elements of same type (e.g pages). It is
 * assumed text is organized in lines.
 * 
 * @author mzatt
 *
 */
public abstract class Text<T extends TextElement> implements TextElement {

	private final String id;

	/**
	 * @return Unique ID for the Text; might be null.
	 */
	@Override	
	public String getId() {
		return id;
	}

	// Alphabet used by this text
	private Alphabet alphabet;

	// elements in this text; notice each one can itself contain other
	// elements.
	protected List<T> elements = new ArrayList<>();

	// maps from element id into corresponding element.
	protected Map<String, T> elementMap = new HashMap<>();

	protected Text() {
		this(null, Alphabet.ASCII, new ArrayList<T>());
	}
	
	protected Text(String id) {
		this(id, Alphabet.ASCII, new ArrayList<T>());
	}

	protected Text(String id, Alphabet a) {
		this(id, a, new ArrayList<T>());
	}

	protected Text(String id, Alphabet a, List<T> elements) {
		this.id = id;
		this.alphabet = a;
		this.elements = elements;
	}

	/**
	 * 
	 * @param id
	 * @return element with given ID; notice this looks only to the elements
	 *         contained directly in this instance and does not recourse into them.
	 */
	public T getElement(String id) {
		return elementMap.get(id);
	}

	public void addElement(T el) {
		if (elementMap.containsKey(el.getId()))
			throw new IllegalArgumentException("Duplicated ID when inserting element: " + el.getId());

		elements.add(el);
		elementMap.put(el.getId(), el);
		el.setParent(this);
	}

	@Override
	public Alphabet getAlphabet() {
		return alphabet;
	}

	protected void setAlphabet(Alphabet a) {
		this.alphabet = a;
	}

	@Override
	public String getText() {
		StringBuilder result = new StringBuilder();
		boolean first = false;
		for (TextElement el : elements) {
			if (first)
				first = false;
			else
				result.append("\n");

			result.append(el.getText());
		}

		return result.toString();
	}

	@Override
	public String getPlainText() {
		StringBuilder result = new StringBuilder();
		boolean first = false;
		for (TextElement el : elements) {
			if (first)
				first = false;
			else
				result.append("\n");

			result.append(el.getPlainText());
		}

		return result.toString();
	}

	/**
	 * 
	 * @param filter
	 * @return the elements in this text for which filter.keep() returned true.
	 */
	public List<T> filterElements(ElementFilter<T> filter) {
		List<T> toKeep = new ArrayList<>();
		for (T element : elements)
			if (filter.keep(element))
				toKeep.add(element);

		return toKeep;
	}

	/**
	 * Splits elements in this Text accordingly to the categories introduced by given splitter.
	 * 
	 * @param filter
	 * @return a map from each category into corresponding elments.
	 */
	public Map<String, List<T>> splitElements(ElementSplitter<T> splitter) {
		Map<String, List<T>> result = new HashMap<>();
	
		for (T element : elements) {
			String category = splitter.getCategory(element);
			List<T> l = result.get(category);
			
			if (l == null) {
				l = new ArrayList<T>();
				result.put(category, l);
			}

			l.add(element);
		}
		
		return result;
	}
}
