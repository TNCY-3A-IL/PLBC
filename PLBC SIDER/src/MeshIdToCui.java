
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
 * It call the method getCuiFromMeshId that enables to obtain UMLS id form a MeSH id
 * */
public class MeshIdToCui {
  
  private MeshIdToCui() {}
 
  public static void main(String[] args) {
	 
	  		String inputMeshId = "D003924";
			
			SimpleLuceneSearch searchInMrConso;
			try {
				searchInMrConso = new SimpleLuceneSearch("/Users/coulet/workspace/data_resource/mesh/indexOnMesh2012");
				
				String cui=searchInMrConso.getCuiFromMeshId(inputMeshId);
				
				System.out.println("the MeSH id "+inputMeshId+" has for UMLS CUI : "+cui);
				
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
  }
  
}

