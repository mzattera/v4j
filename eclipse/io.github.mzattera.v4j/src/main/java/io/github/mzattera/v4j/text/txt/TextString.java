/* Copyright (c) 2020-2022 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.text.txt;

import java.util.Map.Entry;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.util.Counter;

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

	/**
	 * Creates a "dummy" text from a Counter. The text will contain words in given
	 * counter, each appearing the corresponding number of times. This constructor
	 * is used as an utility to create a text that can be analyzed by existing
	 * methods.
	 */
	public TextString(Counter<String> c) {
		this(c, "<UNK>", Alphabet.UTF_16);
	}

	/**
	 * Creates a "dummy" text from a Counter. The text will contain words in given
	 * counter, each appearing the corresponding number of times. This constructor
	 * is used as an utility to create a text that can be analyzed by existing
	 * methods.
	 */
	public TextString(Counter<String> c, String ID) {
		this(c, ID, Alphabet.UTF_16);
	}

	/**
	 * Creates a "dummy" text from a Counter. The text will contain words in given
	 * counter, each appearing the corresponding number of times. This constructor
	 * is used as an utility to create a text that can be analyzed by existing
	 * methods.
	 */
	public TextString(Counter<String> c, Alphabet alphabet) {
		this(c, "<UNK>", alphabet);
	}

	/**
	 * Creates a "dummy" text from a Counter. The text will contain words in given
	 * counter, each appearing the corresponding number of times. This constructor
	 * is used as an utility to create a text that can be analyzed by existing
	 * methods.
	 */
	public TextString(Counter<String> c, String ID, Alphabet alphabet) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Entry<String, Integer> e : c.entrySet()) {
			for (int i = 0; i < e.getValue(); ++i) {
				if (first)
					first = false;
				else
					sb.append(alphabet.getSpace());
				sb.append(e.getKey());
			}
		}
		this.text = sb.toString();
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
