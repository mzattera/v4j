/* Copyright (c) 2018-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import io.github.mzattera.v4j.Identifiable;
import io.github.mzattera.v4j.text.CompositeText;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * This represent a text element that has a descriptor (e.g. a page with its
 * header or a line with its locus identifier).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class IvtffElement<D extends Identifiable, T extends Text> extends CompositeText<T>
		implements Identifiable {

	protected D descriptor;

	public D getDescriptor() {
		return descriptor;
	}

	@Override
	public String getId() {
		return descriptor.getId();
	}

	protected IvtffElement(Alphabet a) {
		super(a);
	}

	protected IvtffElement(D descriptor, Alphabet a) {
		super(a);
		this.descriptor = descriptor;
	}
}
