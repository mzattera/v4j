/**
 * 
 */
package org.v4j;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.util.Counter;

/**
 * Tests Text.getWords()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class WordCount implements RegressionTest {

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + 
			"#\n" + 
			"<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	@Override
	public void doTest() throws Exception {

		IvtffText doc = new IvtffText(txt);

		assert (doc.getText().equals("qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>..ychear.otchal.char..char.ckhy!<-><!plant>.!or.chear.kor.chodaly.chom<$><!plant>"));
		assert (doc.getPlainText().equals("qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom"));

		Counter<String> c = doc.getWords(false);
		assert (c.size() == 16);
		assert (c.getCount("char") == 2);
		assert (c.getCount("chear") == 2);
		assert (c.getCount("ckhy") == 1);
		assert (c.getCount("chodaly") == 1);
		assert (c.getCount("chom") == 1);
		assert (c.getCount("do") == 1);
		assert (c.getCount("een") == 1);
		assert (c.getCount("eey") == 1);
		assert (c.getCount("kcheolokal") == 1);
		assert (c.getCount("kor") == 1);
		assert (c.getCount("or") == 1);
		assert (c.getCount("otchal") == 1);
		assert (c.getCount("qodar") == 1);
		assert (c.getCount("r") == 1);
		assert (c.getCount("ychear") == 1);
		assert (c.getCount("??s") == 1);

		c = doc.getWords(true);
		assert (c.size() == 15);
		assert (c.getCount("char") == 2);
		assert (c.getCount("chear") == 2);
		assert (c.getCount("ckhy") == 1);
		assert (c.getCount("chodaly") == 1);
		assert (c.getCount("chom") == 1);
		assert (c.getCount("do") == 1);
		assert (c.getCount("een") == 1);
		assert (c.getCount("eey") == 1);
		assert (c.getCount("kcheolokal") == 1);
		assert (c.getCount("kor") == 1);
		assert (c.getCount("or") == 1);
		assert (c.getCount("otchal") == 1);
		assert (c.getCount("qodar") == 1);
		assert (c.getCount("r") == 1);
		assert (c.getCount("ychear") == 1);
	}
}
