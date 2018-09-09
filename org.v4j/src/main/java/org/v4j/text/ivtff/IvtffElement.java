/**
 * 
 */
package org.v4j.text.ivtff;

import org.v4j.Identifiable;
import org.v4j.text.Text;
import org.v4j.text.TextElement;

/**
 * This represent a text element that has a descriptor (e.g. a page with its header or a line with its locus identifier).
 * 
 * @author mzatt
 *
 */
public abstract class IvtffElement<D extends Identifiable, T extends TextElement> extends Text<T> implements Identifiable {
	
	protected D descriptor;
	
	public D getDescriptor() {
		return descriptor;
	}
	
	@Override
	public String getId() {
		return descriptor.getId();
	}
	
	protected IvtffElement() {}
	
	protected IvtffElement(D descriptor) {
		this.descriptor = descriptor;
	}
}
