/**
 * 
 */
package io.github.mzattera.v4j.applications.clustering;

import java.io.File;
import java.io.IOException;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.ElementSplitter;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.PageHeader;
import io.github.mattera.v4j.text.ivtff.PageSplitter;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.clustering.BagOfWords.BagOfWordsMode;

/**
 * Static class holding global configuration parameters for application srelated
 * to clustering.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public final class ClusteringConfiguration {

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.MAJORITY;

	////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	static final ElementSplitter<IvtffPage> SPLIT_BY_PARCHMENTS = new PageSplitter.Builder().byParchment().build();

	@SuppressWarnings("unused")
	private static final ElementSplitter<IvtffPage> SPLIT_BY_PAGES = new ElementSplitter<IvtffPage>() {
		@Override
		public String getCategory(IvtffPage element) {
			return element.getId();
		}

		@Override
		public String toString() {
			return "Split by page.";
		}
	};

	/**
	 * This decides how the initial document is split before being processed (by
	 * pages or parchments; see above).
	 */
//	public static final ElementSplitter<IvtffPage> DOCUMENT_SPLITTER = SPLIT_BY_PAGES;
	public static final ElementSplitter<IvtffPage> DOCUMENT_SPLITTER = SPLIT_BY_PARCHMENTS;

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * How Bag of Words are built out of the text.
	 */
	public static final BagOfWordsMode BOW_MODE = BagOfWordsMode.COUNT;
//	public static final BagOfWordsMode BOW_MODE = BagOfWordsMode.TF_IDF;

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Folder where to place outputs.
	 */
	public static final String OUTPUT_FOLDER = "D:\\";

	////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	private static final ElementFilter<IvtffPage> NO_OUTLIERS = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			for (String o : PageHeader.OUTLIER_PAGES) {
				if (element.getDescriptor().getId().equals(o))
					return false;
			}

			return true;
		}

		@Override
		public String toString() {
			return "Remove outliers";
		}
	};

	@SuppressWarnings("unused")
	private static final ElementFilter<IvtffPage> NO_OUTLIERS_C_A = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			if (NO_OUTLIERS.keep(element))
				return !element.getDescriptor().getIllustrationType().equals("C")
						&& !element.getDescriptor().getIllustrationType().equals("A");
			else
				return false;
		}

		@Override
		public String toString() {
			return "Remove outliers, C and A pages";
		}
	};

	@SuppressWarnings("unused")
	private static final ElementFilter<IvtffPage> HA_P_NO_OUTLIERS = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			if (NO_OUTLIERS.keep(element))
				return element.getDescriptor().getIllustrationType().equals("P")
						|| (element.getDescriptor().getIllustrationType().equals("H")
								&& element.getDescriptor().getLanguage().equals("A"));
			else
				return false;
		}

		@Override
		public String toString() {
			return "P and HA pages, after removing outliers";
		}
	};

	@SuppressWarnings("unused")
	static final ElementFilter<IvtffPage> BY_PARCH_NO_OUTLIERS = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			if (NO_OUTLIERS.keep(element)) {
				for (int p : PageHeader.OUTLIER_PARCHMENTS) {
					if ((element.getDescriptor().getParchment() == p) || (element.getDescriptor().getParchment() == -1))
						return false;
				}
				return true;
			}

			return true;
		}

		@Override
		public String toString() {
			return "Remove outliers and outlier parchments (29, 30, 31, 32, 40).";
		}
	};

	@SuppressWarnings("unused")
	static final ElementFilter<IvtffPage> BY_PARCH_B_NO_OUTLIERS = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			if (BY_PARCH_NO_OUTLIERS.keep(element))
				return element.getDescriptor().getIllustrationType().equals("B")
						|| element.getDescriptor().getIllustrationType().equals("S")
						|| (element.getDescriptor().getIllustrationType().equals("H")
								&& element.getDescriptor().getLanguage().equals("B"));
			else
				return false;
		}

		@Override
		public String toString() {
			return "Remove outliers, outlier parchments (29, 30, 31, 32, 40), and keep only B, S, HB pages.";
		}
	};

	@SuppressWarnings("unused")
	static final ElementFilter<IvtffPage> BY_PARCH_HA_n_P_NO_OUTLIERS = new ElementFilter<IvtffPage>() {

		@Override
		public boolean keep(IvtffPage element) {
			if (BY_PARCH_NO_OUTLIERS.keep(element))
				return element.getDescriptor().getIllustrationType().equals("P")
						|| (element.getDescriptor().getIllustrationType().equals("H")
								&& element.getDescriptor().getLanguage().equals("A"));
			else
				return false;
		}

		@Override
		public String toString() {
			return "Remove outliers, outlier parchments (29, 30, 31, 32, 40), and keep only P and HA pages.";
		}
	};

	/**
	 * Use this to decide which pages to keep for the clustering.
	 */
//	public static final ElementFilter<IvtffPage> OUTLIER_FILTER = NO_OUTLIERS;
//	public static final ElementFilter<IvtffPage> OUTLIER_FILTER = HA_P_NO_OUTLIERS;
//	public static final ElementFilter<IvtffPage> OUTLIER_FILTER = BY_PARCH_NO_OUTLIERS;
//	public static final ElementFilter<IvtffPage> OUTLIER_FILTER = BY_PARCH_B_NO_OUTLIERS;
	public static final ElementFilter<IvtffPage> OUTLIER_FILTER = BY_PARCH_HA_n_P_NO_OUTLIERS;

	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints out the configuration settings.
	 * 
	 * @throws IOException if output folder is not properly configured.
	 */
	public static void print() throws IOException {
		System.out.println("Using transcription     : " + TRANSCRIPTION);
		System.out.println("Using transcription type: " + TRANSCRIPTION_TYPE);
		System.out.println("Spliting document using : " + DOCUMENT_SPLITTER);
		System.out.println("Using BoW mode          : " + BOW_MODE);
		System.out.println("Output to               : " + new File(OUTPUT_FOLDER).getCanonicalPath());
		System.out.println("Outlier filter          : " + OUTLIER_FILTER);
		System.out.print("Outliers                :");
		for (String o : PageHeader.OUTLIER_PAGES) {
			System.out.print(" " + o);
		}
		System.out.println("\n");
	}
}
