package io.github.mattera.v4j.applications.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
import io.github.mattera.v4j.text.ivtff.LineSplitter;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;

public class QT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// The document to process, divided in lines
			IvtffText doc = new IvtffText(new File("D:\\Interlinear_ivtff_1.5.txt"));
			Map<String, IvtffText> groups = doc.splitLines(new LineSplitter.Builder().byPage().byNumber().build());

			for (IvtffText docIvtffText : groups.values()) {
				// Concordance version of the line
				IvtffText concordance = docIvtffText.filterLines(new ElementFilter<IvtffLine>() {

					@Override
					public boolean keep(IvtffLine element) {
						// TODO Auto-generated method stub
						return element.getDescriptor().getTranscriber().equals(VoynichFactory.CONCORDANCE_TRANSCRIBER);
					}
				});

				System.out.println(concordance.getLines().get(0));

				// Other transcriptions of same line
				IvtffText intelinear = docIvtffText.filterLines(new ElementFilter<IvtffLine>() {

					@Override
					public boolean keep(IvtffLine element) {
						// TODO Auto-generated method stub
						return !element.getDescriptor().getTranscriber().equals(VoynichFactory.CONCORDANCE_TRANSCRIBER)
								&& !element.getDescriptor().getTranscriber()
										.equals(VoynichFactory.MAJORITY_TRANSCRIBER);
					}
				});

				// Align lines for comparison; first line is the concordance version
				List<IvtffLine> lines = new ArrayList<>();
				lines.addAll(intelinear.getLines());
				IvtffLine.align(lines); // notice we must align here, to make sure concordance does not influence alignment 
				lines.add(0, concordance.getLines().get(0));

				// do the check
				for (int i = 0; i < lines.get(0).getText().length(); ++i) {
					char c = lines.get(0).getText().charAt(i);
					if (c != ',')
						continue;
					for (int j = 1; j < lines.size(); ++j) {
						char c1 = lines.get(j).getText().charAt(i);
						if (!doc.getAlphabet().isWordSeparator(c1) && (c1 != '!')) {
							System.out.println();
							for (int k = 1; k < lines.size(); ++k) {
								System.out.println(lines.get(k));
							}
							System.out.println(lines.get(0));
							System.out.println(lines.get(0).getText().substring(0,i) + " [" + i + "]");
							throw new IllegalArgumentException();
						}
					}
				}
			}
			doc.filterLines(new LineFilter.Builder().transcriber(VoynichFactory.CONCORDANCE_TRANSCRIBER).build());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}
}
