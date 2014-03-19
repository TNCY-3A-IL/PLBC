package pharmgkb;

import java.util.List;

import pharmgkb.triplet.RDFGenerator;
import pharmgkb.triplet.Triplet;

public class Main {

	public static void main (String[] args) {
		NameFinder nf = new NameFinder("genes.tsv");
		DataExtractor de = new DataExtractor ("clinical_ann_metadata.tsv", nf.getDictEntrez(), nf.getDictName());

		
		RDFGenerator refGen = new RDFGenerator(de.genes);
		refGen.run();

	}
}
