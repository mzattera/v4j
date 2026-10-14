package io.github.mzattera.v4j.transformations;

import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;

/**
 * Represents a transformation stating that a char at a given slot position has
 * been removed.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class RemoveAtSlotTransformation implements Transformations.Transformation {
	private final int slot; // 0-21, covering separable words
	private final String removedChar;

	public RemoveAtSlotTransformation(int slot, String removedChar) {
		this.slot = slot;
		this.removedChar = removedChar;
	}

	public int slot() {
		return slot;
	}

	public String removedChar() {
		return removedChar;
	}

	@Override
	public String revert(String w) {

		TermDecomposition d = SlotAlphabet.decompose(w);
		if (d.classification == TermClassification.UNSTRUCTURED)
			return null;

		int slotCount = SlotAlphabet.SLOTS.size();
		int part, slot;
		String emptyChar;
		if (this.slot < slotCount) {
			part = 0;
			slot = this.slot;
			emptyChar = d.slots1[slot];
		} else {
			part = 1;
			slot = this.slot - slotCount;
			emptyChar = d.slots2[slot];
		}

		if (!emptyChar.isEmpty())
			return null;

		String[] newWord = new String[slotCount * 2];
		System.arraycopy(d.slots1, 0, newWord, 0, d.slots1.length);
		System.arraycopy(d.slots2, 0, newWord, d.slots1.length, d.slots2.length);
		newWord[part * slotCount + slot] = this.removedChar;

		return String.join("", newWord);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RemoveAtSlotTransformation o2 = (RemoveAtSlotTransformation) o;
		return (this.slot == o2.slot) && (this.removedChar == o2.removedChar());
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(slot, removedChar);
	}

	@Override
	public String toString() {
		return "Remove char '" + removedChar + " from slot " + slot;
	}

	@Override
	public String toListElement() {
		return "l.add(new RemoveAtSlotAnalysis.Transformation(" + slot + ", '" + removedChar + "'));";
	}
}