/* Copyright (c) 2018-2024 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Various file utilities.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class FileUtil {

	private FileUtil() {
	}

	/**
	 * Write a string into an ASCII file.
	 */
	public static void write(String txt, String fileName) throws IOException {
		write(txt, new File(fileName), "ASCII");
	}

	/**
	 * Write a string into an ASCII file.
	 */
	public static void write(String txt, File file) throws IOException {
		write(txt, file, "ASCII");
	}

	/**
	 * Write a string into a file with given encoding.
	 */
	public static void write(String txt, String fileName, String encoding) throws IOException {
		write(txt, new File(fileName), encoding);
	}

	/**
	 * Write a string into a file with given encoding.
	 */
	public static void write(String txt, File file, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			out.write(txt);
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
	 * Write a list of strings into a file. Uses UTF-8 encoding.
	 */
	public static void write(List<String> txt, String fileName) throws IOException {
		write(txt, new File(fileName), "UTF-8");
	}

	/**
	 * Write a list of strings into a file. Uses UTF-8 encoding.
	 */
	public static void write(List<String> txt, File file) throws IOException {
		write(txt, file, "UTF-8");
	}

	/**
	 * Write a list of strings into a file with given encoding.
	 */
	public static void write(List<String> txt, String fileName, String encoding) throws IOException {
		write(txt, new File(fileName), encoding);
	}

	/**
	 * Write a list of strings into a file with given encoding.
	 */
	public static void write(List<String> txt, File file, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			for (String s : txt) {
				out.write(s);
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
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * It assumes file is in UTF-8 encoding.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(File file) throws IOException {
		return read(file, "UTF-8");
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(File file, String encoding) throws IOException {
		return read(file.getCanonicalPath(), encoding);
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * It assumes file is in UTF-8 encoding.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(String fileName) throws IOException {
		return read(fileName, "UTF-8");
	}

	/**
	 * Reads given text file and returns its contents as a list of non-empty lines.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(String fileName, String encoding) throws IOException {
		return read(new FileInputStream(fileName), encoding);
	}

	/**
	 * Reads text from given stream and returns its contents as a list of non-empty
	 * lines.
	 * 
	 * @throws IOException
	 */
	public static List<String> read(InputStream stream, String encoding) throws IOException {

		List<String> rows = new ArrayList<String>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, encoding))) {
			String row;
			while ((row = in.readLine()) != null) {
				rows.add(row);
			}

			return rows;
		}
	}
}
