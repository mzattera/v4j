/**
 * 
 */
package org.v4j.text.ivtff;

import java.util.List;

import org.v4j.text.Text;

/**
 * A page in the Voynich manuscript.
 * 
 * @author mzatt
 *
 */
public class IvtffPage extends IvtffElement<PageHeader, IvtffLine> {

	// Document containing this page
	private Text<?> document = null;

	@Override
	public Text<?> getParent() {
		return document;
	}

	@Override
	public void setParent(Text<?> document) {
		this.document = document;
	}

	public IvtffText getDocument()
	{
		return (IvtffText)getParent();
	}

	protected IvtffPage(PageHeader descriptor) {
		super(descriptor);
	}

	/**
	 * Append a line to this page.
	 * 
	 * @param line
	 */
	protected void addLine(IvtffLine line) {
		if (!line.getDescriptor().getPageId().equals(this.getId()))
			throw new IllegalArgumentException(
					"Line " + line.getDescriptor().toString() + " cannot be added to page " + this.getId());

		super.addElement(line);
	}

	/**
	 * Return lines. Notice that for performance reasons we do not clone the list,
	 * so alterign the list will impact the docuemnt.
	 */
	public List<IvtffLine> getLines() {
		return (List<IvtffLine>) elements;
	}
}
