/**
 * 
 */
package io.github.mattera.v4j.text.txt;

import io.github.mattera.v4j.text.Text;
import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextString extends Text {

	private final String ID; 
	
	private final String text;
	
	private final Alphabet alphabet = Alphabet.UTF_16;
	
	/**
	 * Creates a Text from a String.
	 */
	public TextString(String text) {
		this(text, "<UNK>");
	}
	
	/**
	 * Creates a Text from a String and gives it given ID.
	 */
	public TextString(String text, String ID) {
		this.text = text;
		this.ID = ID;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Alphabet getAlphabet() {
		return alphabet;
	}

	@Override
	public String getText() {
		return text;
	}
}
