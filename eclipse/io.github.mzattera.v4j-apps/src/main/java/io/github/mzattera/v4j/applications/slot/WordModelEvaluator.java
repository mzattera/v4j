package io.github.mzattera.v4j.applications.slot;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.statemachine.SlotBasedModel;
import io.github.mzattera.v4j.util.statemachine.StateMachine;

/**
 * Evaluates F1 score for models of Voynich words, considered as a classifiers
 * for Voynich terms.
 * 
 * See concept here:
 * https://developers.google.com/machine-learning/crash-course/classification/precision-and-recall
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class WordModelEvaluator {

	/** Transcription to use */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/** Transcription to use */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/**
	 * Which Alphabet type to use.
	 */
	public static final Alphabet ALPHABET = Alphabet.EVA;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	private static NumberFormat INTEGER_F = NumberFormat.getIntegerInstance();
	static {
		INTEGER_F.setGroupingUsed(true);
	}
	private static NumberFormat DECIMAL_F = new DecimalFormat("#0.000");

	public static void main(String[] args) throws Exception {

		// Prints configuration parameters
		System.out.println("Transcription     : " + TRANSCRIPTION);
		System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
		System.out.println("Filter            : " + (FILTER == null ? "None" : FILTER.toString()));
		System.out.println();

		// Get the document to process
		IvtffText voynich = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.EVA);
		if (FILTER != null)
			voynich = voynich.filterPages(FILTER);

		// All readable terms
		Counter<String> voynichTokens = voynich.getWords(true);

		// "Recipes" section
		IvtffText recipes = voynich.filterPages(new PageFilter.Builder().illustrationType("S").language("B").build());

		System.out.println("Total Voynich Terms: " + INTEGER_F.format(voynichTokens.size()));
		System.out.println();
		System.out.println(
				"| Model \t| Generated strings \t| True Positives \t| Positive Tokens \t| Precision \t| Recall \t| F-Score |");
		System.out.println("| :--- \t| ---: \t| ---: \t| ---: \t| ---: \t| ---: \t| ---: |");

		evaluateRoe(voynichTokens);
		evaluateStolfi(voynichTokens);
		evaluateNeal1a(voynichTokens);
		evaluateNeal1b(voynichTokens);
		evaluateNeal2(voynichTokens);
		evaluatePalmer(voynichTokens);
		evaluateVogt("VOGT (Recipes)", recipes.getWords(true));
		evaluateVogt("VOGT", voynichTokens);
		evaluatePelling(voynichTokens);
		evaluateSlots(voynichTokens);
		evaluateSlotMachine(voynichTokens);

		System.out.println();
		evaluateSlotMachines(voynichTokens);

		System.out.println();
	}

	/**
	 * Evaluate Row Model (http://www.voynich.nu/a3_para.html)
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateRoe(Counter<String> voynichTokens) throws ParseException {

		StateMachine m = new StateMachine();
		m.setInitialState(m.addState("Start", new String[] { "o", "qo", "cho" }));
		m.addState("t.k", new String[] { "t", "k" });
		m.addState("o.cho", new String[] { "o", "cho" });
		m.addState("e.pedestals", new String[] { "e", "ee", "che", "ch", "sh", "" });
		m.addState("aSeq", new String[] { "al", "am", "ain", "aiin" });
		m.addState("r.l", new String[] { "l", "r" });
		m.addState("y.dy", new String[] { "y", "dy" });
		m.addState("End", true);

		m.addNext("Start", new String[] { "t.k" });
		m.addNext("t.k", new String[] { "o.cho", "e.pedestals", "aSeq" });
		m.addNext("o.cho", new String[] { "r.l" });
		m.addNext("e.pedestals", new String[] { "y.dy" });
		m.addNext("aSeq", new String[] { "End" });
		m.addNext("r.l", new String[] { "End" });
		m.addNext("y.dy", new String[] { "End" });

		evaluate("ROE", voynichTokens, m.emit().itemSet());
	}

	/**
	 * Evaluate crust-mantle-core model by Stolfi.
	 * 
	 * https://www.ic.unicamp.br/~stolfi/EXPORT/projects/voynich/00-06-07-word-grammar/
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateStolfi(Counter<String> voynichTokens) throws ParseException {
		// As it is impossible to generate all terms define by Stolfi's grammar (they
		// are 1.5e20)
		// nor it is possible to parse each term as the grammar is not LL-recursive,
		// we assume each Voynich term that is not an AbnormalWord is recognized by the
		// grammar.
		int tp = 0;
		int ttp = 0;
		for (String t : voynichTokens.itemSet())
			if (!ABNORMAL_WORDS.contains(t)) {
				++tp;
				ttp += voynichTokens.getCount(t);
			}

		// 1.4312456007524008E20 is obtained with
		// io.github.mzattera.v4j.cmc.count.generator.CmcCounterGenerator
		evaluate("STOLFI", voynichTokens.size(), 1.4312456007524008E20, tp, voynichTokens.getTotalCounted(), ttp);

	}

	/**
	 * Tests Philip Neal’s own Voynichese word generator from
	 * 
	 * https://www.voynich.ninja/thread-762-post-6281.html?highlight=%5Bqd_%5D%5Baoy_%5D%5Blr_%5D%5BktpfKTPF_%5D%5BCS_%5D%5BeE_%5D%5Bd%5D%5Bao_%5D%5Blrmn_%5D%5By_%5D#pid6281
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateNeal1a(Counter<String> voynichTokens) throws ParseException {

		// TODO
		//// The below generates all possible regular terms ///////////////////////////
		SlotBasedModel m = new SlotBasedModel(new String[][] { //
				{ "q", "d", "" }, //
				{ "a", "o", "y", "" }, //
				{ "l", "r", "" }, //
				{ "k", "t", "p", "f", "ckh", "cth", "cph", "cfh", "" }, //
				{ "ch", "sh", "" }, //
				{ "e", "ee", "" }, //
				{ "d" }, //
				{ "a", "o", "" }, //
				{ "l", "r", "m", "n", "" }, //
				{ "y", "" } //
		});
		Set<String> generated = m.emit();

		evaluate("NEAL_1a", voynichTokens, generated);
	}

	/**
	 * Tests Philip Neal’s own Voynichese word generator from
	 * 
	 * https://www.voynich.ninja/thread-762-post-6281.html?highlight=%5Bqd_%5D%5Baoy_%5D%5Blr_%5D%5BktpfKTPF_%5D%5BCS_%5D%5BeE_%5D%5Bd%5D%5Bao_%5D%5Blrmn_%5D%5By_%5D#pid6281
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateNeal1b(Counter<String> voynichTokens) throws ParseException {

		// TODO
		//// The below generates all possible regular terms ///////////////////////////
		SlotBasedModel m = new SlotBasedModel(new String[][] { //
				{ "q", "d", "" }, //
				{ "a", "o", "y", "" }, //
				{ "l", "r", "" }, //
				{ "k", "t", "p", "f", "ckh", "cth", "cph", "cfh", "" }, //
				{ "ch", "sh", "" }, //
				{ "e", "ee", "" }, //
				{ "d", "" }, //
				{ "a", "o", "" }, //
				{ "l", "r", "m", "n", "" }, //
				{ "y", "" } //
		});
		Set<String> generated = m.emit();

		evaluate("NEAL_1b", voynichTokens, generated);
	}

	/**
	 * Tests Philip Neal’s own Voynichese word generator from
	 * 
	 * http://ciphermysteries.com/2010/11/22/sean-palmers-voynichese-word-generator
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateNeal2(Counter<String> voynichTokens) throws ParseException {

		//// The below generates all possible regular terms ///////////////////////////
		SlotBasedModel m = new SlotBasedModel(new String[][] { //
				{ "d", "k", "l", "p", "r", "s", "t", "" }, //
				{ "o", "a", "" }, //
				{ "l", "r", "" }, //
				{ "f", "k", "p", "t", "" }, //
				{ "sh", "ch ", "" }, //
				{ "e", "ee", "eee", "eeee", "" }, //
				{ "d", "cfh", "ckh", "cph", "cth", "" }, //
				{ "a", "o", "" }, //
				{ "m", "n", "l", "in", "iin", "iiin", "" }, //
				{ "y", "" }, //
		});
		Set<String> generated = m.emit();

		evaluate("NEAL_2", voynichTokens, generated);
	}

	/**
	 * Tests Sean Palmer word generator from
	 * 
	 * http://ciphermysteries.com/2010/11/22/sean-palmers-voynichese-word-generator
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluatePalmer(Counter<String> voynichTokens) throws ParseException {

		// The grammar can generate infinite sequences. I only check for positives

		// Original grammar
		Pattern p = Pattern.compile("[qyktfp]*(([cs][ktfp]*h*e*)|([ktfp]+e*)|[dslr]|(ai*n*)|o)*[ymg]?");

		// Original grammar; * replaced with actual occurrences
//		Pattern p = Pattern.compile("[qyktfp]{0,4}(([cs][ktfp]?h{0,2}e{0,3})|([ktfp]e{0,4})|[dslr]|(ai{0,3}n?)|o){0,10}[ymg]?");

		// Original grammar; actual occurrences replaced with strings
//		Pattern p = Pattern.compile(
//				"[qyktfp]{0,4}(([cs][ktfp]?(h|hh)?(e|ee|eee)?)|([ktfp](e|ee|eee|eeee)?)|[dslr]|(a(i|ii|iii)?n?)|o){0,10}[ymg]?");

		// Try to build a machies for the below pattern this elped determine the size of
		// genrated terms, in case we try to be smart.

		// [qyktfp]{0,4} ( m1
		//
		// ([cs][ktfp]?(h|hh)?(e|ee|eee)?) | m2
		// ([ktfp](e|ee|eee|eeee)?) | m3
		// [dslr] | m4
		// (a(i|ii|iii)?n?) | m5
		// o ){0,10} m6
		//
		// [ymg]?"); m7

//		SlotBasedModel m1 = new SlotBasedModel(new String[][] { //
//				{ "q", "y", "k", "t", "f", "p", "" }, { "q", "y", "k", "t", "f", "p", "" },
//				{ "q", "y", "k", "t", "f", "p", "" }, { "q", "y", "k", "t", "f", "p", "" } });
//		Set<String> g1 = m1.emit();
//		System.out.println("*** " + g1.size());
//
//		SlotBasedModel m2 = new SlotBasedModel(new String[][] { //
//			{ "c", "s" },
//			{ "k", "t", "f", "p", "" },
//			{ "h", "hh", "" },
//			{ "e", "ee", "eee", "" }
//		});
//		Set<String> g2 = m2.emit();
//		g2.add("");
//		System.out.println("*** " + g2.size());
//
//		SlotBasedModel m3 = new SlotBasedModel(new String[][] { //
//			{ "k", "t", "f", "p", "" },
//			{ "e", "ee", "eee", "eeee", "" }
//		});
//		Set<String> g3 = m3.emit();
//		System.out.println("*** " + g3.size());
//
//		SlotBasedModel m4 = new SlotBasedModel(new String[][] { //
//			{ "d", "s", "l", "r" }
//		});
//		Set<String> g4 = m4.emit();
//		System.out.println("*** " + g4.size());
//
//		// (a(i|ii|iii)?n?) |					m5
//		SlotBasedModel m5 = new SlotBasedModel(new String[][] { //
//			{ "a" },
//			{ "i", "ii", "iii", "" },
//			{ "n", "" }
//		});
//		Set<String> g5 = m5.emit();
//		System.out.println("*** " + g5.size());
//
//		SlotBasedModel m6 = new SlotBasedModel(new String[][] { //
//			{ "o" }
//		});
//		Set<String> g6 = m6.emit();
//		System.out.println("*** " + g6.size());
//
//		// [ymg]?
//		SlotBasedModel m7 = new SlotBasedModel(new String[][] { //
//			{ "y", "m", "g", "" }
//		});
//		Set<String> g7 = m7.emit();
//		System.out.println("*** " + g7.size());
//
//		Set<String> gMiddle = new HashSet<>();
//		gMiddle.addAll(g2);
//		gMiddle.addAll(g3);
//		gMiddle.addAll(g4);
//		gMiddle.addAll(g5);
//		gMiddle.addAll(g6);
//		System.out.println("*** " + gMiddle.size());
//		
//		List<String> middle = new ArrayList<>(gMiddle);
//		List<List<String>> middleSlots = new ArrayList<>();
//		for (int i=0;i<10;++i)
//			middleSlots.add(middle);
//		SlotBasedModel mMiddle = new SlotBasedModel(middleSlots);
//		Set<String> g8 = mMiddle.emit();
//		System.out.println("*** " + g8.size());

//		Pattern p2 = Pattern.compile("[qyktfp]{0,4}(([cs][ktfp]?h{0,2}e{0,3})|([ktfp]+e{0,4})|[dslr]|(ai{0,3}n?)|o){0,7}[ymg]?");
//		Set<String> s2 = new HashSet<>();
//		for (String t : voynichTokens.itemSet())
//			if (p2.matcher(t).matches()) s2.add(t);
//		for (String t : voynichTokens.itemSet())
//			if (p.matcher(t).matches() && !s2.contains(t))
//				System.out.println("*** " + t);

		int tp = 0;
		int ttp = 0;
		for (String t : voynichTokens.itemSet()) {

			if (p.matcher(t).matches()) {
				++tp;
				ttp += voynichTokens.getCount(t);
			}
		}

		evaluate("PALMER", voynichTokens.size(), Double.POSITIVE_INFINITY, tp, voynichTokens.getTotalCounted(), ttp);
//		evaluate("PALMER_2", voynichTokens.size(), 1555*Math.pow(157, 10)*4, tp, voynichTokens.getTotalCounted(), ttp);
	}

	/**
	 * Tests E. Vogt grammar from
	 * 
	 * https://voynichthoughts.wordpress.com/grammar/
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateVogt(String name, Counter<String> voynichTokens) throws ParseException {

		// Creates all and only chke sequences in the proposed vocabulary
//		final char[] chke = new char[] { 'c', 'h', 'k', 'e' };
//		Set<String> chkeSeq = new HashSet<>();
//		for (int i = 0; i < chke.length; ++i)
//			for (int j = 0; j < chke.length; ++j)
//				for (int k = 0; k < chke.length; ++k)
//					for (int l = 0; l < chke.length; ++l) {
//						String s = Character.toString(chke[i]) + chke[j] + chke[k] + chke[l];
//						for (String t : voynichTokens.itemSet()) {
//							if (t.indexOf(s) != -1) {
//								chkeSeq.add(s);
//								break;
//							}
//						}
//					}

		// chke sequences
		SlotBasedModel sm = new SlotBasedModel(new String[][] { //
				{ "c", "h", "k", "e", "" }, { "c", "h", "k", "e", "" }, { "c", "h", "k", "e", "" },
				{ "c", "h", "k", "e", "" } });
		Set<String> chkeSeq = sm.emit();

		StateMachine m = new StateMachine();
		m.setInitialState(m.addState("Start"));
		m.addState("0q", new String[] { "q", "" });
		m.addState("0o", new String[] { "o", "" });
		m.addState("0r", new String[] { "R" });
		m.addState("0d", new String[] { "d" });
		m.addState("0t", new String[] { "t" });
		m.addState("0l", new String[] { "l", "" });
		m.addState("0p", new String[] { "p" });
		m.addState("0k", new String[] { "k", "" });
		m.addState("0s", new String[] { "s" });
		m.addState("0h", new String[] { "h" });
		m.addState("Sep1", "/");
		m.addState("Sep2", "/");
		m.addState("1a", new String[] { "a" });
		m.addState("1i1", new String[] { "I", "" });
		m.addState("1i2", new String[] { "i", "" });
		m.addState("1i3", new String[] { "i" });
		m.addState("1l", new String[] { "l" });
		m.addState("1m", new String[] { "m" });
		m.addState("1r", new String[] { "r" });
		m.addState("1n", new String[] { "n" });
		m.addState("chke", new String[] { "C", "" });
		m.addState("2o", new String[] { "o" });
		m.addState("2d", new String[] { "d", "" });
		m.addState("2l", new String[] { "l", "" });
		m.addState("2r", new String[] { "r", "" });
		m.addState("2y", new String[] { "y" });
		m.addState("End", true);

		m.addNext("Start", new String[] { "0q", "0s" });
		m.addNext("0q", new String[] { "0o" });
		m.addNext("0o", new String[] { "0r", "0d", "0t", "0l", "0p" });
		m.addNext("0l", new String[] { "0k" });
		m.addNext(new String[] { "0r", "0d", "0t", "0k" }, "Sep1");
		m.addNext("0s", new String[] { "0h", "Sep1" });
		m.addNext(new String[] { "0k", "0p", "0h" }, "Sep2");

		m.addNext("Sep1", "1a");
		m.addNext("Sep2", "chke");

		m.addNext("1a", new String[] { "1l", "1m", "1i1" });
		m.addNext("1i1", new String[] { "1i2" });
		m.addNext("1i2", new String[] { "1i3", "1r" });
		m.addNext("1i3", new String[] { "1n" });
		m.addNext(new String[] { "1l", "1m", "1n", "1r" }, "End");

		m.addNext("chke", new String[] { "2o", "2d" });
		m.addNext("2o", new String[] { "2l", "2r" });
		m.addNext("2d", new String[] { "2y" });
		m.addNext(new String[] { "2l", "2r", "2y" }, "End");

		// Filter produced words based on rules

		Set<String> tmp = new HashSet<>();
		for (String e : m.emit().itemSet()) {
			int p = e.indexOf('/');
			String start = e.substring(0, p);
			String stop = e.substring(p + 1);
			if (start.length() > 0) {
				// Start group used
				tmp.add(start + stop.replace("I", "")); // Rule 2
			}
			tmp.add(stop.replace('I', 'i')); // Rule 2
		}

		Set<String> generated = new HashSet<>();
		for (String e : tmp) {

			// Rule (1)
			int p = e.indexOf('R');
			if ((p > 0) && (e.charAt(p - 1) == 'q'))
				continue;

			e = e.replace('R', 'r');
			for (String c : chkeSeq) {
				generated.add(e.replace("C", c)); // Rule 3
			}
		}

		evaluate(name, voynichTokens, generated);
	}

	/**
	 * Tests E. Vogt grammar from
	 * 
	 * http://ciphermysteries.com/2010/11/22/sean-palmers-voynichese-word-generator
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluatePelling(Counter<String> voynichTokens) throws ParseException {

		// Notice that the below does not emit terms, but regex, this because the
		// machine would generate infinite terms).
		StateMachine m = new StateMachine();
		m.setInitialState(m.addState("Start"));
		m.addState("1qo", new String[] { "qo", "y", "ol", "l", "o", "or" });
		m.addState("2sh", new String[] { "sh" });
		m.addState("2ch", new String[] { "ch" });
		m.addState("2k", new String[] { "k", "t" });
		m.addState("2d", new String[] { "d", "s" });
		m.addState("Box1", new String[] { "" });
		m.addState("3eo", new String[] { "eo" });
		m.addState("3e", new String[] { "((e|ee)|((e|ee)(ckh|cth))+(e|ee)?)" });
		m.addState("3ckh", new String[] { "((ckh|cth)|((ckh|cth)(e|ee))+(ckh|cth)?)" });
		m.addState("3a", new String[] { "a", "o" });
		m.addState("Box2", new String[] { "" });
		m.addState("4l", new String[] { "l", "r" });
		m.addState("4dy", new String[] { "dy", "ky", "y" });
		m.addState("4n", new String[] { "n", "in", "iin", "r", "ly", "m" });
		m.addState("End", true);

		m.addNext("Start", new String[] { "1qo", "2sh", "2ch", "2k", "2d" });
		m.addNext("1qo", new String[] { "2ch", "2k" });
		m.addNext("2sh", new String[] { "Box1" });
		m.addNext("2ch", new String[] { "Box1" });
		m.addNext("2k", new String[] { "3e", "3a" });
		m.addNext("2d", new String[] { "3a" });
		m.addNext("Box1", new String[] { "3eo", "3e", "3ckh" });
		m.addNext("3eo", new String[] { "4l", "4dy" });
		m.addNext("3e", new String[] { "Box2" });
		m.addNext("3ckh", new String[] { "Box2" });
		m.addNext("3a", new String[] { "4n" });
		m.addNext("Box2", new String[] { "4dy" });
		m.addNext(new String[] { "4l", "4dy", "4n" }, "End");

		int tp = 0;
		int ttp = 0;
		for (String w : m.emit().itemSet()) {
			Pattern p = Pattern.compile(w);
			for (String t : voynichTokens.itemSet()) {
				if (p.matcher(t).matches()) {
					++tp;
					ttp += voynichTokens.getCount(t);
				}
			}
		}

		evaluate("PELLING", voynichTokens.size(), Double.POSITIVE_INFINITY, tp, voynichTokens.getTotalCounted(), ttp);
	}

	/**
	 * Evaluate Slots model
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateSlots(Counter<String> voynichTokens) throws ParseException {

		//// The below generates all possible regular terms ///////////////////////////
		List<List<String>> s = SlotAlphabet.SLOTS;
		for (List<String> l : s)
			l.add("");
		SlotBasedModel m = new SlotBasedModel(s);
		Set<String> generatedSlot = m.emit();
		Set<String> generated = new HashSet<>(generatedSlot.size());
		for (String w : generatedSlot)
			generated.add(SlotAlphabet.toEva(w));

		evaluate("SLOT", voynichTokens, generated);
	}

	/**
	 * Evaluate Slots state machine model. This generates and evaluates several
	 * models.
	 * 
	 * @param slotTerms List of Voynich terms (EVA).
	 * @throws IOException
	 */
	private static void evaluateSlotMachines(Counter<String> voynichTokens) throws ParseException, IOException {

		Counter<String> slotTerms = SlotAlphabet.fromEva(voynichTokens);

		for (int i = 0; i <= 30; i += 5) {
			String name = "SM_" + i;
			evaluate(name, voynichTokens,
					SlotAlphabet.toEva(BuildSlotStateMachine.process(slotTerms, null, i, false).emit().itemSet()));
		}
	}

	/**
	 * Evaluate best Slots state machine model we have so far (automatically
	 * generated with BuildSlotSttteMachine and MIN_WEIGHT=10; not the best but
	 * simple to describe and good enough).
	 * 
	 * @param voynichTokens List of Voynich terms (EVA).
	 */
	private static void evaluateSlotMachine(Counter<String> voynichTokens) throws ParseException {

		StateMachine m = new StateMachine();
		m.addState("10_r", new String[] { "r" }, false);
		m.addState("4_S", new String[] { "S" }, false);
		m.addState("3_t|3_p|3_k|3_f", new String[] { "p", "t", "f", "k" }, false);
		m.addState("9_i|9_J", new String[] { "i", "J" }, false);
		m.addState("11_y", new String[] { "y" }, false);
		m.addState("<BEGIN>", new String[] { "" }, false);
		m.addState("10_d", new String[] { "d" }, false);
		m.addState("0_d", new String[] { "d" }, false);
		m.addState("10_n", new String[] { "n" }, false);
		m.addState("8_a", new String[] { "a" }, false);
		m.addState("10_m", new String[] { "m" }, false);
		m.addState("10_l", new String[] { "l" }, false);
		m.addState("6_e|6_E|6_B", new String[] { "B", "e", "E" }, false);
		m.addState("7_d", new String[] { "d" }, false);
		m.addState("5_T|5_P|5_K", new String[] { "P", "T", "K" }, false);
		m.addState("2_l", new String[] { "l" }, false);
		m.addState("1_o", new String[] { "o" }, false);
		m.addState("0_q", new String[] { "q" }, false);
		m.addState("0_s", new String[] { "s" }, false);
		m.addState("2_r", new String[] { "r" }, false);
		m.addState("8_o", new String[] { "o" }, false);
		m.addState("1_y", new String[] { "y" }, false);
		m.addState("7_s", new String[] { "s" }, false);
		m.addState("<END>", new String[] { "" }, true);
		m.addState("4_C", new String[] { "C" }, false);
		m.setInitialState("<BEGIN>");
		m.addNext("10_r", new String[] { "<END>" });
		m.addNext("4_S", new String[] { "6_e|6_E|6_B", "8_o" });
		m.addNext("3_t|3_p|3_k|3_f", new String[] { "4_C", "11_y", "8_a", "6_e|6_E|6_B", "8_o" });
		m.addNext("9_i|9_J", new String[] { "10_n", "10_r" });
		m.addNext("11_y", new String[] { "<END>" });
		m.addNext("<BEGIN>", new String[] { "4_C", "0_q", "0_s", "8_a", "3_t|3_p|3_k|3_f", "5_T|5_P|5_K", "2_l", "2_r",
				"1_o", "1_y", "0_d", "7_d", "4_S" });
		m.addNext("10_d", new String[] { "11_y", "<END>" });
		m.addNext("0_d", new String[] { "4_C", "4_S" });
		m.addNext("10_n", new String[] { "<END>" });
		m.addNext("8_a", new String[] { "10_l", "10_n", "10_r", "9_i|9_J", "10_m" });
		m.addNext("10_m", new String[] { "<END>" });
		m.addNext("10_l", new String[] { "11_y", "<END>" });
		m.addNext("6_e|6_E|6_B", new String[] { "11_y", "10_d", "<END>", "7_s", "8_o" });
		m.addNext("7_d", new String[] { "8_a", "8_o" });
		m.addNext("5_T|5_P|5_K", new String[] { "8_a", "6_e|6_E|6_B", "8_o" });
		m.addNext("2_l", new String[] { "4_C", "3_t|3_p|3_k|3_f", "4_S" });
		m.addNext("1_o", new String[] { "4_C", "2_r", "8_a", "7_d", "6_e|6_E|6_B", "3_t|3_p|3_k|3_f", "5_T|5_P|5_K" });
		m.addNext("0_q", new String[] { "1_o" });
		m.addNext("0_s", new String[] { "4_C" });
		m.addNext("2_r", new String[] { "8_a" });
		m.addNext("8_o", new String[] { "10_l", "10_d", "<END>", "10_r" });
		m.addNext("1_y", new String[] { "4_C", "3_t|3_p|3_k|3_f", "4_S" });
		m.addNext("7_s", new String[] { "<END>" });
		m.addNext("4_C", new String[] { "11_y", "8_a", "10_d", "6_e|6_E|6_B", "8_o" });

		evaluate("SM", voynichTokens, SlotAlphabet.toEva(m.emit().itemSet()));
	}

	/**
	 * Evaluates and prints stats for a word generation model.
	 * 
	 * @param voynichTokens List of Voynich terms (EVA) with the number of times
	 *                      they appear.
	 * @param generated     All terms generated by the model to evaluate (EVA).
	 */
	public static void evaluate(String modelId, Counter<String> voynichTokens, Set<String> generated) {

		// True Positives
		Set<String> terms = voynichTokens.itemSet();
		int tp = 0; // True positives (terms)
		int ttp = 0; // Number of token covered by the model
		for (String w : generated) {
			if (terms.contains(w)) {
				++tp;
				ttp += voynichTokens.getCount(w);
			}
		}

		// Ignore empty string
		int size = generated.contains("") ? generated.size() - 1 : generated.size();
		evaluate(modelId, voynichTokens.size(), size, tp, voynichTokens.getTotalCounted(), ttp);
	}

	/**
	 * Evaluates and prints stats for a word generation model.
	 * 
	 * @param voyTermsCount     Total number of Voynich terms.
	 * @param generatedCount    Total number of terms generated by the model.
	 * @param truePositiveCount True positive terms for the model.
	 * @param voyTokensCount    Total number of tokens in the Voynich.
	 * @param tokenCount        Total number of actual tokens generated by the
	 *                          model.
	 */
	public static void evaluate(String modelId, double voyTermsCount, double generatedCount, double truePositiveCount,
			double voyTokensCount, double tokenCount) {

		System.out.print("| " + modelId + " \t| " + INTEGER_F.format(generatedCount));

		System.out.print("\t| " + INTEGER_F.format(truePositiveCount));
		System.out.print("\t| " + DECIMAL_F.format(tokenCount / voyTokensCount * 100) + "%");

		// Precision: % of positives were correct = TP / (TP+FP) ->
		// (# generated terms in Voynich) / (total generated terms)
		// Precision == 1.0 means all terms generated by the model are real Voynich
		// terms
		double precision = truePositiveCount / generatedCount;

		// Recall: % of positives correctly identified = TP / (TP+FN) ->
		// (# generated terms in Voynich) / (total terms in Voynich)
		// Recall == 1.0 means all of the Voynich terms are generated by the model
		double recall = truePositiveCount / voyTermsCount;

		// F-score
		double f1 = 2 * precision * recall / (precision + recall);

		// Print results
		System.out.print("\t| " + DECIMAL_F.format(precision));
		System.out.print("\t| " + DECIMAL_F.format(recall));
		System.out.println("\t| " + DECIMAL_F.format(f1) + " |");
	}

	private final static String[] ABNORMAL_WORDS_ARRAY = { "a", "acfhy", "ackhy", "acpheey", "acphy", "acthedy",
			"acthey", "acthy", "adairchdy", "ai", "aiees", "aiichy", "aiickhedy", "aiicthy", "aiidaiim", "aiidal",
			"aiidy", "aiikam", "aiildy", "aiily", "aiinaim", "aiinal", "aiinod", "aiiny", "aiioly", "aiior", "aiip",
			"aiiral", "aiiraly", "aiirchar", "aiirody", "aiirol", "aiiry", "aiisom", "aikhckhy", "aiky", "aildy",
			"ainaly", "ainam", "ainy", "airal", "airam", "airar", "airchy", "airod", "airody", "airol", "airols",
			"airoy", "airy", "aithy", "aiy", "akaiiky", "akaiin", "akair", "akal", "akar", "akarar", "akchy", "akey",
			"aky", "alamchy", "alcheem", "allchdm", "alm", "amal", "amam", "aman", "amchd", "amchy", "amd", "amom",
			"amy", "apolairi", "aporair", "apy", "arairaly", "arakaiin", "arolser", "ashe", "ataiin", "ateey", "atolm",
			"aty", "c", "cfhekchdy", "cfhhy", "cha", "chaiind", "chairal", "chak", "chakain", "chakod", "chalkain",
			"chalkar", "chalkeedy", "chalolky", "chapchy", "chariin", "chataiin", "chcheey", "chcheky", "chchoty",
			"chckhoda", "chckshy", "chcpar", "chcs", "chdairod", "chdalchdy", "chdalkair", "chdchol", "chdchy",
			"chdedy", "chdeey", "chdykchedy", "chdypdaiin", "chdyshdy", "chea", "cheackhy", "cheamar", "cheamy",
			"chechey", "checta", "cheda", "chedacphy", "chedalkedy", "chedchey", "chedchy", "chedees", "chedeey",
			"chedey", "chedkain", "chedkel", "chedky", "chedykar", "chedyteokain", "chedytey", "chedyty", "cheeir",
			"cheeorfor", "cheesees", "cheeta", "chekeek", "chekeesshy", "cheockhedchy", "cheodchy", "cheodeeey",
			"cheodeey", "cheodkedy", "cheoekar", "cheoepy", "cheoet", "cheoiees", "cheokchet", "cheokorchey",
			"cheolchal", "cheolchdaiin", "cheolchdy", "cheolchey", "cheolkain", "cheolkeedy", "cheolkeepchy",
			"cheolkeey", "cheolky", "cheolshy", "cheoltain", "cheoltchedaiin", "cheoltey", "cheomam", "cheooky",
			"chepa", "chepchefy", "chesey", "chesokchoteody", "chetalshy", "chetses", "cheyqy", "chiin", "chka",
			"chkaid", "chkalchy", "chkchykoly", "chkorchy", "chlchd", "chlchpsheey", "chockhhy", "chocty", "chodaiindy",
			"chodchy", "chodey", "chodykchy", "choekchcey", "choikhy", "chokeeoky", "chokesey", "chokoishe", "chokokor",
			"cholairy", "cholchedy", "cholchey", "choldchy", "choldshy", "cholfchy", "cholfor", "cholfy", "cholkaiin",
			"cholkain", "cholkal", "cholkar", "cholkchy", "cholkedy", "cholkeedy", "cholkeeedy", "cholkeeey",
			"cholkeod", "cholkshedy", "cholky", "cholp", "cholpchd", "cholsho", "choltaiin", "choltaly", "choltam",
			"choltar", "choolkeey", "chopcheopchy", "chorcholsal", "chorchor", "chorchy", "chorochy", "chorolk",
			"choschy", "choteoky", "chpa", "chpiir", "chpkcheos", "chrky", "chsamoky", "chseeor", "chsey", "chshek",
			"chshoty", "chsky", "chtocthy", "chym", "chyokeor", "chyqoldy", "ckcho", "ckchol", "ckheckhy", "ckheokey",
			"ckshy", "cky", "coy", "cphdacthy", "cpheckhy", "cpheocthy", "cphocthy", "cphodaiils", "cphodales",
			"cphokchol", "cseo", "ctar", "ctchy", "ctechy", "ctha", "cthachchy", "cthaichar", "cthaildy", "ctheepchy",
			"ctheety", "ctheotol", "cthoem", "ctholcthy", "cthorchy", "cthres", "cthscthain", "cto", "ctoiin", "ctos",
			"da", "dachy", "dackhy", "daein", "daichy", "daicthy", "daid", "daidy", "daiial", "daiicthy", "daiidal",
			"daiidy", "daiiidy", "daiiine", "daiiiny", "daiiirchy", "daiild", "daiildy", "daiindy", "daiinol", "daiiny",
			"daiioam", "daiiody", "daiiol", "daiiral", "daiirol", "daiiry", "daiisaly", "daiity", "daikam", "daikeody",
			"daildain", "daimamdy", "daimd", "daimm", "daind", "dainkey", "dainl", "dainod", "dainy", "dairal",
			"dairam", "dairar", "dairchey", "dairin", "dairiy", "dairkal", "dairl", "dairo", "dairod", "dairody",
			"dairol", "dairom", "dairy", "dairydy", "daisn", "daisoldy", "dait", "dakar", "dakocth", "daky", "dala",
			"daldeam", "damamm", "damo", "daraiinm", "darala", "daramdal", "dardsh", "dariin", "darolm", "dateey",
			"dayty", "dchairam", "dcheoekal", "dchir", "dchm", "dchodees", "dchrchy", "ddsschx", "deedyoty", "deeeese",
			"desey", "diiin", "diin", "diir", "dlssho", "dm", "docfhhy", "docheesm", "docodal", "doeedey", "doeoear",
			"doithy", "doleodainn", "dorkcheky", "dyeees", "dym", "eeoseeo", "eesey", "ekokeey", "epairody", "esechor",
			"esedy", "eses", "etsees", "fachys", "faiiral", "faimy", "famam", "farsheey", "fchcfhy", "fchecfhy",
			"fchedey", "fchedypaiin", "fcheokair", "fchocthar", "fchoctheody", "fchodees", "fchodycheol", "fchokshy",
			"fchoky", "fdeechdy", "fdykain", "fochof", "folchear", "folchey", "folchol", "folshody", "fsheda",
			"fshodchy", "i", "iiin", "iir", "iirchal", "ka", "kacthy", "kadchy", "kaich", "kaiindal", "kaiishdy",
			"kaipy", "kairam", "kairy", "kaisar", "kaishd", "kalchdy", "kalchedy", "kaleearol", "kalkal", "kamdam",
			"kaolkar", "kara", "karainy", "karchy", "kchdpy", "kcheat", "kcheedchdy", "kcheeky", "kchekain", "kcheoey",
			"kchetam", "kchm", "kchokchy", "kcholchdar", "kchoty", "kdchody", "kecheokeo", "kechodshey", "kedaleey",
			"keechey", "keedeedy", "keedey", "keedym", "keeees", "keem", "keocthy", "keodky", "keoedy", "keoky",
			"keolchey", "kochky", "kockhos", "kodalchy", "kodeey", "kodshey", "kodshol", "kokaiin", "koky", "kolchdy",
			"kolchedy", "kolcheey", "kolches", "kolfchdy", "kolkar", "kolkedy", "kolky", "kolpy", "kolschees", "kolshd",
			"kolshes", "kolshey", "kolsho", "kolyky", "korchy", "koshet", "kotaly", "kotchody", "kotchy", "kschdy",
			"ksholochey", "kshotol", "kydainy", "kydeedy", "kykaiin", "kyty", "lchea", "lcheoekam", "lchm",
			"lcholkaiin", "lkamo", "lkarshar", "lkedeed", "lkedey", "lkedlkey", "lkedshedy", "lkeechey", "lkeeshedy",
			"lkeopol", "lklcheol", "lkshykchy", "llaiiry", "llainal", "llm", "lm", "lshalshy", "lshdyqo", "m", "mol",
			"oairar", "oamr", "oaorar", "ochodare", "ochoiky", "ocholshod", "ochoyk", "ockey", "ockhh", "ocsesy",
			"octhede", "octhole", "odaidy", "odaiiily", "odariin", "oecs", "oeedey", "oeeees", "oeeolchy", "oeksa",
			"oeolales", "oepchksheey", "ofacfom", "ofaiino", "ofakal", "ofalcfhy", "ofaramoty", "ofcheefar", "ofchtar",
			"ofseod", "ofyskydal", "oiiiin", "oiinar", "oiinol", "oiiny", "oishy", "oka", "okacfhy", "okachey",
			"okaeechey", "okai", "okaiifchody", "okaiiny", "okaildy", "okainy", "okairady", "okaircham", "okairo",
			"okairody", "okairy", "okalchal", "okalchdy", "okalched", "okalchedy", "okalchy", "okalda", "okaldm",
			"okalm", "okalshdy", "okalshey", "okarchy", "okchdpchy", "okcheefy", "okcheese", "okchoda", "okchodshy",
			"okchop", "okchoteees", "okearcheol", "okedes", "okeedaky", "okeedchsy", "okeedey", "okeockhey", "okeodof",
			"okeoeeey", "okeokain", "okeokear", "okeokedr", "okeoky", "okeolkey", "okeoschso", "okeoteey", "okesoe",
			"okeyteey", "okeyty", "okiin", "okil", "oklairdy", "okockhy", "okodchy", "okokam", "okokchodm", "okokeedy",
			"okoksheo", "okolchey", "okolchy", "okoleeolar", "okolshy", "okoroeey", "oksholshol", "okyeeshy", "okylky",
			"okytaiin", "olackhy", "olaiiny", "olcheesey", "oleesey", "oleoeder", "olkeeycthy", "olkiir", "oloiram",
			"olqo", "oltshsey", "oochockhy", "ooeeor", "opa", "opaichy", "opaiiino", "opaiinar", "opaiiral", "opailo",
			"opairam", "opakam", "opalchdy", "opalefam", "opalkaiin", "opalkam", "opalkar", "opaloiiry", "opalshedy",
			"oparairdly", "opashcfhedy", "opcheeky", "opchepy", "opchety", "opchytch", "opdaildo", "opdairody",
			"opocphor", "opodchdal", "opodeeol", "opokchor", "opolkeor", "opolkod", "oporchy", "opotey", "opykey",
			"oracphy", "oraiino", "oraiiny", "orairody", "oraryteop", "orchsey", "oreeem", "oriin", "orokeeeey",
			"osearees", "ota", "otacphy", "otai", "otaiikam", "otaiilody", "otaily", "otainy", "otairar", "otairin",
			"otairor", "otakaiman", "otakar", "otakeol", "otaky", "otalchal", "otalchy", "otaldiin", "otalef",
			"otaleky", "otalkain", "otalkchy", "otalky", "otalshdy", "otalshedy", "otalshy", "otamy", "otaramy",
			"otarcho", "otarchol", "otariin", "otchedey", "otchetchar", "otchm", "otchodeey", "otchoky", "otcholchy",
			"otchotar", "otchoty", "otchyky", "oteatey", "otedeey", "oteedchey", "oteeoky", "oteeolchor", "oteeolkeey",
			"oteeykeey", "oteodched", "oteodchy", "oteoefol", "oteofy", "oteokeey", "oteoteotsho", "oteotey", "oteotor",
			"otiir", "otkchedy", "otlchdain", "otleey", "otockhy", "otodchy", "otodeeodor", "otoeeseor", "otokcho",
			"otokeeey", "otokol", "otoky", "otolaiino", "otolchd", "otolchdy", "otolches", "otolchey", "otolky",
			"otolosheey", "otoralchy", "otorchety", "otorchy", "otork", "otorkeol", "otorsheey", "otorsheod", "ototar",
			"ototay", "otyda", "otydm", "otykchs", "otykeey", "otykol", "otyky", "otytam", "otytchol", "otytchy",
			"oykchor", "oykeedy", "oykeey", "oykshy", "oyshey", "oyshy", "oyteedy", "oyteey", "oytor", "oytoyd", "oyty",
			"paichy", "paiinody", "pairar", "pairody", "palchar", "palchd", "palkeedy", "palshsar", "parchdy",
			"pchafdan", "pchcfhdy", "pchedairs", "pchedchdy", "pchedeey", "pchedey", "pcheety", "pcheockhy",
			"pcheocphy", "pcheodchy", "pcheoepchy", "pcheokeey", "pchesfchy", "pchety", "pchocthy", "pchocty",
			"pchoetal", "pchof", "pchofar", "pchofychy", "pcholkal", "pcholkeedy", "pcholky", "pchotchy", "pchsed",
			"pchtchdy", "pchykar", "pdalshor", "pdrairdy", "pdsairy", "pdsheody", "pdychoiin", "podairol", "podchey",
			"podeesho", "podkor", "podshedy", "poeokeey", "pofochey", "pokain", "pokar", "pokeey", "polairy",
			"polalchdy", "polchal", "polchdy", "polchechy", "polched", "polchedy", "polcheolkain", "polchey", "polchls",
			"polchs", "polchy", "poldaky", "poldchody", "poldshedy", "poleedaran", "poleeol", "polkchy", "polkeedal",
			"polkeeo", "polkeey", "polkiin", "polky", "polsaisy", "polshdal", "polshdy", "polshey", "polshol",
			"polshor", "polshy", "polteshol", "polyshy", "porachol", "porarchy", "porchey", "porshols", "posairy",
			"posalshy", "potchokar", "potoy", "pschedal", "psheodshy", "psheoepoain", "psheoky", "psheot", "pydchdom",
			"pydeey", "pykedy", "pykeor", "pykydal", "pyoaly", "pysaiinor", "qairal", "qeedeey", "qepoepy", "qocky",
			"qocpheeckhy", "qodaikhy", "qoedeey", "qoeedeody", "qoeeeey", "qoekeeykeody", "qofcheepy", "qofockhdy",
			"qoikeey", "qoirain", "qoisal", "qokaekeeey", "qokaiii", "qokairolchdy", "qokaiy", "qokalchal", "qokalchdy",
			"qokalchey", "qokamdy", "qokaoy", "qokchocthor", "qokchyky", "qokechckhy", "qokededy", "qokeechey",
			"qokeefcy", "qokeeiin", "qokeeokain", "qokeeoky", "qokeeolchey", "qokeokedy", "qokiir", "qokokchy",
			"qokokil", "qokolchedy", "qokolkain", "qokolkchdy", "qokolky", "qokopy", "qoleechedy", "qolsa", "qoochey",
			"qooko", "qooto", "qopairam", "qopchcfhy", "qopchety", "qopchypcho", "qoqokeey", "qota", "qotaiior",
			"qotchoraiiny", "qotchotchy", "qotchytor", "qotea", "qotechoep", "qotedshedy", "qoteode", "qoteotor",
			"qotir", "qotlolkal", "qotocthey", "qotokody", "qotoky", "qotolchd", "qotolchy", "qoykeeey", "qoykeey",
			"qoyky", "qoypchol", "qyoeey", "ra", "racthty", "raii", "raikchy", "rairal", "ramshy", "rarolchl", "roiry",
			"rokaix", "sa", "sachy", "sacthey", "sacthy", "saeos", "sai", "saiichor", "saiinchy", "saiindy", "saiino",
			"saiiny", "saiirol", "saikchy", "saino", "sairal", "sairaly", "sairam", "sairody", "sairol", "sairom",
			"sairor", "sairy", "sakaiin", "saraisl", "satar", "sayfchedy", "saykeedy", "scharchy", "schodchy", "sha",
			"shackhy", "shalkaiin", "shalkl", "shalky", "shapchedy", "shckhdchy", "shckhefy", "shdalky", "shdchdy",
			"shdchy", "shdpchy", "shdykairylam", "sheainy", "shecphhedy", "shecthedchy", "shedaitain", "shedchy",
			"shedeeey", "shedey", "shedkedy", "shedshey", "sheeodees", "sheeolkain", "shekalchdy", "shekeefy", "sheoe",
			"sheoeky", "sheolkain", "sheolkchy", "sheolkedy", "sheolsho", "sheoltey", "shese", "shesed", "sheseky",
			"shetcheodchs", "shhy", "shkakeedy", "shlches", "shoda", "shodshey", "shokocfhy", "sholeey", "sholfaiin",
			"sholfchor", "sholfosdaiin", "sholkair", "sholkeechy", "sholkeedy", "sholkshy", "sholshdy", "shoqoky",
			"shorchdy", "shotchot", "shotokody", "shoykcho", "shoyty", "shseor", "skaiiodar", "sochorcfhy",
			"soefchocphy", "soeokeot", "soity", "somy", "soocth", "sqokeo", "stolpchy", "taedaiin", "taidy", "taipar",
			"tairoor", "talchos", "talkl", "talshdy", "tarairy", "tarshor", "tcfhy", "tchcthy", "tchdoltdy", "tchede",
			"tcheepchey", "tcheoky", "tcheolchy", "tcheorsho", "tchkaiin", "tchodairos", "tchoep", "tchokedy", "tchoky",
			"tchokyd", "tcholdchy", "tcholkaiin", "tchotchey", "tchotchor", "tchotshey", "tchtcho", "tchtod", "tchty",
			"tchykchy", "tdokchcfhy", "tedcheo", "teodeear", "teodeey", "teolkedain", "teolshy", "teoteey", "toairshy",
			"tockhy", "tocpheey", "tocphol", "tocthey", "tocthy", "todaraiily", "todashx", "todeeey", "todky",
			"toealchs", "toeeedchy", "toeeodchy", "toeoky", "tokar", "tokary", "tokol", "toky", "tolchd", "tolchdaiin",
			"tolchedy", "tolchor", "toldshy", "toleeshal", "tolkain", "tolkal", "tolkchdy", "tolkeeedy", "tolkeol",
			"tolkey", "tolkshey", "tolm", "tolokeedy", "tolpchy", "tolsheo", "tolsheol", "tolshey", "tolshosor",
			"tolshy", "topar", "torchey", "torchy", "torolshsdy", "torshor", "totchy", "totol", "tshdshey", "tshedky",
			"tsheokeedy", "tshodeesy", "tshodpy", "tshokeody", "tshoky", "typchey", "tyqoky", "ycheechy", "ycheolk",
			"ychoopy", "ycphko", "ydairol", "ydaraishy", "yeed", "yees", "yekees", "yekey", "yey", "yfcheky", "yhal",
			"ykaipy", "ykairaiin", "ykairolky", "ykaky", "ykalairal", "ykarshy", "ykceol", "ykchdm", "ykcheolchcthy",
			"ykchokeo", "ykcholqod", "ykchotchy", "ykecthey", "ykeda", "ykedckhy", "ykeealkey", "ykeedi", "ykeeeedaiir",
			"ykeeepol", "ykeeshedy", "ykeeykeey", "ykeeyky", "ykeockhey", "ykeoeshy", "ykocfhy", "ykofar", "ykyka",
			"ykykaiin", "yochedy", "yochor", "yoees", "yokaiin", "yokalod", "yokeeol", "yokeey", "yokeody", "yokor",
			"yoky", "yotaiin", "yotedy", "ypchedpy", "ypchseds", "ypolcheey", "ysholshy", "yskhy", "ytairal",
			"ytalchos", "ytalshdy", "ytapy", "ytarem", "ytashy", "ytcheeky", "ytchoky", "ytdm", "yteeoey", "yteokar",
			"ytoda", "ytoeopchey", "ytokar", "ytyda", "yyfchy" };

	private final static Set<String> ABNORMAL_WORDS = new HashSet<String>();
	static {
		for (String w : ABNORMAL_WORDS_ARRAY)
			ABNORMAL_WORDS.add(w);
	}
}
