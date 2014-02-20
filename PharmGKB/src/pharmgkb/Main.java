package pharmgkb;

public class Main {

	public static void main (String[] args) {
		NameFinder nf = new NameFinder("genes.tsv");
		DataExtractor de = new DataExtractor ("clinical_ann_metadata.tsv", nf.getDictEntrez(), nf.getDictName());
		System.out.println(de);
		System.out.println(de.getStats());
	}
}
