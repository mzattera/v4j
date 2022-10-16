package io.github.mzattera.v4j.applications.chars;

/**
 * 
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffText;

/**
 * Tests Experiments inner class split text as they should.
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class ExperimentsTest {

	private static IvtffText INPUT;
	static {
		try {
			INPUT = new IvtffText("#=IVTFF Slot 1.5\n"
					+ "<f1r>          <! $I=T $Q=A $P=A $L=A $H=1 $X=V>                                \n" + "#\n"
					+ "<f1r.0,=Pa;m>	dain.?s.teody\n" + "#\n"
					+ "<f1r.1,@P0;m>	faCys.ykal.ar.ataJn.Sol.Sory.Tres.y.kor.Soldy\n"
					+ "<f1r.2,+P0;m>	?ory.Kar.or.y.kair.CtaJn.Sar.are.Tar.Tar.dan\n"
					+ "<f1r.3,+P0;m>	syaJr.Seky.or.ykaJn.Sod.Toary.Tes.daraJn.sy<$>\n" + "#\n"
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
		} catch (Exception e) {
		}
	}

	@Test
	@DisplayName("Experiments.FirstLetter().splitDocument()")
	public void doTest1() throws Exception {
		Text[] splitted = new Experiments.FirstLetter().splitDocument(INPUT);
		test(splitted[0], new String[] { "f?s?yd?dSP" });
	}

	@Test
	@DisplayName("Experiments.FirstLineInPage().splitDocument()")
	public void doTest2() throws Exception {
		Text[] splitted = new Experiments.FirstLineInPage().splitDocument(INPUT);
		test(splitted[0], new String[] { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" });
	}

	@Test
	@DisplayName("Experiments.FirstParagraphLine().splitDocument()")
	public void doTest3() throws Exception {
		Text[] splitted = new Experiments.FirstParagraphLine().splitDocument(INPUT);
		assertEquals(splitted[0] instanceof IvtffText, true);
		test((IvtffText) splitted[0],
				new String[][] { { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" },
						{ "?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
						{ "?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
						{ "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" } });
	}

	@Test
	@DisplayName("Experiments.LastLetter().splitDocument()")
	public void doTest4() throws Exception {
		Text[] splitted = new Experiments.LastLetter().splitDocument(INPUT);
		test(splitted[0], new String[] { "ynyysysay?" });
	}

	@Test
	@DisplayName("Experiments.LastParagraphLine().splitDocument()")
	public void doTest5() throws Exception {
		Text[] splitted = new Experiments.LastParagraphLine().splitDocument(INPUT);
		assertEquals(splitted[0] instanceof IvtffText, true);
		test((IvtffText) splitted[0],
				new String[][] { { "syaJr", "Seky", "or", "ykaJn", "Sod", "Toary", "Tes", "daraJn", "sy" },
						{ "daJn", "Sos", "Fol", "Sody" }, { "Sok", "Cor", "Cey", "dain", "Key" },
						{ "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" } });
	}

	private void test(IvtffText text, String[][] t) {
		List<IvtffLine> lines = text.getLines();
		assertEquals(lines.size(), t.length);
		for (int i = 0; i < t.length; ++i)
			test(lines.get(i), t[i]);
	}

	private static void test(Text text, String[] t) {
		String[] s = text.splitWords();
		assertEquals(s.length, t.length);
		for (int i = 0; i < t.length; ++i)
			assertEquals(s[i], t[i]);
	}
}