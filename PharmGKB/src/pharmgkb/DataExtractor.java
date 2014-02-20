package pharmgkb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import tncy.plbc.SimpleLuceneSearch;

/***
 * 
 * @author Nounou & Tartinette
 *
 */
public class DataExtractor {
	
	public List<Gene> genes;
	public Stats stat;
	
	public DataExtractor(String fileName, HashMap<String, String> dictE, HashMap<String, String> dictN){
		try {
			stat = new Stats();
			genes = new ArrayList<Gene> ();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] processedLine;
			String[] temp;
			Gene last;
			br.readLine();
			while ((line = br.readLine()) != null) {
				if (!line.contains("\tOther")) {
					processedLine= line.split("\t");
					stat.incAnnot();
					if (processedLine[2].contains("),")) {
						temp = processedLine[2].split(",");
						for (String var : temp){
							stat.incGene();
							processedLine[2]=var;
							last = new Gene(processedLine, stat);
							last.setGkbName(dictE.get(last.getId()));
							last.setDrugName(dictN.get(last.getId()));
							genes.add(last);
						}
					} else if (processedLine[2].length()>=1) {
						stat.incGene();
						last = new Gene(processedLine, stat);
						last.setGkbName(dictE.get(last.getId()));
						last.setDrugName(dictN.get(last.getId()));
						genes.add(last);
					}
				}
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString () {
		String res = "";
		for (Gene gene : genes){
			res += gene.toString()+ "\n";
		}
		return res;
	}
	
	public String getStats() {
		String ges = stat.getNbGenes() + "";
		return stat.toString();
	}
	
}
