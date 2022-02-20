package io.github.mzattera.v4j.util.statemachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a model to generate Voynich words based on ordered "slots" that can
 * contain any between a set of strings. Words are built by choosing strings
 * from the slots and concatenating them.
 * 
 * Notice that, if a slot can be empty, that needs to be explicitly declared by
 * listing the empty string as option for the slot. This is a faster way to
 * simulate a model than using StateMachine.
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SlotBasedModel {

	/**
	 * "Slots" for words; each element is a list of character combinations admitted
	 * in that slot.
	 */
	private final List<List<String>> slots;

	public SlotBasedModel(List<List<String>> slots) {
		this.slots = new ArrayList<>(slots);
	}

	public SlotBasedModel(String[][] slots) {
		this.slots = new ArrayList<>();
		for (String[] slot : slots) {
			List<String> l = Arrays.asList(slot);
			this.slots.add(l);
		}
	}

	/**
	 * @return all possible words this machine can emit from given state.
	 */
	public Set<String> emit() {

		// All terms generated up to length N, with N increasing from 0 up
		Set<String> result = new HashSet<>();

		if (slots.size() == 0)
			return result;

		// Add following slots
		for (int i = 0; i < slots.size(); ++i) {
			Set<String> w = new HashSet<>();

			if (i == 0) {
				w.addAll(slots.get(0)); // We start from the first slot
			} else {
				// Add to the substrings we have so far, all chars in slot i
				for (String s : result) {
					for (int j = 0; j < slots.get(i).size(); ++j) {
						w.add(s + slots.get(i).get(j));
					}
				}
			}

			// Now store words of length i+1
			result = w;
		}

		return result;
	}

	public static void main(String[] args) {
		SlotBasedModel m = new SlotBasedModel(new String[][] { //
				{ "e", "ee", "" }, //
				{ "d", "" }, //
		});

		for (String s : m.emit()) {
			System.out.println(">" + s + "<");
		}
	}
}
