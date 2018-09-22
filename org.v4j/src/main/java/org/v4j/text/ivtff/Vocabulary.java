package org.v4j.text.ivtff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a dcitionary that contains words and their occurrences. TODO:
 * better sorting method.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class Vocabulary {

	/** Sort value to sort by ascending alphabetical order */
	public static final int SORT_BY_WORD_ASCENDING = 0;

	/** Sort value to sort by descending number of occurrences */
	public static final int SORT_BY_COUNT_DESCENDING = 1;

	/** Map each word (String) in its Entry */
	private Map<String, Entry> _words;

	/** Elements sorted alphabetically (null if not sorted yet) */
	private String[] _byWord = null;

	/** Elements sorted alphabetically (null if not sorted yet) */
	private String[] _byCount = null;

	/** Total word count (sum of occurrences for all words) */
	private int _count;

	/** Creates one empty Vocabulary */
	public Vocabulary() {
		_words = new HashMap<String, Entry>();
		_count = 0;
		noSorted();
	}

	/** Creates one Vocabulary with items from another one */
	public Vocabulary(Vocabulary d) {
		this();
		String[] words = d.getWords();
		for (int i = 0; i < words.length; ++i) {
			add(words[i], d.count(words[i]));
		}
	}

	/** Creates a new instance of Vocabulary from file */
	public Vocabulary(String fin) throws IOException, ParseException {
		this(new File(fin));
	}

	/**
	 * Creates a new instance of Document.
	 * 
	 * @param in
	 *            THe file from which the document has to be read.
	 */
	public Vocabulary(File fin) throws IOException, ParseException {
		this();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fin));

			String row;
			while ((row = in.readLine()) != null) {
				if (row.startsWith("#"))
					continue; // comment
				String[] s = row.split("\t");
				if ((s.length != 2) && (s.length != 3))
					throw new ParseException("Invalid line in dictionary: " + row);
				int c = 0;
				try {
					c = Integer.parseInt(s[1]);
				} catch (NumberFormatException e) {
					new ParseException("Invalid line in dictionary: " + row);
				}
				add(s[0], c);
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
				}
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Adds a word to the dictionary (it occurrences are increased by one).
	 * 
	 * @return number of that word occurrences.
	 */
	public int add(String word) {
		return add(word, 1);
	}

	/**
	 * Adds a word to the dictionary (it occurrences are increased by given amount).
	 * 
	 * @return number of that word occurrences.
	 */
	public int add(String word, int occurrences) {
		Entry e = (Entry) _words.get(word);
		if (e == null) {
			e = new Entry(word, 0);
			_words.put(word, e);
		}
		_count += occurrences;
		noSorted();
		return e.incCount(occurrences);
	}

	/**
	 * Remove a word form the dictionary.
	 */
	public void remove(String word) {
		int c = count(word);
		_words.remove(word);
		_count -= c;
		noSorted();
	}

	/**
	 * Return true if the disctionary contains a given word.
	 */
	public boolean contains(String word) {
		return (count(word) > 0);
	}

	/**
	 * Return number of words in the dictionary.
	 */
	public int size() {
		return _words.size();
	}

	/**
	 * Return number of occurrences for one word.
	 */
	public int count(String word) {
		Entry e = (Entry) _words.get(word);
		if (e == null)
			return 0;
		return e._count;
	}

	/**
	 * Return sum of occurrences for all words.
	 */
	public int count() {
		return _count;
	}

	/**
	 * Returns all words, sorted in decending count.
	 */
	public String[] getWords() {
		return byCount();
	}

	/**
	 * Returns all words, sorted in given order.
	 */
	public String[] getWords(int order) {
		switch (order) {
		case SORT_BY_WORD_ASCENDING:
			return byWord();
		case SORT_BY_COUNT_DESCENDING:
			return byCount();
		default:
			throw new IllegalArgumentException("Invalid sort ID: " + order);
		}
	}

	/**
	 * Returns all words, sorted in ascending alphabetical order.
	 */
	private String[] byWord() {
		if (_byWord == null) { // redo sort
			List<String> l = new ArrayList<String>(_words.keySet());
			Collections.sort(l);
			_byWord = new String[l.size()];
			l.toArray(_byWord);
		}

		return (String[]) _byWord.clone();
	}

	/**
	 * Returns all words, sorted in decending count.
	 */
	private String[] byCount() {
		if (_byCount == null) { // redo sort

			Comparator<Object> c = new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					Entry e1 = (Entry) o1, e2 = (Entry) o2;
					if (e1._count == e2._count)
						return 0;
					else if (e1._count < e2._count)
						return 1;
					else
						return -1;
				}
			};

			List<Entry> l = new ArrayList<Entry>(_words.values());
			Collections.sort(l, c);
			_byCount = new String[l.size()];
			for (int i = 0; i < _byCount.length; ++i) {
				_byCount[i] = ((Entry) l.get(i))._word;
			}
		}

		return (String[]) _byCount.clone();
	}

	/** Mark the classs as not sorted */
	private void noSorted() {
		_byWord = null;
		_byCount = null;
	}

	/**
	 * Print the disctionary.
	 */
	public void print() {
		print(SORT_BY_COUNT_DESCENDING);
	}

	/**
	 * Print the dictionary, with given order.
	 */
	public void print(int order) {
		System.out.println("#---[Total words " + size() + " (" + count() + " occurrences) ]------------\n");
		String[] word = getWords(order);
		for (int i = 0; i < word.length; ++i) {
			System.out.println(word[i] + "\t" + count(word[i]) + "\t" + (count(word[i]) * 100.0 / count()));
		}
		System.out.println("#--------------------------------------------------------------------------");
	}

	/**
	 * Write Document into one file.
	 */
	public void write(String fileName) throws IOException {
		write(fileName, "ASCII");
	}

	/**
	 * Write Document into one output stream.
	 * 
	 * @param os
	 *            stream that will contain the document.
	 */
	public void write(String fileName, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), encoding));

			out.write("#---[Total words " + size() + " (" + count() + " occurrences) ]------------");
			out.newLine();
			String[] word = getWords();
			for (int i = 0; i < word.length; ++i) {
				out.write(word[i] + "\t" + count(word[i]));
				out.newLine();
			}
			out.flush();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * Inner class to store word counts.
	 */
	private class Entry {
		public String _word;
		public int _count;

		public Entry(String word, int count) {
			_word = word;
			_count = count;
		}

		public int incCount(int occurrences) {
			return _count += occurrences;
		}
	}
}
