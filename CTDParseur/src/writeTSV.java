import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class writeTSV {





   
    BufferedWriter bw;
   
    public writeTSV() throws IOException{
       
        File file = new File("C:\\Users\\Kira\\Desktop\\projets\\PLBC\\Projet\\test.ttl");

       
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
                String[] country2 = country[9].split("\\|");
                for(int i = 0; i< country2.length; i++){
                	System.out.println(country2[i]);
                }
                
                this.bw.write("dr:" + country[1] + " ctd:" + country[9] + " ge:" + country[4]+".");
                this.bw.write("\n");
                this.bw.write("dr:" + country[1] + " rdfs:hasLabel " + country[0]+".");
                this.bw.write("\n");
                this.bw.write("ge:" + country[4] + " rdfs:hasLabel " + country[3]+".");
                this.bw.write("\n");


          
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
       
    }

    public void traitementFichier3() throws IOException{
   
        bw.write("@prefix di: <http://telecomnancy.eu/disease/> .");
        bw.write("\n");
        bw.write("@prefix ge: <http://telecomnancy.eu/gene/> .");
        bw.write("\n");
        bw.write("@prefix ctd: <http://telecomnancy.eu/ctd/> .");
        bw.write("\n");
   
    }
   
    public void close() throws IOException{
        this.bw.close();
        System.out.println("Done");
    }
   

    /**
     * @param args
     * @throws IOException
     */
  
    public static void main(String[] args) throws IOException {
   
    	writeTSV run = new writeTSV();
        run.traitementFichier1();
        run.close();
       
    }

}