package tncy.plbc;

/**
 * @author: Adrien Coulet
 * @date: June, 26, 2012
 * Obtain the CUI of a drug from a drug name  
 */


import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import tncy.plbc.SimpleLuceneSearch;;

/** 
 * This class instantiate the class Simple Lucene Search to do a search on a lucene index
 * It call the method getCuidFromMimId that enables to obtain UMLS id form a OMIM id
 * */
public class OmimIdToCui {
  
  private OmimIdToCui() {}
 
  public static void main(String[] args) {
	 
	  		String inputOmimId = "607554";
			
			SimpleLuceneSearch searchInMrConso;
			try {
				searchInMrConso = new SimpleLuceneSearch("/home/depot/3A/plbc/projet_2014/index/indexOnUmlsOmim");
				
				String cui=searchInMrConso.getCuidFromMimId(inputOmimId);
				
				System.out.println("the OMIM id "+inputOmimId+" has for UMLS CUI : "+cui);
				
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
  }
  
}

