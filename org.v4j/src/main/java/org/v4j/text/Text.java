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
 * A text, as a sequence of text elements of same type (e.g pages).
 * 
 * @author mzatt
 *
 */
public abstract class Text<T extends TextElement> implements TextElement {

	// Alphabet used by this text
	private Alphabet alphabet;

	// elements in this text; notice each one can itself contain other
	// elements.
	protected List<T> elements = new ArrayList<>();

	// maps from element id into corresponding element.
	protected Map<String, T> elementMap = new HashMap<>();

	protected Text() {
		this.alphabet = Alphabet.ASCII;
	}

	protected Text(Alphabet a) {
		this.alphabet = a;
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
}
