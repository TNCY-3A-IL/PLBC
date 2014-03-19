
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;


public class ReadCVS   {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		 
		ReadCVS obj = new ReadCVS();
		obj.run2();
	 
	  }
	 
	  public void run() {
	 
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
 
			
			System.out.println(country[0] + " || " + country[1] + " || " + country[3] + " || " + country[4] + " || " + country[9] );
			
			}
			
			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	  }
	  
	  public void run2() {
			 
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
				int counterAll = 0;
				while ((line = br.readLine()) != null) {
					
					String[] country = line.split("\\t");
					
					if(country[5].toLowerCase().contains("therapeutic")){
						
						counterAll++;
						
						if (country[4].contains("MESH")){
							SimpleLuceneSearch searchInMrConso;
							try {
								//System.out.println("mesh detected");
								searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnMesh2012");
								String meshId[] = country[4].split(":");
								String cui=searchInMrConso.getCuiFromMeshId(meshId[1]);
								//System.out.println("the MeSH id "+country[4]+" has for UMLS CUI : "+cui);
								//System.out.println(country[0] + " || " + country[1] + " || " + country[3] + " || cui: " + cui );
								
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
						
						else if (country[4].contains("OMIM")){
							SimpleLuceneSearch searchInMrConso;
							try {
								System.out.println("OMIM detected");
								
								searchInMrConso = new SimpleLuceneSearch("C:\\workspace\\plbcProject\\indexOnUmlsOmim");	
								String omimId[] = country[4].split(":");
								String cui=searchInMrConso.getCuidFromMimId(omimId[1]);
								System.out.println("the OMIM id "+country[4]+" has for UMLS CUI : "+cui);
								System.out.println(country[0] + " || " + country[1] + " || " + country[3] + " || cui: " + cui );
								
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
						
						//else System.out.println(country[0] + " || " + country[1] + " || " + country[3] + " || " + country[4] );
						
						//counterLine++;
					}
				}
				//System.out.println("counterLine: " + counterLine);
				
		 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		 
			System.out.println("Done");
		  }
	  
	  public void run3() {
			 
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
		 
					//System.out.println(country[0] + " || " + country[1] + " || " + country[2] + " || " + country[3]);
						counterLine++;
					}
					
				}
				System.out.println("counterLine: " + counterLine);
				
		 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		 
			System.out.println("Done");
		  }
	

}
