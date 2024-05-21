/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * A text, as a sequence of text elements of same type (e.g pages).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class CompositeText<T extends Text> extends Text {

	// elements in this text; notice each one can itself contain other
	// elements.
	protected List<T> elements = new ArrayList<>();

	/**
	 * 
	 * @return the elements composing this next, notice that the returned list is the internal representation and should not be 
	 * altered directly.
	 */
	public List<T> getElements() {
		return elements;
	}

	// maps from element id into corresponding element.
	protected Map<String, T> elementMap = new HashMap<>();

	public void addElement(T el) {
		if (elementMap.containsKey(el.getId()))
			throw new IllegalArgumentException("Duplicated ID when inserting element: " + el.getId());
	
		elements.add(el);
		elementMap.put(el.getId(), el);
		el.setParent(this);
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

	protected CompositeText() {
		this(Alphabet.UTF_16, new ArrayList<T>());
	}

	protected CompositeText(Alphabet a) {
		this(a, new ArrayList<T>());
	}

	protected CompositeText(List<T> elements) {
		this(Alphabet.UTF_16, elements);
	}

	protected CompositeText(Alphabet a, List<T> elements) {
		this.alphabet = a;
		this.elements = elements;
	}

	/**
	 * @return the text as a string; by default concatenates the text of each element, using Alphabet.getSpace() as separator.
	 */
	@Override
	public String getText() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Text el : elements) {
			if (first)
				first = false;
			else
				result.append(alphabet.getSpace());

			result.append(el.getText());
		}

		return result.toString();
	}

	/**
	 * @return the plain text as a string; by default concatenates the plain text of each element, using Alphabet.getSpace() as separator.
	 */
	@Override
	public String getPlainText() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Text el : elements) {
			if (first)
				first = false;
			else
				result.append(alphabet.getSpace());

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
	 * @param splitter
	 * @return a map from each category into corresponding elements.
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
