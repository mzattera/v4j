/**
 * 
 */
package org.v4j;

import java.util.ArrayList;
import java.util.List;

import org.v4j.text.ivtff.IvtffLine;

/**
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class LineAlignment implements RegressionTest {

	@Override
	public void doTest() throws Exception {

		// test auto-alignment
		List<IvtffLine> group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("a!aca!bra"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("a!!aca!!bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<!long>"));
		group.add(new IvtffLine("a!aca!bra"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra!");
		assert group.get(1).getText().equals("a!!aca!!bra!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<!long>"));
		group.add(new IvtffLine("a!acada!<!long>"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra!");
		assert group.get(1).getText().equals("a!!acada!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("abra<!c>"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("abra!!!!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("abra<->"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("abra.");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("abr%%bra"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("abr??bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abra<->cadabra"));
		group.add(new IvtffLine("abra.cada!a"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abra.cadabra");
		assert group.get(1).getText().equals("abra.cada!!a");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abra<->cadabra"));
		group.add(new IvtffLine("abra.!!cada!a"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abra.cadabra");
		assert group.get(1).getText().equals("abra.!!cada!a");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("a%aca%bra"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("a?aca?bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<!long>"));
		group.add(new IvtffLine("a%aca%bra"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra!");
		assert group.get(1).getText().equals("a?aca?bra!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<->"));
		group.add(new IvtffLine("a!aca%bra"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abracadabra.");
		assert group.get(1).getText().equals("a!!!aca?bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<-> <!Grove's>"));
		group.add(new IvtffLine("a%aca%bra"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra.!");
		assert group.get(1).getText().equals("a?aca?bra!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra<!long>"));
		group.add(new IvtffLine("a%acada%<!long>"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra!");
		assert group.get(1).getText().equals("a?acada?!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abracadabra"));
		group.add(new IvtffLine("abra%<!c>"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("abracadabra");
		assert group.get(1).getText().equals("abra?!!!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("abra<->cadabra"));
		group.add(new IvtffLine("abra.cada%a"));
		assert IvtffLine.align(group) == false;
		assert group.get(0).getText().equals("abra.cadabra");
		assert group.get(1).getText().equals("abra.cada?a");

		group = new ArrayList<>();
		group.add(new IvtffLine("otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"));
		group.add(new IvtffLine("otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("otchol.daiin.daiin.ctho,daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		IvtffLine.align(group);
		// TODO  in this case the alignment is not perfect because the longest string contains only ?
		// Given the type of processing we do when aligning, this should never happen with the real interlinear
		// worst case, we loose some text anyway marked as unreadable, and we do not add artifacts
		assert group.get(0).getText().equals("otchol.daiin.daiin.ctho.daiin.qotaiin.!!!!!!!!!!otchy.d.!!!!!!!!shar");
		assert group.get(1).getText().equals("????????????????????????????????????????????????????????????????????");
		assert group.get(2).getText().equals("otchol.daiin.daiin.ctho.daiin.qotaiin.!!!!!!!!!!otchy.d.!!!!!!!!shar");
		assert group.get(3).getText().equals("otchol.daiin.daiin.ctho,daiin.qotaiin.!otchy.d.!!!!!!!!!!!!!!!!!shar");

		group = new ArrayList<>();
		group.add(new IvtffLine("otolchcthy "));
		group.add(new IvtffLine("otolch!tey "));
		group.add(new IvtffLine("otolchc!hy <!gallows lands on 2nd 'ch'>"));
		group.add(new IvtffLine("opolchs!hy <!gallows lands on 'sh'>"));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("otolchcthy!");
		assert group.get(1).getText().equals("otolch!tey!");
		assert group.get(2).getText().equals("otolchc!hy!");
		assert group.get(3).getText().equals("opolchs!hy!");

		// TODO complete or remove
		group = new ArrayList<>();
		group.add(new IvtffLine("okain<-><!first of two lines>"));
		group.add(new IvtffLine("oka!n<-><!was (f89r2.b.0;L)>"));
		group.add(new IvtffLine("okain<-><!Grove's X.10 word 1>"));
		group.add(new IvtffLine("okain!!! "));
		assert IvtffLine.align(group);
		assert group.get(0).getText().equals("okain.!!");
		assert group.get(1).getText().equals("oka!n.!!");
		assert group.get(2).getText().equals("okain.!!");
		assert group.get(3).getText().equals("okain!!!");
		for (IvtffLine l : group)
			System.out.println(l.getText());
		
		group = new ArrayList<>();
		group.add(new IvtffLine("qoky.shkeeo.s!chod!ar.shkol<-><!plant>chotchy.ctho!dol"));
		group.add(new IvtffLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"));
		group.add(new IvtffLine("qoky.shkeeo.r!chod.ar.shkof<-><!plant>chotchy.ctho.dol"));
		group.add(new IvtffLine("qoky.shkeeo,s.chod!ar.shkol<-><!plant>choteey.ctho!dol"));
		assert IvtffLine.align(group);
	}
}
