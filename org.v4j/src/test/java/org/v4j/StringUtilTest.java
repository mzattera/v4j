/**
 * 
 */
package org.v4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.v4j.applications.BuildDocumentVersion;
import org.v4j.text.ivtff.IvtffLine;
import org.v4j.util.StringUtil;

/**
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class StringUtilTest implements RegressionTest {

	@Override
	public void doTest() throws Exception {

		// Increase length by 1
		String s1 = "a!b!c";
		List<String> chk = new ArrayList<>();
		chk.add("a!!b!c");
		chk.add("a!b!!c");
		Collection<String> res = StringUtil.growLength(s1, 1);
		assert res.size() == chk.size();
		for (String c : chk)
			assert res.contains(c);

		// Increase length by 2
		s1 = "a!b!c";
		chk = new ArrayList<>();
		chk.add("a!!!b!c");
		chk.add("a!b!!!c");
		chk.add("a!!b!!c");
		res = StringUtil.growLength(s1, 2);
		assert res.size() == chk.size();
		for (String c : chk)
			assert res.contains(c);

		// Increase length by 1
		s1 = "!b!";
		chk = new ArrayList<>();
		chk.add("!!b!");
		chk.add("!b!!");
		res = StringUtil.growLength(s1, 1);
		assert res.size() == chk.size();
		for (String c : chk)
			assert res.contains(c);

		// Increase length by 2
		s1 = "!b!";
		chk = new ArrayList<>();
		chk.add("!!!b!");
		chk.add("!b!!!");
		chk.add("!!b!!");
		res = StringUtil.growLength(s1, 2);
		assert res.size() == chk.size();
		for (String c : chk)
			assert res.contains(c);

		// test auto-alignment
		String s2 = "abracadabra";
		s1 = "a!aca!bra";
		String a = StringUtil.align(s1, s2);
		assert a.equals("a!!aca!!bra");

		// test auto-alignment
		s2 = "abracadabra<long>";
		s1 = "a!aca!bra";
		a = StringUtil.align(s1, s2);
		assert a.equals("a!!aca!!bra");

		// test auto-alignment
		s2 = "abracadabra<long>";
		s1 = "a!acada!<long>";
		a = StringUtil.align(s1, s2);
		assert a.equals("a!!acada!!!<long>");

		// test auto-alignment
		s2 = "abracadabra";
		s1 = "abra<!c>";
		a = StringUtil.align(s1, s2);
		assert a.equals("abra<!c>");

		// test auto-alignment
		s2 = "abracadabra";
		s1 = "abra!<!c>";
		a = StringUtil.align(s1, s2);
		assert a.equals("abra!!!<!c>");

		// test auto-alignment
		s2 = "abra<->cadabra";
		s1 = "abra.cada!a";
		a = StringUtil.align(s1, s2);
		assert a.equals("abra!!.cada!!a");

		// test auto-alignment
		s2 = "abracadabra";
		s1 = "a%aca%bra";
		a = StringUtil.align(s1, s2);
		assert a.equals("a!%aca!%bra");

		// test auto-alignment
		s2 = "abracadabra<long>";
		s1 = "a%aca%bra";
		a = StringUtil.align(s1, s2);
		assert a.equals("a!%aca!%bra");

		// test auto-alignment
		s2 = "abracadabra<long>";
		s1 = "a%acada%<long>";
		a = StringUtil.align(s1, s2);
		assert a.equals("a!%acada!!%<long>");

		// test auto-alignment
		s2 = "abracadabra";
		s1 = "abra%<!c>";
		a = StringUtil.align(s1, s2);
		assert a.equals("abra!!%<!c>");

		// test auto-alignment
		s2 = "abra<->cadabra";
		s1 = "abra.cada%a";
		a = StringUtil.align(s1, s2);
		assert a.equals("abra!!.cada!%a");

		List<IvtffLine> group = new ArrayList<>();
		group.add(new IvtffLine("otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"));
		group.add(new IvtffLine("otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("otchol.daiin.daiin.ctho,daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		BuildDocumentVersion.alignText(group);
		assert group.get(1).getText().equals("!!!!!!%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

		group = new ArrayList<>();
		group.add(new IvtffLine("otolchcthy "));
		group.add(new IvtffLine("otolch!tey "));
		group.add(new IvtffLine("otolchc!hy <!gallows lands on 2nd 'ch'>"));
		group.add(new IvtffLine("opolchs!hy <!gallows lands on 'sh'>"));
		BuildDocumentVersion.alignText(group);
		assert group.get(0).getText().equals("otolchcthy");
		assert group.get(1).getText().equals("otolch!tey");
		assert group.get(2).getText().equals("otolchc!hy");
		assert group.get(3).getText().equals("opolchs!hy");

		group = new ArrayList<>();
		group.add(new IvtffLine("okain<-><!first of two lines>"));
		group.add(new IvtffLine("oka!n<-><!was (f89r2.b.0;L)>"));
		group.add(new IvtffLine("okain<-><!Grove's X.10 word 1>"));
		group.add(new IvtffLine("okain!!! "));
		BuildDocumentVersion.alignText(group);
		for (IvtffLine l : group)
			System.out.println(l.getText());
	}
}
