package io.github.mzattera.v4j.transformations;

import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;

/**
 * Represents a transformation stating that a char at a given slot position has
 * changed.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class InsertAtSlotTransformation implements Transformations.Transformation {
	private final int slot; // 0-21, covering separable words
	private final String insertedChar;

	public InsertAtSlotTransformation(int slot, String insertedChar) {
		this.slot = slot;
		this.insertedChar = insertedChar;
	}

	public int slot() {
		return slot;
	}

	public String insertedChar() {
		return insertedChar;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InsertAtSlotTransformation o2 = (InsertAtSlotTransformation) o;
		return (this.slot == o2.slot) && (this.insertedChar == o2.insertedChar());
	}

	/**
	 * @return Given word after applying transformation or null if the
	 *         transformation does not apply.
	 */
	@Override
	public String revert(String w) {

		TermDecomposition d = SlotAlphabet.decompose(w);
		if (d.classification == TermClassification.UNSTRUCTURED)
			return null;

		int slotCount = SlotAlphabet.SLOTS.size();
		int part, slot;
		String insertedChar;
		if (this.slot < slotCount) {
			part = 0;
			slot = this.slot;
			insertedChar = d.slots1[slot];
		} else {
			part = 1;
			slot = this.slot - slotCount;
			insertedChar = d.slots2[slot];
		}

		if (!insertedChar.equals(this.insertedChar))
			return null;

		String[] newWord = new String[slotCount * 2];
		System.arraycopy(d.slots1, 0, newWord, 0, d.slots1.length);
		System.arraycopy(d.slots2, 0, newWord, d.slots1.length, d.slots2.length);
		newWord[part * slotCount + slot] = "";

		return String.join("", newWord);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(slot, insertedChar);
	}

	@Override
	public String toString() {
		return "Insert '" + insertedChar + "' in slot " + slot;
	}

	@Override
	public String toListElement() {
		return "l.add(new InsertAtSlotAnalysis.Transformation(" + slot + ", \"" + insertedChar + "\"));";
	}
}