package fetcher;


public class Main {
	public static void main(String[] args) {
		ParserSideEffects parser = null;
		String url = new String("http://sideeffects.embl.de/media/download/meddra_adverse_effects.tsv.gz");
		String name = new String("meddra_adverse_effects.tsv.gz");
		parser = new ParserSideEffects(url);
		parser.extract(name);
		parser.deleteFile(name);
	}
}