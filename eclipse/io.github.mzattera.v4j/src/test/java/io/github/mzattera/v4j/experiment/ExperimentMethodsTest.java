/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.experiment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.TestUtil;

/**
 * Tests Experiments inner class split text as they should.
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class ExperimentMethodsTest {

	private static IvtffText INPUT;
	static {
		try {
			INPUT = new IvtffText("#=IVTFF Slot 1.5\n"
					+ "<f1r>          <! $I=T $Q=A $P=A $L=A $H=1 $X=V>                                \n" + "#\n" //
					+ "<f1r.0,=Pa;m>	dain.?s.teody\n" + "#\n" //
					+ "<f1r.1,@P0;m>	faCys.ykal.ar.ataJn.Sol.Sory.Tres.y.kor.Soldy\n" //
					+ "<f1r.2,+P0;m>	?ory.Kar.or.y.kair.CtaJn.Sar.are.Tar.Tar.dan\n" //
					+ "<f1r.3,+P0;m>	syaJr.Seky.or.yka??Jn.Sod.Toary.Tes.daraJn.sy<$>\n" + "#\n" //
					+ "<f1r.4,@L0;m>	dain.?s.teody<$>\n" + "#\n" //
					+ "<f1r.5,*P0;m>	?.odar.sy.Sol.Poy.oydar.S.s.FoaJn.Sodary\n" //
					+ "<f1r.6,+P0;m>	ySey.Sody.okCoy.otCol.CoTy.osCy.dain.Cor.kos\n" //
					+ "<f1r.7,+P0;m>	daJn.Sos.Fol.Sody<$>\n" + "#\n" //
					+ "<f1r.8,=Pt;m>	dain.?s.teody\n" + "#\n" //
					+ "<f1r.9,*P0;m>	?.ydain.PesaJn.ol.s.Pey.ytain.SoSy.Podales\n" //
					+ "<f1r.10,+P0;m>	dCar.STaJn.okaJr.Cey.?Cy.potol.Tols.d?o?ta\n" //
					+ "<f1r.11,+P0;m>	Sok.Cor.Cey.dain.Key<$>\n" + "#\n" //
					+ "<f1r.12,=Ca;m>	otol.daUn<$>\n" + "#  \n" //
					+ "<f1r.13,*P0;m>	Po.SaJn.SokCEy.Col.tSodEsy.Sey.pydEy.Cy.ro.d?<$>\n" + "#  \n" //
					+ "<f1r.14,*P0;m>	\n" + "#  \n" //
					+ "<f1r.15,*P0;m>	<$>\n" + "#  \n" //
					+ "<f1r.16,*P0;m>	..\n" + "#  \n" //
					+ "<f1r.17,*P0;m>	<$>\n" + "#  \n");

//			"faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy",
//			"?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan",
//			"syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy",
//			
//			"?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary",
//			"ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos",
//			"daJn", "Sos", "Fol", "Sody",
//			
//			"?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales",
//			"dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta",
//			"Sok", "Cor", "Cey", "dain", "Key",
//			
//			"Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?"

//			{"faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy"},
//			{"?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan"},
//			{"syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy"},
//			
//			{"?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary"},
//			{"ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos"},
//			{"daJn", "Sos", "Fol", "Sody"},
//			
//			{"?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales"},
//			{"dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta"},
//			{"Sok", "Cor", "Cey", "dain", "Key"},
//			
//			{"Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?"}

		} catch (Exception e) {
		}
	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=false, minLineLen NA, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionF__FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPosition(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false);

		String[] test = new String[] { "faCys", "?ory", "syaJr", "?", "ySey", "daJn", "?", "dCar", "Sok", "Po", };
		TestUtil.testWordCount(splitted.get(0), test);

		test = new String[] { "ykal", "Kar", "Seky", "odar", "Sody", "Sos", "ydain", "STaJn", "Cor", "SaJn" };
		TestUtil.testWordCount(splitted.get(1), test);

		test = new String[] { "ataJn", "y", "yka??Jn", "Sol", "otCol", "Sody", "ol", "Cey", "dain", "Col" };
		TestUtil.testWordCount(splitted.get(3), test);

		test = new String[] { "Sory", "CtaJn", "Toary",

				"oydar", "osCy",

				"Pey", "potol",

				"Sey" };
		TestUtil.testWordCount(splitted.get(5), test);
	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=false, minLineLen=5, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionF_5__FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPosition(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 5);

		String[] test = new String[] { "faCys", "?ory", "syaJr", "?", "ySey", "?", "dCar", "Sok", "Po", };
		TestUtil.testWordCount(splitted.get(0), test);

	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=false, minLineLen=0, maxLineLen=4, skipFirst=false, skipLast=false)")
	public void getWordsByPositionF__4_FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPosition(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 0, 4);

		String[] test = new String[] { "daJn", };
		TestUtil.testWordCount(splitted.get(0), test);
	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=true, minLineLen NA, maxLineLen NA, skipFirst=true, skipLast=true)")
	public void getWordsByPositionT__TT() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByPosition(
				INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), true, 0, Integer.MAX_VALUE, true, true);

		String[] test = new String[] {};
		TestUtil.testWordCount(splitted.get(0), test);

		test = new String[] { "ykal", "Kar", "Seky", "odar", "Sody", "Sos", "ydain", "STaJn", "Cor", "SaJn" };
		TestUtil.testWordCount(splitted.get(1), test);

		test = new String[] { "ataJn", "y", "Sol", "otCol", "ol", "Cey", "dain", "Col" };
		TestUtil.testWordCount(splitted.get(3), test);
	}

	@Test
	@DisplayName("getWordsByPositionReversed(readableOnly=false, minLineLen NA, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionReversedF__FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPositionReversed(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false);

		String[] test = new String[] { "Soldy", "dan", "sy", "Sodary", "kos", "Sody", "Podales", "d?o?ta", "Key",
				"d?" };
		TestUtil.testWordCount(splitted.get(0), test);

		test = new String[] { "kor", "Tar", "daraJn", "FoaJn", "Cor", "Fol", "SoSy", "Tols", "dain", "ro" };
		TestUtil.testWordCount(splitted.get(1), test);

		test = new String[] { "Tres", "are", "Toary", "S", "osCy", "daJn", "Pey", "?Cy", "Cor", "pydEy" };
		TestUtil.testWordCount(splitted.get(3), test);

	}

	@Test
	@DisplayName("getWordsByPositionReversed(readableOnly=false, minLineLen=5, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionReversedF_5__FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPositionReversed(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 5);

		String[] test = new String[] { "Soldy", "dan", "sy", "Sodary", "kos", "Podales", "d?o?ta", "Key", "d?" };
		TestUtil.testWordCount(splitted.get(0), test);

	}

	@Test
	@DisplayName("getWordsByPositionReversed(readableOnly=false, minLineLen=0, maxLineLen=4, skipFirst=false, skipLast=false)")
	public void getWordsByPositionReversedF__4_FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPositionReversed(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 0, 4);

		String[] test = new String[] { "Sody" };
		TestUtil.testWordCount(splitted.get(0), test);
	}

	@Test
	@DisplayName("getWordsByPositionReversed(readableOnly=true, minLineLen NA, maxLineLen NA, skipFirst=true, skipLast=true)")
	public void getWordsByPositionReversedT__TT() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByPositionReversed(
				INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), true, 0, Integer.MAX_VALUE, true, true);

		String[] test = new String[] {};
		TestUtil.testWordCount(splitted.get(0), test);
		test = new String[] { "kor", "Tar", "daraJn", "FoaJn", "Cor", "Fol", "SoSy", "Tols", "dain", "ro" };
		TestUtil.testWordCount(splitted.get(1), test);
		test = new String[] { "Tres", "are", "Toary", "S", "osCy", "Pey", "Cor", "pydEy" };
		TestUtil.testWordCount(splitted.get(3), test);
	}

	@Test
	@DisplayName("getWordsByLine(readableOnly=FALSE, minLineLen, maxLineLen, skipFirst=FALSE, skipLast=FALSE)")
	public void getWordsByLineF__FF() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByLine(INPUT, false);

		String[] test = new String[] { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "?",
				"odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary", "?", "ydain", "PesaJn", "ol", "s",
				"Pey", "ytain", "SoSy", "Podales", "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro",
				"d?" };
		TestUtil.testWordCount(splitted.get(0), test);

		test = new String[] { "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy", "daJn", "Sos",
				"Fol", "Sody", "Sok", "Cor", "Cey", "dain", "Key" };
		TestUtil.testWordCount(splitted.get(2), test);

		assertEquals(splitted.size(), 3);
	}

	@Test
	@DisplayName("getWordsByLine(readableOnly=TRUE, minLineLen=5, maxLineLen, skipFirst=TRUE, skipLast=TRUE)")
	public void getWordsByLineT_5__TT() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByLine(INPUT, true, 5, Integer.MAX_VALUE, true, true);

		String[] test = new String[] {};
		TestUtil.testWordCount(splitted.get(0), test);
		TestUtil.testWordCount(splitted.get(2), test);

		test = new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "ySey", "Sody",
				"okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos", "dCar", "STaJn", "okaJr", "Cey", "potol",
				"Tols" };
		TestUtil.testWordCount(splitted.get(1), test);
	}

	@Test
	@DisplayName("getWordsByLine(readableOnly=TRUE, minLineLen=4, maxLineLen=5, skipFirst=FALSE, skipLast=FALSE)")
	public void getWordsByLineT_4_5_FF() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByLine(INPUT, true, 4, 5);

		String[] test = new String[] { "daJn", "Sos", "Fol", "Sody", "Sok", "Cor", "Cey", "dain", "Key", };
		TestUtil.testWordCount(splitted.get(2), test);
	}

	@Test
	@DisplayName("filterLines(discardFirstLine=TRUE, discardLastLine=FALSE)")
	public void filterLines_TF() throws Exception {
		IvtffText splitted = Experiment.filterLines(INPUT, true, false);

		String[][] test = new String[][] {
				{ "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
				{ "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy" },
				{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
				{ "daJn", "Sos", "Fol", "Sody" }, { "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" },
				{ "Sok", "Cor", "Cey", "dain", "Key" }, {}, {} };
		TestUtil.testMatch(splitted, test);
	}

	@Test
	@DisplayName("filterLines(discardFirstLine=FALSE, discardLastLine=TRUE)")
	public void filterLines_FT() throws Exception {
		IvtffText splitted = Experiment.filterLines(INPUT, false, true);

		String[][] test = new String[][] {
				{ "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" },
				{ "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
				{ "?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
				{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
				{ "?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
				{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" }, {}, {} };
		TestUtil.testMatch(splitted, test);
	}

	@Test
	@DisplayName("filterLines(discardFirstLine=TRUE, discardLastLine=TRUE)")
	public void filterLines_TT() throws Exception {
		IvtffText splitted = Experiment.filterLines(INPUT, true, true);

		String[][] test = new String[][] {
				{ "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
				{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
				{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" } };
		TestUtil.testMatch(splitted, test);
	}

	@Test
	@DisplayName("getStandardWordsPopulation(readableOnly=FALSE)")
	public void getStandardWordsPopulation_F() throws Exception {
		Counter<String> splitted = Experiment.getStandardWordsPopulation(INPUT, false);

		String[] test = new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "Sody", "okCoy",
				"otCol", "CoTy", "osCy", "dain", "Cor", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols" };

		TestUtil.testWordCount(splitted, test);
	}

	@Test
	@DisplayName("getStandardWordsPopulation(readableOnly=TRUE)")
	public void getStandardWordsPopulation_T() throws Exception {
		Counter<String> splitted = Experiment.getStandardWordsPopulation(INPUT, true);

		String[] test = new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "Sody", "okCoy",
				"otCol", "CoTy", "osCy", "dain", "Cor", "STaJn", "okaJr", "Cey", "potol", "Tols" };
		TestUtil.testWordCount(splitted, test);
	}

	@Test
	@DisplayName("getInterestingWords(Text,Text)")
	public void getInterestingWords_Text() throws Exception {
		// 'B' has 10% probability to appear in population
		TextString population = new TextString("A A A A A A A A A B");

		// 0.1% chances of this happening
		TextString sample = new TextString("B B B");

		// 'B' is interesting
		String[] test = new String[] { "B", "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population), test);

		// but not SO interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population, 0.0005), test);

		// 0.4% chances of this happening
		sample = new TextString("B B B A");

		// 'B' is interesting, 'A' is not
		test = new String[] { "B", "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population), test);

		// 2.7% chances of this happening
		sample = new TextString("B A B");

		// none is interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population), test);

		// ...unless we lower alpha
		test = new String[] { "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population, 0.03), test);

		// 2.7% chances of this happening, if ? is ignored
		sample = new TextString("B A B ? ? ?");

		// none is interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample, population), test);
	}

	@Test
	@DisplayName("getInterestingWords(Counter,Counter)")
	public void getInterestingWords_Counter() throws Exception {
		// 'B' has 10% probability to appear in population
		TextString population = new TextString("A A A A A A A A A B");

		// 0.1% chances of this happening
		TextString sample = new TextString("B B B");

		// 'B' is interesting
		String[] test = new String[] { "B", "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true)), test);

		// but not SO interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true), 0.0005),
				test);

		// 0.4% chances of this happening
		sample = new TextString("B B B A");

		// 'B' is interesting, 'A' is not
		test = new String[] { "B", "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true)), test);

		// 2.7% chances of this happening
		sample = new TextString("B A B");

		// none is interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true)), test);

		// ...unless we lower alpha
		test = new String[] { "B", "B" };
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true), 0.03),
				test);

		// 2.7% chances of this happening, if ? is ignored
		sample = new TextString("B A B ? ? ?");

		// none is interesting
		test = new String[] {};
		TestUtil.testWordCount(Experiment.getInterestingWords(sample.getWords(true), population.getWords(true)), test);
	}

	@Test
	@DisplayName("getInterestingWordsPercent()")
	public void getInterestingWordsPercent() throws Exception {

		IvtffText population = new IvtffText("#=IVTFF Slot 1.5\n" //
				+ "<f1r>          <! $I=T $Q=A $P=A $L=A $H=1 $X=V>\n" //
				+ "<f1r.1,@P0;m>	B.a.a.a.a.?i\n" //
				+ "<f1r.2,@P0;m>	B.a.a.?a\n" //
				+ "<f1r.3,@P0;m>	B.?e.a.B.a.a\n" //
		);
		List<Counter<String>> cnt = Experiment.getWordsByPosition(population, true);
		assertEquals(cnt.size(), 6);
		double[] p = Experiment.getInterestingWordsPercent(cnt);
		assertEquals(p[0], 1.0);
		for (int i = 1; i < p.length; ++i)
			assertEquals(p[i], 0.0);
	}
}