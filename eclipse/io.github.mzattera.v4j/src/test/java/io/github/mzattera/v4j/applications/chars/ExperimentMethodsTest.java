package io.github.mzattera.v4j.applications.chars;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
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
					+ "<f1r>          <! $I=T $Q=A $P=A $L=A $H=1 $X=V>                                \n" + "#\n"
					+ "<f1r.0,=Pa;m>	dain.?s.teody\n" + "#\n"
					+ "<f1r.1,@P0;m>	faCys.ykal.ar.ataJn.Sol.Sory.Tres.y.kor.Soldy\n"
					+ "<f1r.2,+P0;m>	?ory.Kar.or.y.kair.CtaJn.Sar.are.Tar.Tar.dan\n"
					+ "<f1r.3,+P0;m>	syaJr.Seky.or.yka??Jn.Sod.Toary.Tes.daraJn.sy<$>\n" + "#\n"
					+ "<f1r.4,@L0;m>	dain.?s.teody<$>\n" + "#\n"
					+ "<f1r.5,*P0;m>	?.odar.sy.Sol.Poy.oydar.S.s.FoaJn.Sodary\n"
					+ "<f1r.6,+P0;m>	ySey.Sody.okCoy.otCol.CoTy.osCy.dain.Cor.kos\n"
					+ "<f1r.7,+P0;m>	daJn.Sos.Fol.Sody<$>\n" + "#\n" //
					+ "<f1r.8,=Pt;m>	dain.?s.teody\n" + "#\n"
					+ "<f1r.9,*P0;m>	?.ydain.PesaJn.ol.s.Pey.ytain.SoSy.Podales\n"
					+ "<f1r.10,+P0;m>	dCar.STaJn.okaJr.Cey.?Cy.potol.Tols.d?o?ta\n"
					+ "<f1r.11,+P0;m>	Sok.Cor.Cey.dain.Key<$>\n" + "#\n" //
					+ "<f1r.12,=Ca;m>	otol.daUn<$>\n" + "#  \n"
					+ "<f1r.13,*P0;m>	Po.SaJn.SokCEy.Col.tSodEsy.Sey.pydEy.Cy.ro.d?<$>\n" + "#  \n" + "");

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
		List<Counter<String>> splitted = Experiment.getWordsByPosition(
				INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 0, Integer.MAX_VALUE, false, false);

		String[] test = new String[] { "faCys", "?ory", "syaJr", "?", "ySey", "daJn", "?", "dCar", "Sok", "Po", };
		TestUtil.testWordCount(splitted.get(0), test);

		test = new String[] { "ykal", "Kar", "Seky", "odar", "Sody", "Sos", "ydain", "STaJn", "Cor", "SaJn" };
		TestUtil.testWordCount(splitted.get(1), test);

		test = new String[] { "ataJn", "y", "yka??Jn", "Sol", "otCol", "Sody", "ol", "Cey", "dain", "Col" };
		TestUtil.testWordCount(splitted.get(3), test);

	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=false, minLineLen=5, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionF_5__FF() throws Exception {
		List<Counter<String>> splitted = Experiment.getWordsByPosition(
				INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 5, Integer.MAX_VALUE, false, false);

		String[] test = new String[] { "faCys", "?ory", "syaJr", "?", "ySey", "?", "dCar", "Sok", "Po", };
		TestUtil.testWordCount(splitted.get(0), test);

	}

	@Test
	@DisplayName("getWordsByPosition(readableOnly=false, minLineLen=5, maxLineLen NA, skipFirst=false, skipLast=false)")
	public void getWordsByPositionF__4_FF() throws Exception {
		List<Counter<String>> splitted = Experiment
				.getWordsByPosition(INPUT.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER), false, 0, 4, false, false);

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
}