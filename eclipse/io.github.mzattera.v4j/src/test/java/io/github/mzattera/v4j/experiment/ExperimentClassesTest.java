package io.github.mzattera.v4j.experiment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.util.TestUtil;

/**
 * Tests Experiments inner class split text as they should.
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class ExperimentClassesTest {

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
	@DisplayName("Experiments.FirstLineInPage().splitDocument()")
	public void FirstLineInPage() throws Exception {
		Text[] splitted = new Experiment.FirstLineInPage().splitDocument(INPUT);
		TestUtil.testMatch((IvtffText) splitted[0],
				new String[][] { { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" } });
		TestUtil.testMatch((IvtffText) splitted[1],
				new String[][] { { "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
						{ "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy" },
						{ "?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
						{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
						{ "daJn", "Sos", "Fol", "Sody" },
						{ "?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
						{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" },
						{ "Sok", "Cor", "Cey", "dain", "Key" },
						{ "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" }, {}, {}, {}, {} });		
	}

	@Test
	@DisplayName("Experiments.FirstLineInParagraph(TRUE).splitDocument()")
	public void FirstLineInParagraphT() throws Exception {
		Text[] splitted = new Experiment.FirstLineInParagraph(true).splitDocument(INPUT);
		TestUtil.testMatch((IvtffText) splitted[0],
				new String[][] { { "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" },
						{ "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
						{ "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
						{ "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" }, {}, {} });
		TestUtil.testMatch((IvtffText) splitted[1],
				new String[][] { { "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
						{ "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy" },
						{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
						{ "daJn", "Sos", "Fol", "Sody" },
						{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" },
						{ "Sok", "Cor", "Cey", "dain", "Key" }, {}, {} });
	}

	@Test
	@DisplayName("Experiments.FirstLineInParagraph(FALSE).splitDocument()")
	public void FirstLineInParagraphF() throws Exception {
		Text[] splitted = new Experiment.FirstLineInParagraph(false).splitDocument(INPUT);
		TestUtil.testMatch((IvtffText) splitted[0],
				new String[][] { { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" },
						{ "?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
						{ "?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
						{ "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" }, {}, {} });
		TestUtil.testMatch((IvtffText) splitted[1],
				new String[][] { { "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
						{ "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy" },
						{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
						{ "daJn", "Sos", "Fol", "Sody" },
						{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" },
						{ "Sok", "Cor", "Cey", "dain", "Key" }, {}, {} });
	}

	@Test
	@DisplayName("Experiments.FirstWordInParagraph().splitDocument() TRUE")
	public void FirstWordInParagraphT() throws Exception {
		Text[] splitted = new Experiment.FirstWordInParagraph(true).splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "faCys", "Po" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "Kar", "or", "y",
						"kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "syaJr", "Seky", "or", "Sod", "Toary",
						"Tes", "daraJn", "sy", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary", "ySey",
						"Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos", "daJn", "Sos", "Fol", "Sody",
						"ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales", "dCar", "STaJn", "okaJr",
						"Cey", "potol", "Tols", "Sok", "Cor", "Cey", "dain", "Key", "SaJn", "SokCEy", "Col", "tSodEsy",
						"Sey", "pydEy", "Cy", "ro" });
	}

	@Test
	@DisplayName("Experiments.FirstWordInParagraph().splitDocument( FALSE)")
	public void FirstWordInParagraphF() throws Exception {
		Text[] splitted = new Experiment.FirstWordInParagraph(false).splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "faCys", "?", "?", "Po" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "?ory", "Kar", "or",
						"y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "syaJr", "Seky", "or", "yka??Jn",
						"Sod", "Toary", "Tes", "daraJn", "sy", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn",
						"Sodary", "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos", "daJn", "Sos",
						"Fol", "Sody", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales", "dCar", "STaJn",
						"okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta", "Sok", "Cor", "Cey", "dain", "Key", "SaJn",
						"SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" });
	}

	@Test
	@DisplayName("Experiments.LastLineInParagraph().splitDocument()")
	public void LastLineInParagraph() throws Exception {
		Text[] splitted = new Experiment.LastLineInParagraph().splitDocument(INPUT);
		TestUtil.testMatch((IvtffText) splitted[0],
				new String[][] { { "syaJr", "Seky", "or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy" },
						{ "daJn", "Sos", "Fol", "Sody" }, { "Sok", "Cor", "Cey", "dain", "Key" },
						{ "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro", "d?" }, {}, {} });
		TestUtil.testMatch((IvtffText) splitted[1],
				new String[][] { { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy" },
						{ "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan" },
						{ "?", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary" },
						{ "ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos" },
						{ "?", "ydain", "PesaJn", "ol", "s", "Pey", "ytain", "SoSy", "Podales" },
						{ "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "d?o?ta" }, {}, {} });
	}

	@Test
	@DisplayName("Experiments.FirstWordInLine(FALSE, TRUE).splitDocument()")
	public void FirstWordInLineFT() throws Exception {
		Text[] splitted = new Experiment.FirstWordInLine(false, true).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0], new String[] { "faCys", "syaJr", "ySey", "daJn", "dCar", "Sok", "Po" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "Kar", "or", "y",
						"kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "Seky", "or", "Sod", "Toary", "Tes",
						"daraJn", "sy", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "Sodary", "Sody",
						"okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "kos", "Sos", "Fol", "Sody", "ydain", "PesaJn",
						"ol", "s", "Pey", "ytain", "SoSy", "Podales", "STaJn", "okaJr", "Cey", "potol", "Tols", "Cor",
						"Cey", "dain", "Key", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro" });
	}

	@Test
	@DisplayName("Experiments.FirstWordInLine(TRUE, FALSE).splitDocument() FALSE")
	public void FirstWordInLineTF() throws Exception {
		Text[] splitted = new Experiment.FirstWordInLine(true, false).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0], new String[] { "?ory", "syaJr", "ySey", "daJn", "dCar", "Sok" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "Seky", "or",
						"yka??Jn", "Sod", "Toary", "Tes", "daraJn", "sy", "Sody", "okCoy", "otCol", "CoTy", "osCy",
						"dain", "Cor", "kos", "Sos", "Fol", "Sody", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols",
						"d?o?ta", "Cor", "Cey", "dain", "Key", });
	}

	@Test
	@DisplayName("Experiments.LastWordInLine(FALSE, TRUE).splitDocument()")
	public void LastWordInLineFT() throws Exception {
		Text[] splitted = new Experiment.LastWordInLine(false, true).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0],
				new String[] { "Soldy", "dan", "sy", "Sodary", "kos", "Sody", "Podales", "Key", });
		TestUtil.testWordCount(splitted[1],
				new String[] { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "Tres", "y", "kor", "Kar", "or", "y",
						"kair", "CtaJn", "Sar", "are", "Tar", "Tar", "syaJr", "Seky", "or", "Sod", "Toary", "Tes",
						"daraJn", "odar", "sy", "Sol", "Poy", "oydar", "S", "s", "FoaJn", "ySey", "Sody", "okCoy",
						"otCol", "CoTy", "osCy", "dain", "Cor", "daJn", "Sos", "Fol", "ydain", "PesaJn", "ol", "s",
						"Pey", "ytain", "SoSy", "dCar", "STaJn", "okaJr", "Cey", "potol", "Tols", "Sok", "Cor", "Cey",
						"dain", "Po", "SaJn", "SokCEy", "Col", "tSodEsy", "Sey", "pydEy", "Cy", "ro" });
	}

	@Test
	@DisplayName("Experiments.LastWordInLine(TRUE, FALSE).splitDocument()")
	public void LastWordInLineTF() throws Exception {
		Text[] splitted = new Experiment.LastWordInLine(true, false).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0], new String[] { "dan", "sy", "kos", "Sody", "d?o?ta", "Key" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "?ory", "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "syaJr", "Seky",
						"or", "yka??Jn", "Sod", "Toary", "Tes", "daraJn", "ySey", "Sody", "okCoy", "otCol", "CoTy",
						"osCy", "dain", "Cor", "daJn", "Sos", "Fol", "dCar", "STaJn", "okaJr", "Cey", "?Cy", "potol",
						"Tols", "Sok", "Cor", "Cey", "dain", });
	}

	@Test
	@DisplayName("Experiments.WordInPositionInLine(3, FALSE, FALSE, FALSE)")
	public void WordInPositionInLine3FFF() throws Exception {
		Text[] splitted = new Experiment.WordInPositionInLine(3, false, false, false).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0],
				new String[] { "ataJn", "y", "yka??Jn", "Sol", "otCol", "Sody", "ol", "Cey", "dain", "Col" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "faCys", "ykal", "ar", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "?ory", "Kar", "or",
						"kair", "CtaJn", "Sar", "are", "Tar", "Tar", "dan", "syaJr", "Seky", "or", "Sod", "Toary",
						"Tes", "daraJn", "sy", "?", "odar", "sy", "Poy", "oydar", "S", "s", "FoaJn", "Sodary", "ySey",
						"Sody", "okCoy", "CoTy", "osCy", "dain", "Cor", "kos", "daJn", "Sos", "Fol", "?", "ydain",
						"PesaJn", "s", "Pey", "ytain", "SoSy", "Podales", "dCar", "STaJn", "okaJr", "?Cy", "potol",
						"Tols", "d?o?ta", "Sok", "Cor", "Cey", "Key", "Po", "SaJn", "SokCEy", "tSodEsy", "Sey", "pydEy",
						"Cy", "ro", "d?" });
	}

	@Test
	@DisplayName("Experiments.WordInPositionInLine(3, FALSE, FALSE, TRUE)")
	public void WordInPositionInLine3FFT() throws Exception {
		Text[] splitted = new Experiment.WordInPositionInLine(3, false, false, true).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0],
				new String[] { "ataJn", "y", "Sol", "otCol", "Sody", "ol", "Cey", "dain", "Col" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "faCys", "ykal", "ar", "Sol", "Sory", "Tres", "y", "kor", "Soldy", "Kar", "or", "kair",
						"CtaJn", "Sar", "are", "Tar", "Tar", "dan", "syaJr", "Seky", "or", "Sod", "Toary", "Tes",
						"daraJn", "sy", "odar", "sy", "Poy", "oydar", "S", "s", "FoaJn", "Sodary", "ySey", "Sody",
						"okCoy", "CoTy", "osCy", "dain", "Cor", "kos", "daJn", "Sos", "Fol", "ydain", "PesaJn", "s",
						"Pey", "ytain", "SoSy", "Podales", "dCar", "STaJn", "okaJr", "potol", "Tols", "Sok", "Cor",
						"Cey", "Key", "Po", "SaJn", "SokCEy", "tSodEsy", "Sey", "pydEy", "Cy", "ro" });
	}

	@Test
	@DisplayName("Experiments.WordInPositionInLine(6, FALSE, FALSE, FALSE)")
	public void WordInPositionInLine6FFF() throws Exception {
		Text[] splitted = new Experiment.WordInPositionInLine(6, false, false, false).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0],
				new String[] { "Tres", "Sar", "Tes", "S", "dain", "ytain", "Tols", "pydEy" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "faCys", "ykal", "ar", "ataJn", "Sol", "Sory", "y", "kor", "Soldy", "?ory", "Kar", "or",
						"y", "kair", "CtaJn", "are", "Tar", "Tar", "dan", "syaJr", "Seky", "or", "yka??Jn", "Sod",
						"Toary", "daraJn", "sy", "?", "odar", "sy", "Sol", "Poy", "oydar", "s", "FoaJn", "Sodary",
						"ySey", "Sody", "okCoy", "otCol", "CoTy", "osCy", "Cor", "kos", "daJn", "Sos", "Fol", "Sody",
						"?", "ydain", "PesaJn", "ol", "s", "Pey", "SoSy", "Podales", "dCar", "STaJn", "okaJr", "Cey",
						"?Cy", "potol", "d?o?ta", "Sok", "Cor", "Cey", "dain", "Key", "Po", "SaJn", "SokCEy", "Col",
						"tSodEsy", "Sey", "Cy", "ro", "d?" });
	}

	@Test
	@DisplayName("Experiments.WordInPositionInLine(6, FALSE, FALSE, FALSE)")
	public void WordInPositionInLine6TTF() throws Exception {
		Text[] splitted = new Experiment.WordInPositionInLine(6, true, true, false).splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0], new String[] { "Sar", "Tes", "dain", "Tols", });
		TestUtil.testWordCount(splitted[1],
				new String[] { "Kar", "or", "y", "kair", "CtaJn", "are", "Tar", "Tar", "dan", "Seky", "or", "yka??Jn",
						"Sod", "Toary", "daraJn", "sy", "Sody", "okCoy", "otCol", "CoTy", "osCy", "Cor", "kos", "Sos",
						"Fol", "Sody", "STaJn", "okaJr", "Cey", "?Cy", "potol", "d?o?ta", "Cor", "Cey", "dain",
						"Key" });
	}

	@Test
	@DisplayName("Experiments.WithStandardPopulation(TRUE): First Word in line ")
	public void WithStandardPopulationT() throws Exception {
		Text[] splitted = new Experiment.WithStandardPopulation(new Experiment.FirstWordInLine(false, true), true)
				.splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0], new String[] { "faCys", "syaJr", "ySey", "daJn", "dCar", "Sok", "Po" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "Seky", "or", "Sod",
						"Toary", "Tes", "daraJn", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "Sos", "Fol",
						"STaJn", "okaJr", "Cey", "potol", "Tols", "Cor", "Cey", "dain" });
	}

	@Test
	@DisplayName("Experiments.WithStandardPopulation(FALSE): First Word in line ")
	public void WithStandardPopulationF() throws Exception {
		Text[] splitted = new Experiment.WithStandardPopulation(new Experiment.FirstWordInLine(false, false), false)
				.splitDocument(INPUT);
		TestUtil.testWordCount(splitted[0],
				new String[] { "faCys", "?ory", "syaJr", "?", "ySey", "daJn", "?", "dCar", "Sok", "Po" });
		TestUtil.testWordCount(splitted[1],
				new String[] { "Kar", "or", "y", "kair", "CtaJn", "Sar", "are", "Tar", "Tar", "Seky", "or", "yka??Jn",
						"Sod", "Toary", "Tes", "daraJn", "Sody", "okCoy", "otCol", "CoTy", "osCy", "dain", "Cor", "Sos",
						"Fol", "STaJn", "okaJr", "Cey", "?Cy", "potol", "Tols", "Cor", "Cey", "dain" });
	}

	@Test
	@DisplayName("Experiments.Initials(TRUE): FirstWordInLine(false, false)")
	public void InitialsT() throws Exception {
		Text[] splitted = new Experiment.Initials(new Experiment.FirstWordInLine(false, false), true)
				.splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "fsyddSP" });
		TestUtil.testMatch(splitted[1],
				new String[] { "yaaSSTykSKoykCSaTTdSoySTTdsosSPoSsFSSooCodCkSFSyPosPySPSoCpTdCCdKSSCtSpCrd" });
	}

	@Test
	@DisplayName("Experiments.Initials(FALSE): FirstWordInLine(false, false)")
	public void InitialsF() throws Exception {
		Text[] splitted = new Experiment.Initials(new Experiment.FirstWordInLine(false, false), false)
				.splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "f?s?yd?dSP" });
		TestUtil.testMatch(splitted[1],
				new String[] { "yaaSSTykSKoykCSaTTdSoySTTdsosSPoSsFSSooCodCkSFSyPosPySPSoC?pTdCCdKSSCtSpCrd" });
	}

	@Test
	@DisplayName("Experiments.Finals(TRUE): LastWordInLine(false, false)")
	public void FinalsT() throws Exception {
		Text[] splitted = new Experiment.Finals(new Experiment.LastWordInLine(false, false), true).splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "ynyysysay" });
		TestUtil.testMatch(splitted[1],
				new String[] { "slrnlysyryrryrnrerrryrndysnrylyrSsnyyylyynrnslnnlsynyrnryylskrynonylyyyyo" });
	}

	@Test
	@DisplayName("Experiments.Finals(FALSE): LastWordInLine(false, false)")
	public void FinalsF() throws Exception {
		Text[] splitted = new Experiment.Finals(new Experiment.LastWordInLine(false, false), false)
				.splitDocument(INPUT);
		TestUtil.testMatch(splitted[0], new String[] { "ynyysysay?" });
		TestUtil.testMatch(splitted[1],
				new String[] { "slrnlysyryrryrnrerrryrndysn?rylyrSsnyyylyynrnsl?nnlsynyrnryylskrynonylyyyyo" });
	}
}