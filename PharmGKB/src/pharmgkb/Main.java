package pharmgkb;

public class Main {

	public static void main (String[] args) {
		System.out.println("salut nounou");
		NameFinder nf = new NameFinder("genes.tsv");
		DataExtractor de = new DataExtractor ("clinical_ann_metadata.tsv", nf.getDictEntrez(), nf.getDictName());
		System.out.println(de);
	}
}
