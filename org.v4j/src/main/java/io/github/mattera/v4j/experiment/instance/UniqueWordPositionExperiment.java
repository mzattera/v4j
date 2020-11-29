/**
 * 
 */
package io.github.mattera.v4j.experiment.instance;

import io.github.mattera.v4j.experiment.Measurement;
import io.github.mattera.v4j.experiment.RandomizedTextExperiment;
import io.github.mattera.v4j.experiment.StatisticalTest;
import io.github.mattera.v4j.experiment.TextRandomizationProcess;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
import io.github.mattera.v4j.text.ivtff.PageFilter;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * @author Massimiliano_Zattera
 *
 */
public class UniqueWordPositionExperiment extends RandomizedTextExperiment<IvtffPage, long[][]> {
	private final static int REFERENCE_POPULATON_SIZE = 1000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Keep only BB1 paragraphs
			IvtffText voy = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			voy = voy.filterPages(new PageFilter.Builder().cluster("BB1").build());
			voy = voy.filterLines(new LineFilter.Builder().genericLocusType("P").build());

			Measurement<IvtffPage, long[][]> measurement = new MeasureUniqueWordPosition();
			StatisticalTest<long[][]> test = new Chi2GoodnessOfFitTest();
			TextRandomizationProcess<IvtffPage> randomizer = new WordsInPageRandomizer();

			UniqueWordPositionExperiment inst = new UniqueWordPositionExperiment(measurement, test, randomizer,
					REFERENCE_POPULATON_SIZE);
			inst.setPopulation(voy.getElements());

			printMeasure(inst.measure());
			System.out.println("-------------------------");
			printMeasure(inst.measureReferencePopulation());
			
//			System.out.println(inst.runExperiment());
			System.out.println(inst.pValue());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void printMeasure(long[][] ls) {
		for (int i = 0; i < ls.length; ++i) {
			for (int j = 0; j < ls[0].length; ++j) {
				System.out.print(ls[i][j] + "; ");
			}
			System.out.println();
		}
	}

	public UniqueWordPositionExperiment(Measurement<IvtffPage, long[][]> measurement, StatisticalTest<long[][]> test,
			TextRandomizationProcess<IvtffPage> randomizer, int refPopSize) {
		super(measurement, test, randomizer, refPopSize);
	}
}
