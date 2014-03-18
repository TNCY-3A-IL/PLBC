import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadCVS2   {
   
    BufferedWriter bw;
   
    public ReadCVS2() throws IOException{
       
        File file = new File("C:\\Users\\Kira\\Desktop\\projets\\PLBC\\Projet\\test1.ttl");

       
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
         
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        this.bw = new BufferedWriter(fw);
       
        this.bw.write("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .");
        this.bw.write("\n");
        this.bw.write("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .");
        this.bw.write("\n");
       
    }
   
    public void traitementFichier1() throws IOException{
       
        this.bw.write("@prefix dr: <http://telecomnancy.eu/drug/> .");
        this.bw.write("\n");
        this.bw.write("@prefix ge: <http://telecomnancy.eu/gene/> .");
        this.bw.write("\n");
        this.bw.write("@prefix ctd: <http://telecomnancy.eu/ctd/> .");
        this.bw.write("\n");
       
        String csvFile = "C:\\CTD_chem_gene_ixns.tsv";

        BufferedReader br = null;
        String line = "";
    
        try {
    
            br = new BufferedReader(new FileReader(csvFile));
            int counterLine = 0;
            while (counterLine < 28) {
    
                  counterLine++;
                  line = br.readLine();
                
            }
          
            while ((line = br.readLine()) != null) {
          
                String[] country = line.split("\\t");
                /*
                String regexp = "(.*)(\\^)(\\w)(.*)";
                String majuscule = country[9].replaceAll(regexp, "$3").toUpperCase();
                String affiche = country[9].replaceAll(regexp, "$1"+majuscule+"$4");

                               
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
                System.out.println(affiche2);
				*/
                String[] affiche3 = nerfGirlsPlz(country[9]);
                //System.out.println(affiche3);
                for(int i=0; i < affiche3.length; i++){
                
	                //this.bw.write("dr:" + country[1] + " ctd:" + affiche2 + " ge:" + country[3]+".");
	                this.bw.write("dr:" + country[1] + " ctd:" + affiche3[i] + " ge:" + country[4]+".");
	                this.bw.write("\n");
	                this.bw.write("dr:" + country[1] + " rdfs:hasLabel " + country[0]+".");
	                this.bw.write("\n");
	                this.bw.write("ge:" + country[4] + " rdfs:hasLabel " + country[3]+".");
	                this.bw.write("\n");
	                
	                //country[0] : ChemicalName
	                //country[1] : ChemicalId
	                //country[3] : GeneSymbol
	                //country[4] : GeneID
	                //country[9] : InteractionActions
                }
            }
           
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       
    }
   
    public void traitementFichier2() throws IOException{
       
        bw.write("@prefix dr: <http://telecomnancy.eu/drug/> .");
        bw.write("\n");
        bw.write("@prefix di: <http://telecomnancy.eu/disease/> .");
        bw.write("\n");
        bw.write("@prefix ctd: <http://telecomnancy.eu/ctd/> .");
        bw.write("\n");
        String csvFile = "C:\\CTD_chemicals_diseases.tsv";

        BufferedReader br = null;
        String line = "";
    
        try {
    
            br = new BufferedReader(new FileReader(csvFile));
            int counterLine = 0;
            while (counterLine < 28) {
    
                  counterLine++;
                  line = br.readLine();
                
            }
          
            while ((line = br.readLine()) != null) {
          
                String[] country = line.split("\\t");
                
                
                if(country[5].toLowerCase().contains("therapeutic")){
					
					
					if (country[4].contains("MESH")){
						SimpleLuceneSearch searchInMrConso;
						try {
							//System.out.println("mesh detected");
							searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnMesh2012");
							String meshId[] = country[4].split(":");
							String cui=searchInMrConso.getCuiFromMeshId(meshId[1]);
							country[4] = cui;
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
					
					if (country[4].contains("OMIM")){
						SimpleLuceneSearch searchInMrConso;
						try {
							//System.out.println("OMIM detected");
							
							searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnUmlsOmim");	
							String omimId[] = country[4].split(":");
							String cui=searchInMrConso.getCuidFromMimId(omimId[1]);
							country[4] = cui;
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
					//System.out.println(country[0] + " || " + country[1] + " || " + country[3] + " || " + country[4] );
					//counterLine++;
					this.bw.write("dr:" + country[1] + " ctd:hasIndication di:" + country[4]+".");
	                this.bw.write("\n");
	                this.bw.write("dr:" + country[1] + " rdfs:hasLabel " + country[0]+".");
	                this.bw.write("\n");
	                this.bw.write("di:" + country[4] + " rdfs:hasLabel " + country[3]+".");
	                this.bw.write("\n");
	                
	                //country[0] : ChemicalName
	                //country[1] : ChemicalId
	                //country[3] : DiseaseName
	                //country[4] : DiseaseId
	                //country[5] : DirectEvidence
	                
				}
                
            }
           
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       
    }

    public void traitementFichier3() throws IOException{
   
        bw.write("@prefix di: <http://telecomnancy.eu/disease/> .");
        bw.write("\n");
        bw.write("@prefix ge: <http://telecomnancy.eu/gene/> .");
        bw.write("\n");
        bw.write("@prefix ctd: <http://telecomnancy.eu/ctd/> .");
        bw.write("\n");
        String csvFile = "C:\\CTD_genes_diseases.tsv";

        BufferedReader br = null;
        String line = "";
    
        try {
    
            br = new BufferedReader(new FileReader(csvFile));
            int counterLine = 0;
            while (counterLine < 28) {
    
                  counterLine++;
                  line = br.readLine();
                
            }
          
            while ((line = br.readLine()) != null) {
            	String[] country = line.split("\\t");
            	
            	if(country[4].toLowerCase().contains("marker/mechanism")){
                
                if (country[3].contains("MESH")){
					SimpleLuceneSearch searchInMrConso;
					try {
						//System.out.println("mesh detected");
						searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnMesh2012");
						String meshId[] = country[3].split(":");
						String cui=searchInMrConso.getCuiFromMeshId(meshId[1]);
						country[3] = cui;
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
				if (country[3].contains("OMIM")){
					SimpleLuceneSearch searchInMrConso;
					try {
						//System.out.println("OMIM detected");
						
						searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnUmlsOmim");	
						String omimId[] = country[3].split(":");
						String cui=searchInMrConso.getCuidFromMimId(omimId[1]);
						country[3] = cui;
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
                
                this.bw.write("ge:" + country[1] + " ctd:involvedInMechanismOf di:" + country[3]+".");
                this.bw.write("\n");
                this.bw.write("di:" + country[3] + " rdfs:hasLabel " + country[2]+".");
                this.bw.write("\n");
                this.bw.write("ge:" + country[1] + " rdfs:hasLabel " + country[0]+".");
                this.bw.write("\n");
            	}
            }
           
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
   
    public void close() throws IOException{
        this.bw.close();
        System.out.println("Done");
    }
    
    public String girlsAreOp(String s){
        char[] charArray = s.toCharArray();
        boolean transformation = false;
        String rep = "";
        for (char c : charArray){
            if(transformation){
                rep = rep + Character.toUpperCase(c);
                transformation=false;
            }else{
                String troll = ""+c;
                if(troll.equals(" ") || troll.equals("|") || troll.equals("^")){
                    transformation=true;
                }else{
                    rep = rep + c;
                }
            }
        }
        return rep;
    }
    
    public static String[] nerfGirlsPlz(String s){

        
        char[] charArray = s.toCharArray();
        boolean transformation = false;
        String[] tab = new String[1+compteurChar(s,'|')];
        String temp = "";
        int compt = 0;

     
        for (char c : charArray){
            if(transformation){
                temp = temp + Character.toUpperCase(c);

                transformation=false;
            }else{
                String troll = ""+c;
                if(troll.equals("|")){
                    tab[compt] = temp;
                     temp = "";
                     compt++;
                }else{
                    if(troll.equals(" ") || troll.equals("^")){
                        transformation=true;
                    }else{
                        temp = temp + c;
                    }
                }
            }
        }
       
        tab[compt] = temp;
        return tab;
       
    }
   
    public static int compteurChar(String str, char ch){
          int compteur = 0;                 
          for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == ch){          
                   compteur++;
            }
        }
          return compteur;  
    }
   

    /**
     * @param args
     * @throws IOException
     */
  
    public static void main(String[] args) throws IOException {
   
        ReadCVS2 run = new ReadCVS2();
        run.traitementFichier1();
        run.close();
       
    }

}