package pharmgkb.triplet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import pharmgkb.DataExtractor;
import pharmgkb.Gene;
import pharmgkb.NameFinder;
import tncy.plbc.SimpleLuceneSearch;

public class RDFGenerator {
	
	public List<Gene> genes;
	public Set<String> drugNames;
	public List<Triplet> triplets;
	
	int nbTripletDrugUMLSId = 0;
	int nbTripletInteraction = 0;
	int nbTripletId = 0;

	public RDFGenerator(List<Gene> genes) {
		super();
		this.genes = genes;
		this.drugNames = new HashSet<String>();
		
		this.triplets = new ArrayList<Triplet>();
		/*
		 * 		NameFinder nf = new NameFinder("genes.tsv");
		DataExtractor de = new DataExtractor ("clinical_ann_metadata.tsv", nf.getDictEntrez(), nf.getDictName());
		 */
	}
	
	
	
	public void run() {
		
		for (Gene gene : this.genes) {
			
			Triplet  triplet = null;
			
			
			
			
			for (Map.Entry<String, String> relatedDrug : gene.getRelatedDrugs().entrySet()) {
				String drugName = relatedDrug.getKey();
				
				if (!this.drugNames.contains(drugName)) {
					this.drugNames.add(drugName);
					
					triplet = new DrugIdToNameTriplet(relatedDrug.getValue(), drugName);
					this.triplets.add(triplet);
				}
				
				
				
				
				for (String clinicalAnnotationType : gene.getClinicalAnnotationTypes()) {
					triplet = new DrugGeneInteractionTripet(gene.getId(), InteractionTypeFactory.getInstance(clinicalAnnotationType), relatedDrug.getValue());
					this.triplets.add(triplet);
				}
			}


			
			
			
			
			// Derniere
			triplet = new GeneIdToNameTriplet(gene.getId(), gene.getGkbName());
			this.triplets.add(triplet);
		}
	}
	
	public Triplet genereTripletDrugUMLSId(String drugName){
		if(drugNames.contains(drugName)){
			return null;
		}
		drugNames.add(drugName);
		
		nbTripletDrugUMLSId++;
		
		return new Triplet("dr:"+this.drugNameToCui(drugName), "rdf:hasLabel", drugName);		
	}
	
	public Triplet genereTripletInteraction(String geneName, String drugName, String interaction){
		
		String drugUMLS = this.drugNameToCui(drugName);
		
		nbTripletInteraction++;
		
		return new Triplet("ge:"+geneName, "pgkb:"+interaction, "dr:" + drugUMLS);
	}
	
	public Triplet genererTripletId(String geneName, String geneId){
		nbTripletId++;
		return new Triplet("ge:"+geneId, "rdf:label",geneName);
	}
	
	public static void main(String[] arg){
		genereTurtule(null, null);
	}
	
	public static void genereTurtule(List<Triplet> list, String path){
		File file = new File("path");
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < 600 * 10; i++){
				bw.write("qsgzrehzhtsdhstjsftjsghhgdkdyfjhsfjyetdjsfg,jhgdk,gdyuhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+'\n');
			}
			
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String drugNameToCui(String name){
		
		SimpleLuceneSearch searchInMrConso;
		
		try {
			searchInMrConso = new SimpleLuceneSearch("/home/depot/3A/plbc/projet_2014/index/indexOnMesh2012");
			
			String drugCui=searchInMrConso.getCuidFromLabel(name);
			
			return drugCui;
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
