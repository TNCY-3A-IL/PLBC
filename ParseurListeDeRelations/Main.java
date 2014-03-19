import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {
	
	public void parserAttributs(String pathFile) {
		
		ArrayList<String> nomAtts = new ArrayList<String>();
		ArrayList<Integer> freqAtts = new ArrayList<Integer>();
		
		try {
			//LIRE LES NOMS D'ATTRIBUTS DU FICHIER SOURCE
			InputStream fileR =getClass().getClassLoader().getResourceAsStream(pathFile);
	    	BufferedReader buffR = new BufferedReader(new InputStreamReader(fileR));
			String ligne=buffR.readLine();
			while(ligne!=null){
				//GET LES NOMS D'ATTS DANS LA LIGNE
				
				/* CAS CTD
				String[] mots = ligne.split("\\t");
				for(int i=0; i<mots.length; i++){
					String regexp = "(.*)(\\^)(.*)";
					if(mots[i].matches(regexp)) {
						String[] values = mots[i].split("\\|");
						for(int j=0; j< values.length; j++){
							String value = values[j];
							System.out.println(value);
							if(!nomAtts.contains(value)) {
								nomAtts.add(value);
								freqAtts.add(1);
							}else{
								int index = nomAtts.indexOf(value);
								freqAtts.set(index, freqAtts.get(index)+1);
							}
						}
					}
				}
				*/
				// CAS DRUGBANK
				String regexp = "(.*)(<action>)(.*)(</action>)(.*)";
				if(ligne.matches(regexp)) {
					String value = ligne.replaceAll(regexp, "$3");
					System.out.println(value);
					if(!nomAtts.contains(value)) {
							nomAtts.add(value);
							freqAtts.add(1);
						}else{
							int index = nomAtts.indexOf(value);
							freqAtts.set(index, freqAtts.get(index)+1);
						}
				}
				//*/
				ligne = buffR.readLine();
			}
			
			//CREER LA STRING A ECRIRE
			String resultat = "";
			int index =0;
			for(String nomAtt : nomAtts){
				/*CAS DRUGBANK
				
				regexp drugbank 

				resultat+="dr:drug_umls_id db:"+nomAtt+"OfTarget ge:ncbiId."+"\r\n";
				resultat+="dr:drug_umls_id db:"+nomAtt+"OfTransporter ge:ncbiId."+"\r\n";
				resultat+="dr:drug_umls_id db:"+nomAtt+"OfCarrier ge:ncbiId."+"\r\n";
				resultat+="dr:drug_umls_id db:"+nomAtt+"OfEnzyme ge:ncbiId."+"\r\n";
				*/
				/*CAS CTD
				String regexp = "(.*)(\\^)(\\w)(.*)";
				String majuscule = nomAtt.replaceAll(regexp, "$3").toUpperCase();
				String affiche = nomAtt.replaceAll(regexp, "$1"+majuscule+"$4");
				
				boolean modif = true;
				String affiche2 = affiche;
				while(modif){
					modif = false;
					String oldAffiche = affiche2;
					String regexp2 = "(.*)(\\s)(\\w)(.*)";
					String majuscule2 = oldAffiche.replaceAll(regexp2, "$3").toUpperCase();
					affiche2 = oldAffiche.replaceAll(regexp2, "$1"+majuscule2+"$4");
					if(!oldAffiche.equals(affiche2)) modif=true;
				}
				resultat+="dr:drug_umls_id ctd:"+affiche2+" ge:ncbiId."+"\r\n";
				*/
				resultat+="freq de "+nomAtt+" : "+freqAtts.get(index)+"\r\n";
				index ++;
			}
			
			//LES ECRIRE DANS UN FICHIER EN ELIMINANT LES DOUBLONS
			FileWriter fileW=new FileWriter(new File("C:\\Users\\Zoltan\\Desktop\\listAttributs"));
			BufferedWriter buffW=new BufferedWriter(fileW);
			buffW.write(resultat);
			buffW.flush();
			buffW.close();
			fileW.close();
			buffR.close();
			fileR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.parserAttributs("CTD_chem_gene_ixns.tsv");//drugbank.xml
	}

}
