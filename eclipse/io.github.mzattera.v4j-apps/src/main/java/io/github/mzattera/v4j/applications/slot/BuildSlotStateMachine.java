/**
 * 
 */
package io.github.mzattera.v4j.applications.slot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.statemachine.State;
import io.github.mzattera.v4j.util.statemachine.StateMachine;
import io.github.mzattera.v4j.util.statemachine.StateMachine.TrainMode;

/**
 * Builds a state machine that best describes Voynich words, by leveraging the
 * Slots concept.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class BuildSlotStateMachine {

	// Folder where created wirtual machines will be stored
	private static final String OUT_FOLDER = "D:\\";

	// Minimum weight for an edge before being discarded when building the SM
	private static final int MIN_WEIGTH = 5;

	/** Name of begin state. */
	private static final String INITIAL_STATE = "<BEGIN>";

	/** Name of end state. */
	public static final String END_STATE = "<END>";

	// Cluster to limit modeling to....or null.
	private static final String CLUSTER = null;

	/** Transcription to use */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/** Transcription to use */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = (CLUSTER == null) ? null
			: new PageFilter.Builder().cluster("HA").build();

	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Filter            : " + (FILTER == null ? "None" : FILTER.toString()));
			System.out.println("Min Weight        : " + MIN_WEIGTH);
			System.out.println();

			// Get the document to process
			IvtffText voynich = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.SLOT);
			if (FILTER != null)
				voynich = voynich.filterPages(FILTER);

			process(voynich, OUT_FOLDER, MIN_WEIGTH);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * Peturns a state machine that models given document vocabulary.
	 * 
	 * @param doc       The document to model (must use slot alphabet).
	 * @param outFolder Output folder where to put the state machine being created
	 *                  at different stages.
	 * @param minWeigth Each edge that has weight below this (that is it does not
	 *                  generate this number of terms in the initial setup) will be
	 *                  discarded.
	 * 
	 * @return best version of the state machine (kinda).
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public static StateMachine process(IvtffText doc, String outFolder, int minWeigth)
			throws IOException, ParseException {

		if (!doc.getAlphabet().equals(Alphabet.SLOT))
			throw new IllegalArgumentException();

		return process(doc.getWords(true), outFolder, minWeigth, true);
	}

	/**
	 * Returns a state machine that models given set of tokens.
	 * 
	 * @param voynichTokens Words to model (must use slot alphabet).
	 * @param outFolder     Output folder where to put the state machine being
	 *                      created at its different stages. If this is null,
	 *                      nothing will be written.
	 * @param minWeigth     Each edge that has weight below this (that is it does
	 *                      not generate this number of terms in the initial setup)
	 *                      will be discarded.
	 * @param evaluate      If true, will evaluate generated machine at all its
	 *                      intermediate state.
	 * 
	 * @return best version of the state machine (kinda).
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public static StateMachine process(Counter<String> voynichTokens, String outFolder, int minWeigth, boolean evaluate)
			throws IOException, ParseException {

		Set<String> voynichTerms = voynichTokens.itemSet();
		String name = "SM_" + minWeigth;

		// Notice at each state, weights of the state machine will reflect how many
		// TERMS are generated through a given edge.

		StateMachine m = BuildSlotStateMachine.build(voynichTerms, minWeigth);
		if (outFolder != null)
			m.write(outFolder, name + "_RAW");
		if (evaluate)
			WordModelEvaluator.evaluate(name + "_RAW", voynichTokens, m.emit().itemSet());

		m.train(voynichTerms, TrainMode.F1);
		if (outFolder != null)
			m.write(outFolder, name + "_TRAINED");
		if (evaluate)
			WordModelEvaluator.evaluate(name + "_TRAIN", voynichTokens, m.emit().itemSet());

		merge(m, voynichTerms);
		m.train(voynichTerms, TrainMode.F1);
		if (outFolder != null)
			m.write(outFolder, name + "_MERGED");
		if (evaluate)
			WordModelEvaluator.evaluate(name + "_MERGE", voynichTokens, m.emit().itemSet());

		m.trim(minWeigth);
		if (outFolder != null)
			m.write(outFolder, name + "_TRIMMED");
		if (evaluate)
			WordModelEvaluator.evaluate(name + "_TRIMMED", voynichTokens, m.emit().itemSet());

		m.updateWeights(voynichTokens);
		if (outFolder != null)
			m.write(outFolder, name + "_TOKENS");

		if (outFolder != null) {
			System.out.println("\n\n");
			System.out.println(m.toJava());
			System.out.println("\n\n");
			System.out.println(m.toGrammar());
		}

		return m;
	}

	/**
	 * Builds a state machine that models TERMS in given document.
	 * 
	 * @param doc       Document which terms will be modeled.
	 * @param minWeight If the weight of an edge of the state machine is less than
	 *                  this, the edge will be removed before returning the machine.
	 * 
	 * @return The state machine being built.
	 * 
	 * @throws IOException
	 */
	public static StateMachine build(IvtffText doc, int minWeight) throws IOException {
		return build(doc.getWords(true).itemSet(), minWeight);
	}

	/**
	 * Builds a state machine from a set of TERMS that must be modeled.
	 * 
	 * The machine will have state for each slot character (accordingly to Slot
	 * model). Edges will connect characters following each other in terms; the
	 * weight of each edge will be the number of terms that have corresponding
	 * character sequence.
	 * 
	 * Because of the nature of the process, this will only consider REGULAR terms.
	 * In addition, this throws an exception if unreadable terms are provided.
	 * 
	 * @param terms     Words that will be modeled.
	 * @param minWeight If the weight of an edge of the state machine is less than
	 *                  this, the edge will be removed before returning the machine.
	 * 
	 * @return The state machine being built.
	 * 
	 * @throws IOException
	 */
	public static StateMachine build(Set<String> terms, int minWeight) throws IOException {

		// All terms, with their classification
		Map<String, TermDecomposition> classifiedTerms = SlotAlphabet.decompose(terms);

		// My language, that is the target set of strings I want to generate
		List<String> language = new ArrayList<>();
		for (TermDecomposition d : classifiedTerms.values()) {
			if (d.classification == TermClassification.REGULAR)
				language.add(d.term);
		}

		// Initialize begin and end states
		StateMachine m = new StateMachine();
		State initialState = m.addState(INITIAL_STATE);
		m.setInitialState(initialState);
		State endState = m.addState(END_STATE, new String[] { "" }, true);

		// Add state per each character
		for (int i = 0; i < SlotAlphabet.SLOTS.size(); ++i) {
			for (String c : SlotAlphabet.SLOTS.get(i)) {
				m.addState(getCharName(i, c), c);
			}
		}

		// Counts how many times the left char (state) follows the right char (state).
		// Chars/states are represented as ImmutablePair<String, String> so a pair is
		// actually an edge in the graph.
		Counter<ImmutablePair<String, String>> twoCharCount = new Counter<>();

		// Do the counting
		for (String t : language) {
			TermDecomposition d = classifiedTerms.get(t);

			boolean wordBegin = true;
			for (int i = 0; i < d.slots1.length;) {
				// Aligns to first char in the word
				if (d.slots1[i].equals("")) {
					++i;
					continue;
				}

				String first = getCharName(i, d.slots1[i]);
				if (wordBegin) {
					// First char in the word, let initial state point to it
					twoCharCount.count(new ImmutablePair<>(initialState.getName(), first));
					wordBegin = false;
				}

				int j = i + 1;
				for (; j < d.slots1.length; ++j) {
					// Get next char
					if (d.slots1[j].equals(""))
						continue;

					String next = getCharName(j, d.slots1[j]);

					// Count this link
					twoCharCount.count(new ImmutablePair<>(first, next));
//					if (first.equals("1_o") && next.equals("8_o")) {
//						System.out.println(d.term);
//					}

					break;
				}

				// skip the blank spaces
				i = j;

				if (i >= d.slots1.length) {
					// Last character in the string, let it point to the end state for the machine
					twoCharCount.count(new ImmutablePair<>(first, endState.getName()));
				}
			}
		}

		// Now, build a state machine that generates all the terms
		// "Named" chars will be our nodes, linked when one char is followed by another

		// Link states properly
		for (Entry<ImmutablePair<String, String>, Integer> e : twoCharCount.reversed()) {
			ImmutablePair<String, String> edge = e.getKey();
			int weight = e.getValue();

			if (weight >= minWeight) {
				m.addNext(edge.left, edge.right);
				m.setWeight(edge.left, edge.right, weight);
			}
		}

		// Cleanup unused states eventually, no need to update weights, this is only if
		// the machine is generated out of a small vocabulary that is not using all
		// letters.
		m.removeUnreachableStates();

		return m;
	}

	/**
	 * Gets an unique name for a char in a slot.
	 * 
	 * @param slot
	 * @param c
	 */
	private static String getCharName(int slot, String c) {
		return slot + "_" + c;
	}

	/**
	 * Merges states we know are equivalent in the SM.
	 * 
	 * Weights will be updated with the number of terms that have corresponding
	 * character sequence. In addition, this throws an exception if unreadable terms
	 * are provided.
	 * 
	 * @param terms Words that the machine should model.
	 * @param m
	 */
	public static void merge(StateMachine m, Set<String> terms) {

		State r = m.getState("3_t");
		r = merge(r, m.getState("3_p"));
		r = merge(r, m.getState("3_k"));
		r = merge(r, m.getState("3_f"));

//		r = m.getState("4_C");
//		r = merge(r, m.getState("4_S"));

		r = m.getState("5_T");
		r = merge(r, m.getState("5_P"));
		r = merge(r, m.getState("5_K"));
		r = merge(r, m.getState("5_F"));

		r = m.getState("6_e");
		r = merge(r, m.getState("6_E"));
		r = merge(r, m.getState("6_B"));

//		r = m.getState("7_t");
//		r = merge(r, m.getState("7_p"));
//		r = merge(r, m.getState("7_k"));
//		r = merge(r, m.getState("7_f"));
//
//		r = m.getState("7_T");
//		r = merge(r, m.getState("7_P"));
//		r = merge(r, m.getState("7_K"));
//		r = merge(r, m.getState("7_F"));

		r = m.getState("9_i");
		r = merge(r, m.getState("9_J"));
		r = merge(r, m.getState("9_U"));

//		r = m.getState("10_l");
//		r = merge(r, m.getState("10_r"));
//
//		r = m.getState("10_m");
//		r = merge(r, m.getState("10_n"));

		// Weights must be recomputed
		m.updateWeights(terms);
	}

	/**
	 * Merges two states guarding when they are null.
	 * 
	 * @return the merged state.
	 */
	private static State merge(State a, State b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return a.merge(b);

	}
}
