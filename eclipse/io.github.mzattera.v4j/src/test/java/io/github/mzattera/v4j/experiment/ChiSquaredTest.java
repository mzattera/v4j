package io.github.mzattera.v4j.experiment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.experiment.ChiSquared.CharBin;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.TestUtil;

/**
 * Tests ChiSquared class.
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class ChiSquaredTest {

	@Test
	@DisplayName("CharBin merge")
	public void  CharBinMerge() throws Exception {
		CharBin b1 = new CharBin('a');
		
		List<Character> l = new ArrayList<>();
		l.add('a');
		l.add('b');
		
		CharBin b2 = new CharBin('b',1,0.1);
		
		CharBin x = new CharBin();
		x.merge(b1);
		x.merge(b2);
		
		TestUtil.testContentMatch(x.getChars(),l);
		assertEquals(b2.getCount(), x.getCount());
		assertEquals(b2.getFrequency(), x.getFrequency());		
		
		
		final CharBin b3 = new CharBin('a', 2, 0.2);
		 assertThrows(IllegalArgumentException.class, () -> {
			 x.merge(b3);
	  });
		
		b1 = new CharBin('c', 2, 0.2);
		b1.merge(x);
		l.add('c');
		TestUtil.testContentMatch(b1.getChars(),l);
		assertEquals(b1.getCount(),3);
		assertEquals(b1.getFrequency(),0,3);		
	}

	@Test
	@DisplayName("CharBin euqals")
	public void  CharBinEquals() throws Exception {
		CharBin b1 = new CharBin();
		CharBin b2 = new CharBin();
		assertTrue(b1.equals(b2));
		
		b1 = new CharBin();
		b2 = new CharBin('a');
		assertFalse(b1.equals(b2));
		
		b1 = new CharBin('a');
		b2 = new CharBin('a');
		assertTrue(b1.equals(b2));
		
		b1 = new CharBin('a');
		b2 = new CharBin('b');
		assertFalse(b1.equals(b2));
		
		b1 = new CharBin('a',1,1.0);
		b2 = new CharBin('a',1,1.0);
		assertTrue(b1.equals(b2));
		
		b1 = new CharBin('a',1,1.0);
		b2 = new CharBin('b',1,1.0);
		assertFalse(b1.equals(b2));
		
		b1 = new CharBin('a',1,1.0);
		b2 = new CharBin('a',2,1.0);
		assertFalse(b1.equals(b2));
		
		b1 = new CharBin('a',1,1.0);
		b2 = new CharBin('a',1,0.1);
		assertFalse(b1.equals(b2));
		
		assertFalse (new CharBin().equals(""));
	}

	
	@Test
	@DisplayName("chiSquareTest(txt, expected, toUpper)")
	public void chiSquareTest() throws Exception {
		TextString pop = new TextString("aa bb cc dd");
		final List<CharBin> expected = ChiSquared.getCharDistribution(pop, false);
		
		final TextString txt = new TextString("a b c d");
		assertTrue(ChiSquared.chiSquareTest(txt, expected, false) > 0.99);
	
		 assertThrows(IllegalArgumentException.class, () -> {
			 ChiSquared.chiSquareTest(txt, expected, true);
	  });
		
		final TextString txt2 = new TextString("a b c d f");
		 assertThrows(IllegalArgumentException.class, () -> {
			 ChiSquared.chiSquareTest(txt2, expected, false);
	  });
	}

	@Test
	@DisplayName("observe(doc, c, toUpper)")
	public void observe1() throws Exception {
		TextString txt = new TextString("abacadaeaf");

		long[] o = ChiSquared.observe(txt, 'a', false);
		TestUtil.testMatch(new long[] { 5, 5 }, o);

		o = ChiSquared.observe(txt, 'a', true);
		TestUtil.testMatch(new long[] { 0, 10 }, o);

		o = ChiSquared.observe(txt, 'A', true);
		TestUtil.testMatch(new long[] { 5, 5 }, o);
	}

	@Test
	@DisplayName("observe(doc, categories, toUpper, extend)")
	public void observe2() throws Exception {
		TextString txt = new TextString("abacadaeaf???");
		List<CharBin> categories = new ArrayList<>();
		categories.add(new CharBin('a'));

		long[] o = ChiSquared.observe(txt, categories, false, false);
		TestUtil.testMatch(new long[] { 5 }, o);

		o = ChiSquared.observe(txt, categories, false, true);
		TestUtil.testMatch(new long[] { 5, 5 }, o);

		categories.clear();
		categories.add(new CharBin('A'));
		o = ChiSquared.observe(txt, categories, false, false);
		TestUtil.testMatch(new long[] { 0 }, o);

		o = ChiSquared.observe(txt, categories, false, true);
		TestUtil.testMatch(new long[] { 0, 10 }, o);

		o = ChiSquared.observe(txt, categories, true, false);
		TestUtil.testMatch(new long[] { 5 }, o);

		o = ChiSquared.observe(txt, categories, true, true);
		TestUtil.testMatch(new long[] { 5, 5 }, o);
	}

	@Test
	@DisplayName("observe(counter, bins, addGenericBin)")
	public void observe3() throws Exception {
		TextString txt = new TextString("fillo pimmo gibbo fillo fillo as?oz pimmo");
		Counter<String> population = txt.getWords(true);

		Map<String, Integer> bins = new HashMap<>();

		long[] o = ChiSquared.observe(population, bins, false);
		TestUtil.testMatch(new long[]{}, o);

		bins.put("fillo", 0);

		o = ChiSquared.observe(population, bins, false);
		TestUtil.testMatch(new long[] { 3 }, o);

		o = ChiSquared.observe(population, bins, true);
		TestUtil.testMatch(new long[] { 3, 5 }, o); // note ? is a word separator

		bins.put("pimmo", 1);

		o = ChiSquared.observe(population, bins, false);
		TestUtil.testMatch(new long[] { 3, 2 }, o);

		o = ChiSquared.observe(population, bins, true);
		TestUtil.testMatch(new long[] { 3, 2, 3 }, o); // note ? is a word separator
	}

	@Test
	@DisplayName("getCharDistribution(doc, toUpper)")
	public void getCharDistribution() throws Exception {
		TextString txt = new TextString("abacadaeaf???");
		
		List<CharBin> categories = new ArrayList<>();
		categories.add(new CharBin('a',5,0.5));
		categories.add(new CharBin('b',1,0.1));
		categories.add(new CharBin('c',1,0.1));
		categories.add(new CharBin('d',1,0.1));
		categories.add(new CharBin('e',1,0.1));
		categories.add(new CharBin('f',1,0.1));
		TestUtil.testContentMatch(categories, ChiSquared.getCharDistribution(txt, false));
		
		categories.clear();
		categories.add(new CharBin('A',5,0.5));
		categories.add(new CharBin('B',1,0.1));
		categories.add(new CharBin('C',1,0.1));
		categories.add(new CharBin('D',1,0.1));
		categories.add(new CharBin('E',1,0.1));
		categories.add(new CharBin('F',1,0.1));
		TestUtil.testContentMatch(categories, ChiSquared.getCharDistribution(txt, true));
	}

	@Test
	@DisplayName("adjustDistribution(distribution, sampleSize)")
	public void adjustDistribution() throws Exception {
		TextString txt = new TextString("aaa aaa a bb c");
		
		List<CharBin> distr = ChiSquared.getCharDistribution(txt, false);
		List<CharBin> adj = ChiSquared.adjustDistribution(distr,50);
		TestUtil.testContentMatch(distr, adj);
		
		adj = ChiSquared.adjustDistribution(distr,25);
		List<CharBin> l = new ArrayList<>();
		l.add(new CharBin('a',7,0.7));
		CharBin b = new CharBin('b',2,0.2);
		b.merge(new CharBin('c',1,0.1));
		l.add(b);
		TestUtil.testContentMatch(adj, l);
		
		final List<CharBin> d1 = ChiSquared.getCharDistribution(txt, false);
		 Exception e = assertThrows(IllegalArgumentException.class, () -> {
			 ChiSquared.adjustDistribution(d1,4);
	  });
		 assertEquals(e.getMessage(),"Sample too small.");

		txt = new TextString("aaa aaa aa b c xxx xxx xxx x");
		
		distr = ChiSquared.getCharDistribution(txt, false);
		adj = ChiSquared.adjustDistribution(distr,100);
		TestUtil.testContentMatch(distr, adj);
		
		adj = ChiSquared.adjustDistribution(distr,60);
		l.clear();
		l.add(new CharBin('x',10,0.5));
		l.add(new CharBin('a',8,0.4));
		b = new CharBin('b',1,0.05);
		b.merge(new CharBin('c',1,0.05));
		l.add(b);
		TestUtil.testContentMatch(adj, l);	
		
		final List<CharBin> d2 = ChiSquared.getCharDistribution(txt, false);
		 e = assertThrows(IllegalArgumentException.class, () -> {
			 ChiSquared.adjustDistribution(d2,9);
	  });
		 assertEquals(e.getMessage(),"Not enough degrees of feeedom.");
	}
}