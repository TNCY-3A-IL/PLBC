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
	
	public static List<Gene> genes;
	public static Set<String> drugNames;
	public static List<Triplet> triplets;
	
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
					String cuiDrug = relatedDrug.getValue();
					if(cuiDrug != null && !cuiDrug.trim().equals("")){
						triplet = new DrugIdToNameTriplet(relatedDrug.getValue(), drugName);
						this.triplets.add(triplet);
					}
					
				}
				
				
				
				
				for (String clinicalAnnotationType : gene.getClinicalAnnotationTypes()) {
					String cuiDrug = relatedDrug.getValue();
					if(cuiDrug != null && !cuiDrug.trim().equals("")){
						triplet = new DrugGeneInteractionTripet(gene.getId(), InteractionTypeFactory.getInstance(clinicalAnnotationType), cuiDrug);
						this.triplets.add(triplet);
					}
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
		NameFinder nf = new NameFinder("genes.tsv");
		DataExtractor de = new DataExtractor ("clinical_ann_metadata.tsv", nf.getDictEntrez(), nf.getDictName());

		
		RDFGenerator refGen = new RDFGenerator(de.genes);
		refGen.run();
		genereTurtule(null);
	}
	
	public static void genereTurtule(String path){
		File file = new File("pharmgkb.ttl");
		try {
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("prefix dr: <http://telecomnancy.eu/drug/>" + '\n');
			bw.write("prefix ge: <http://telecomnancy.eu/gene/>" + '\n');
			bw.write("prefix pgkb: <http://telecomnancy.eu/pharmgkb/>" + '\n');
			bw.write("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + '\n');
			
			for(Triplet trip : triplets){
				bw.write(trip.toString() + '\n');
			}
			
		
			System.out.println("fini");
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
