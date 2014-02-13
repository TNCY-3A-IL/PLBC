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
		
		PrintWriter out = new PrintWriter("nameDrug.rdf");
		//ecrit les prefix
		out.println("PREFIX dr: http://telecomnancy.eu/drug/");
		out.println("PREFIX ge: http://telecomnancy.eu/gene/");
		out.println("PREFIX db: http://telecomnancy.eu/db/");
		
		double nbNomNonTrouve = 0;
		double nbNom = 0;
		int nbFils = racine.getChildren().size();
		for(int i=0;i<nbFils;i++){
			Element drug = racine.getChildren().get(i);
			if(drug.getName().equals("drug")){
				int nbFilsDrug = drug.getChildren().size();
				for(int j=0;j<nbFilsDrug;j++){
					Element nameDrug = drug.getChildren().get(j);
					if(nameDrug.getName().equals("name")){
						nbNom++;
						String drugCui=searchInMrConso.getCuidFromLabel(nameDrug.getText());
						if(!drugCui.equals("")){
							out.println("dr:"+drugCui+" rdf:hasLabel \""+nameDrug.getText()+"\"");
						}else{
							nbNomNonTrouve++;
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

}
