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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.util.ClassLoaderUtil;

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
	 * Gets the URL for a file in the resource folder.
	 * 
	 * @param resourceName Name for the resource, including path.
	 * @return the URL for a file in the resource folder.
	 */
	public static URL getResourceURL(String resourceName) {
		return ClassLoaderUtil.getResource(resourceName, FileUtil.class);
	}

	/**
	 * Gets the URL for a File in the resource folder.
	 * 
	 * @param resourceName Name for the resource, including path.
	 * @return the URL for a File in the resource folder.
	 * @throws URISyntaxException
	 */
	public static File getResourceFile(String resourceName) {
		URL url = ClassLoaderUtil.getResource(resourceName, FileUtil.class);
		try {
			return (url == null) ? null : new File(url.toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Write a string into an ASCII file.
	 */
	public static void write(String txt, String fileName) throws IOException {
		write(txt, fileName, "ASCII");
	}

	/**
	 * Write a string into a file with given encoding.
	 */
	public static void write(String txt, String fileName, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), encoding));
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
		write(txt, fileName, "UTF-8");
	}

	/**
	 * Write a list of strings into a file with given encoding.
	 */
	public static void write(List<String> txt, String fileName, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), encoding));
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

		List<String> rows = new ArrayList<String>();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
			String row;
			while ((row = in.readLine()) != null) {
				rows.add(row);
			}

			return rows;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
				}
		}
	}

}
