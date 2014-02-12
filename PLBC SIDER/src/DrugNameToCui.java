/**
 * @author: Adrien Coulet
 * @date: June, 26, 2012
 * Obtain the CUI of a drug from a drug name  
 */


import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;


/** 
 * This class instantiate the class Simple Lucene Search to do a search on a lucene index
 * It call the method getCuiFromDrugLabel that enables to obtain a UMLS id from a drug name.
 * */
public class DrugNameToCui {

	public DrugNameToCui() {}

	public String SearchDrugId(String name){
		String inputDrugLabel = name;

		SimpleLuceneSearch searchInMrConso;
		try {
			searchInMrConso = new SimpleLuceneSearch("data/indexOnMesh2012");

			String drugCui=searchInMrConso.getCuidFromLabel(inputDrugLabel);

			System.out.println("the drug named "+inputDrugLabel+" has for UMLS CUI : "+drugCui);
			
			return drugCui;

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}

}



