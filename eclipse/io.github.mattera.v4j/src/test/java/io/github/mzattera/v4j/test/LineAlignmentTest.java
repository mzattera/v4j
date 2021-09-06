/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.ParseException;

/**
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class LineAlignmentTest {

	@Test
	@DisplayName("Line alignment for building majority and concordance works")
	public void doTest() throws ParseException {

		// test auto-alignment
		List<IvtffLine> group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a!aca!bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra");
		assertEquals (group.get(1).getText(), "a!!aca!!bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<!long>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a!aca!bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "a!!aca!!bra!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<!long>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a!acada!<!long>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "a!!acada!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra<!c>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "abra!!!!!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra<->"));
		assertTrue (IvtffLine.align(group) == false);
		assertEquals (group.get(0).getText(), "abracadabra");
		assertEquals (group.get(1).getText(), "abra.");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abr%%bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra");
		assertEquals (group.get(1).getText(), "abr!!!!!bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abra<->cadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra.cada!a"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abra.cadabra");
		assertEquals (group.get(1).getText(), "abra.cada!!a");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abra<->cadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra.!!cada!a"));
		assertTrue (IvtffLine.align(group) == false);
		assertEquals (group.get(0).getText(), "abra.cadabra");
		assertEquals (group.get(1).getText(), "abra.!!cada!a");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a%aca%bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra");
		assertEquals (group.get(1).getText(), "a!!aca!!bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<!long>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a%aca%bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "a!!aca!!bra!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<->"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a!aca%bra"));
		assertTrue (IvtffLine.align(group) == false);
		assertEquals (group.get(0).getText(), "abracadabra.");
		assertEquals (group.get(1).getText(), "a!!aca!!bra");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<-> <!Grove's>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a%aca%bra"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "a!!aca!!bra!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra<!long>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> a%acada%<!long>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "a!!acada!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abracadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra%<!c>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abracadabra!");
		assertEquals (group.get(1).getText(), "abra!!!!!!!!");

		// test auto-alignment
		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> abra<->cadabra"));
		group.add(new IvtffLine("<f0r.1,+P0;y> abra.cada%a"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "abra.cadabra");
		assertEquals (group.get(1).getText(), "abra.cada!!a");

		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("<f0r.1,+P0;y> %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"));
		group.add(new IvtffLine("<f0r.1,+P0;z> otchol.daiin.daiin.ctho.daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		group.add(new IvtffLine("<f0r.1,+P0;w> otchol.daiin.daiin.ctho,daiin.qotaiin<-><!plant>otchy.d<-><!plant>shar"));
		IvtffLine.align(group);
		assertEquals (group.get(0).getText(), "otchol.daiin.daiin.ctho.daiin.qotaiin.!otchy.d.!shar!");
		assertEquals (group.get(1).getText(), "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		assertEquals (group.get(2).getText(), "otchol.daiin.daiin.ctho.daiin.qotaiin.!otchy.d.!shar!");
		assertEquals (group.get(3).getText(), "otchol.daiin.daiin.ctho,daiin.qotaiin.!otchy.d.!shar!");

		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> otolchcthy "));
		group.add(new IvtffLine("<f0r.1,+P0;y> otolch!tey "));
		group.add(new IvtffLine("<f0r.1,+P0;z> otolchc!hy <!gallows lands on 2nd 'ch'>"));
		group.add(new IvtffLine("<f0r.1,+P0;w> opolchs!hy <!gallows lands on 'sh'>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "otolchcthy!");
		assertEquals (group.get(1).getText(), "otolch!tey!");
		assertEquals (group.get(2).getText(), "otolchc!hy!");
		assertEquals (group.get(3).getText(), "opolchs!hy!");

		group = new ArrayList<>();
		group.add(new IvtffLine("<f0r.1,+P0;x> okain<-><!first of two lines>"));
		group.add(new IvtffLine("<f0r.1,+P0;y> oka!n<-><!was (f89r2.b.0;L)>"));
		group.add(new IvtffLine("<f0r.1,+P0;z> okain<-><!Grove's X.10 word 1>"));
		group.add(new IvtffLine("<f0r.1,+P0;w> okain!!! "));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "okain!");
		assertEquals (group.get(1).getText(), "oka!n!");
		assertEquals (group.get(2).getText(), "okain!");
		assertEquals (group.get(3).getText(), "okain!");
		
		group = new ArrayList<>();
		group.add(new IvtffLine("<f67r2.7,&L0;H>	!!!!!!!!!!rfchykchey.ykchys"));
		group.add(new IvtffLine("<f67r2.7,&L0;U>	!!!!!!!!!!ykchykchey.ykchys"));
		group.add(new IvtffLine("<f67r2.7,&L0;V>	<!page curled>???chey.ykchds<-><!Grove's CL.8>"));
		assertTrue (IvtffLine.align(group));
		assertEquals (group.get(0).getText(), "!!!!!!!!!!rfchykchey.ykchys!");
		assertEquals (group.get(1).getText(), "!!!!!!!!!!ykchykchey.ykchys!");
		assertEquals (group.get(2).getText(), "!!!!!!!!!!!!!???chey.ykchds!");

		// TODO ERRORE! FISSARE (VEDI PARTE A DX
		/*

<f72v1.1,@Cc;H>	!!!!!otar.air.chpaly.oteody.okchesal.otear.alshey.oleealy.sh?etey.oteos.alal.dals.alchol.ytolaiin.ydaiin.chotar.ytal.oto.shoty.otey.okchedyly.shdary.sar.ote!dy.oto.r!ar.shedy.!opshey!tey.opairaly.choshydy.otar.cheedy.otalaiin.cheeky.okalar.ysr?y.s.ok?s?t?.oteos.alar.
<f72v1.1,@Cc;U>	???.??lor,air.chpaly.oteody.okchedal.otear.alshey.oleealy.sh?!key.oteas.alal.dals,alchol.ytolaiin.ydaiin.chotar.ytal.oto.shoty.otey.okched??y.chd??!.dar.oteedy.oto!r.?r.shedy,?opshry,tey.opa!raly.ch?shydy.ot??.??!!!?.??!!!!!?.??!!!?.?!!!??.%%%%%%%%%%%%%%%%%%%%%%%%%%%
<f72v1.1,@Cc;m>	???.????r.air.chpaly.oteody.okche?al.otear.alshey.oleealy.sh?e?ey.ote?s.alal.dals.alchol.ytolaiin.ydaiin.chotar.ytal.oto.shoty.otey.okched??y.?hd??y.?ar.oteedy.oto.r.?r.shedy.?opsh?y.tey.opairaly.ch?shydy.ot??.??eed?.??alaii?.??eek?.?kal??.?sr???s????s?t???te?s?a?ar?
<f72v1.1,@Cc;c>	????????r.air.chpaly.oteody.okche?al.otear.alshey.oleealy.sh???ey.ote?s.alal.dals.alchol.ytolaiin.ydaiin.chotar.ytal.oto.shoty.otey.okched??y.?hd???.?ar.ote?dy.oto?r??r.shedy.?opsh?y?tey.opa?raly.ch?shydy.ot??.??????.????????.??????.??????.???????????????????????????

		 
		 */
		// TODO add these tests, maybe in a separate harness
		/*
		<f80v.4,+P0;H>	qokedy.qokar.qokai!n.chedy.qol.qol.shect<!@T>yhy!!.qo!l!y<-><!figure>
		<f80v.4,+P0;C>	qokedy.qokan.qokai!n.chedy.qol.qol.sheek!!!!!!y!!.qo!l!y<-><!figure>
		<f80v.4,+P0;F>	qokede.qokan.qokaiin.chedy.qol.qol.shect!!!!!hy!!.qo!l!y<-><!figure>
		<f80v.4,+P0;V>	qokedy.qokar.qokai!n.chedy.qol.qol.shect!!!!!hyhy.qo.l.y<-><!figure>
		<f80v.4,+P0;m>	qokedy.qoka?.qokai!n.chedy.qol.qol.shect!!!!!hy!!.qo!l!y!
		<f80v.4,+P0;c>	qoked?.qoka?.qokai?n.chedy.qol.qol.she??!!!!??y??.qo?l?y!

<f67r2.7,&L0;H>	!!!!!!!!!!rfchykchey.ykchys
<f67r2.7,&L0;U>	!!!!!!!!!!ykchykchey.ykchys
<f67r2.7,&L0;V>	<!page curled>???chey.ykchds<-><!Grove's CL.8>
<f67r2.7,&L0;m>	!!!!!!!!!!??chykchey.ykchys!
<f67r2.7,&L0;c>	!!!!!!!!!!??????chey.ykch?s!

<f93r.4,+P0;H>	sol.shotol.qokchodal.shody.chotol!s<-><!plant>otol.ykchdg
<f93r.4,+P0;C>	sol.shotol.qokchddal.shody.chotol.s%%%%%%%%%%%%%%%%%%%
<f93r.4,+P0;F>	sol.shotol.q!kchodal.shody.chotol!s<-><!plant>otol.ykchdm
<f93r.4,+P0;U>	sol.shotol.qokchodal.shody.chotol.s<-><!plant>otol.ykchdm
<f93r.4,+P0;V>	sol.shotol.qakchodal.shody.chotol!s<-><!plant>otol.ykchdm

		*/
	}
}
