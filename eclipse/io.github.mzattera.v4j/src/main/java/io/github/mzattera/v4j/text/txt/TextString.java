/**
 * 
 */
package io.github.mzattera.v4j.text.txt;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextString extends Text {

	private final String ID; 
	
	private final String text;
	
	private final Alphabet alphabet;
	
	/**
	 * Creates a Text from a String using UTF_16 alphabet.
	 */
	public TextString(String text) {
		this(text, "<UNK>", Alphabet.UTF_16);
	}
	
	/**
	 * Creates a Text from a String using UTF_16 alphabet and gives it given ID.
	 */
	public TextString(String text, String ID) {
		this(text, ID, Alphabet.UTF_16);
	}
	
	/**
	 * Creates a Text from a String using given alphabet.
	 */
	public TextString(String text, Alphabet alphabet) {
		this(text, "<UNK>", alphabet);
	}
	
	/**
	 * Creates a Text from a String and gives it given ID.
	 */
	public TextString(String text, String ID, Alphabet alphabet) {
		this.text = text;
		this.ID = ID;
		this.alphabet = alphabet;
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
