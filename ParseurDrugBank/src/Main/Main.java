package Main;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import Serveur.SingletonServeur;
import plbc.SimpleLuceneSearch;

public class Main {

	static Document document;
	static Element racine;

	public static void main(String[] args) throws IOException, ParseException
	{

		SAXBuilder sxb = new SAXBuilder();
		try
		{
			document = sxb.build(new File("drugbank.xml"));
		}
		catch(Exception e){}

		racine = document.getRootElement();
		System.out.println(racine.getName());
		System.out.println(racine.getChildren().size());
		System.out.println(racine.getChildren().get(0).getName());
		genereDrugList();
		System.out.println("------ FIN -------");
	}
	
	static void genereDrugList() throws IOException, ParseException{
		
		//appel objet pour recuperer l'id d'un medicament
		SimpleLuceneSearch searchInMrConso = null;
		try {
			searchInMrConso = new SimpleLuceneSearch("indexOnMesh2012");
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = new PrintWriter("nameDrug.ttl");
		//ecrit les prefix
		out.println("@prefix rdfs: </http://www.w3.org/2000/01/rdf-schema#> .");
		out.println("@prefix dr: <http://telecomnancy.eu/drug/> .");
		out.println("@prefix ge: <http://telecomnancy.eu/gene/> .");
		out.println("@prefix db: <http://telecomnancy.eu/db/> .");
		
		double nbNomNonTrouve = 0;
		double nbNom = 0;
		int nbFils = racine.getChildren().size();
		for(int i=0;i<nbFils;i++){
			Element drug = racine.getChildren().get(i);
			if(drug.getName().equals("drug")){
				int nbFilsDrug = drug.getChildren().size();
				for(int j=0;j<nbFilsDrug;j++){
					Element nameDrug = drug.getChildren().get(j);
					//id global du medicament etudiee
					String idGlobalMedicament ="";
					//balise name
					if(nameDrug.getName().equals("name")){
						nbNom++;
						String drugCui=searchInMrConso.getCuidFromLabel(nameDrug.getText());
						if(!drugCui.equals("")){
							idGlobalMedicament =  drugCui;
							out.println("dr:"+drugCui+" rdfs:hasLabel \""+nameDrug.getText()+"\" .");
						}else{
							nbNomNonTrouve++;
						}
					}
					//balise target
					if(nameDrug.getName().equals("targets")){
						List<Element> targetsDrug = nameDrug.getChildren();//dans la balise targets
						for(Element targetDrug : targetsDrug){
							//actions
							Element actionsDrug = targetDrug.getChildren().get(0);
							//action
							List<Element> listActionDrug = actionsDrug.getChildren();
							for(Element actionDrug : listActionDrug){
								String various = actionDrug.getText();
								String idPartner = actionDrug.getAttributeValue("partner");
								//print dans le fichier
								out.println("dr:"+idGlobalMedicament+" db:"+various+"OfTarget ge:"+findGeneID(idPartner)+".");
							}
						}
					}
					//balise transporteur
					if(nameDrug.getName().equals("transporters")){
						List<Element> targetsDrug = nameDrug.getChildren();//dans la balise targets
						for(Element targetDrug : targetsDrug){
							//actions
							Element actionsDrug = targetDrug.getChildren().get(0);
							//action
							List<Element> listActionDrug = actionsDrug.getChildren();
							for(Element actionDrug : listActionDrug){
								String various = actionDrug.getText();
								String idPartner = actionDrug.getAttributeValue("partner");
								//print dans le fichier
								out.println("dr:"+idGlobalMedicament+" db:"+various+"OfTransporter ge:"+findGeneID(idPartner)+".");
							}
						}
					}
					//balise enzyme
					if(nameDrug.getName().equals("enzymes")){
						List<Element> targetsDrug = nameDrug.getChildren();//dans la balise targets
						for(Element targetDrug : targetsDrug){
							//actions
							Element actionsDrug = targetDrug.getChildren().get(0);
							//action
							List<Element> listActionDrug = actionsDrug.getChildren();
							for(Element actionDrug : listActionDrug){
								String various = actionDrug.getText();
								String idPartner = actionDrug.getAttributeValue("partner");
								String idGene = findGeneID(idPartner);
								//print dans le fichier
								out.println("dr:"+idGlobalMedicament+" db:"+various+"OfEnzyme ge:"+ idGene +".");
							}
						}
					}
				}
			}
		}
		out.close();
		System.out.println("nbNom : "+nbNom+", nbNomNonTrouve : "+nbNomNonTrouve);
		double proba =(nbNom-nbNomNonTrouve)/nbNom*100;
		System.out.println("Probabilite de nom trouve = " +proba);
	}
	
	static String findGeneID(String idPartner){
		int nbFils = racine.getChildren().size();
		String geneIdName = "";
		for(int i=0;i<nbFils;i++){
			Element partner = racine.getChildren().get(i);
			if(partner.getName().equals("partner")){
				if(partner.getAttribute("id").equals(idPartner)){
					List<Element> geneNames = partner.getChildren();
					for(Element geneName : geneNames){
						if(geneName.getName().equals("gene-name")){
							geneIdName =geneName.getText();
						}
					}
				}
			}
		}
		
		return SingletonServeur.getInstance().makeRequest(geneIdName);
	}

}
