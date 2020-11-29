/**
 * 
 */
package org.v4j;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.util.Counter;

/**
 * Tests io.github.mattera.v4j.applications.CountNWords.process()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class CountNWords implements RegressionTest {

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + "#\n" + "<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	@Override
	public void doTest() throws Exception {

		IvtffText doc = new IvtffText(txt);

		assert (doc.getText().equals(
				"qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>..ychear.otchal.char..char.ckhy!<-><!plant>.!or.chear.kor.chodaly.chom<$><!plant>"));
		assert (doc.getPlainText().equals(
				"qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom"));

		Counter<String> c = io.github.mattera.v4j.applications.CountNWords.process(doc, 3, false);
		assert (c.size() == 16);
		assert (c.getCount("qodar.??s.eey") == 1);
		assert (c.getCount("??s.eey.kcheolokal") == 1);
		assert (c.getCount("eey.kcheolokal.do") == 1);
		assert (c.getCount("kcheolokal.do.r") == 1);
		assert (c.getCount("do.r.chear") == 1);
		assert (c.getCount("r.chear.een") == 1);
		assert (c.getCount("chear.een.ychear") == 1);
		assert (c.getCount("een.ychear.otchal") == 1);
		assert (c.getCount("ychear.otchal.char") == 1);
		assert (c.getCount("otchal.char.char") == 1);
		assert (c.getCount("char.char.ckhy") == 1);
		assert (c.getCount("char.ckhy.or") == 1);
		assert (c.getCount("ckhy.or.chear") == 1);
		assert (c.getCount("or.chear.kor") == 1);
		assert (c.getCount("chear.kor.chodaly") == 1);
		assert (c.getCount("kor.chodaly.chom") == 1);

		c = io.github.mattera.v4j.applications.CountNWords.process(doc, 3, true);
		assert (c.size() == 14);
		assert (c.getCount("eey.kcheolokal.do") == 1);
		assert (c.getCount("kcheolokal.do.r") == 1);
		assert (c.getCount("do.r.chear") == 1);
		assert (c.getCount("r.chear.een") == 1);
		assert (c.getCount("chear.een.ychear") == 1);
		assert (c.getCount("een.ychear.otchal") == 1);
		assert (c.getCount("ychear.otchal.char") == 1);
		assert (c.getCount("otchal.char.char") == 1);
		assert (c.getCount("char.char.ckhy") == 1);
		assert (c.getCount("char.ckhy.or") == 1);
		assert (c.getCount("ckhy.or.chear") == 1);
		assert (c.getCount("or.chear.kor") == 1);
		assert (c.getCount("chear.kor.chodaly") == 1);
		assert (c.getCount("kor.chodaly.chom") == 1);
	}
}
