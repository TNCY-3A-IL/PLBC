package plbc;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class SimpleLuceneSearch {
	private String INDEX;
	private IndexSearcher SEARCHER;
	private Analyzer ANALYSER;
	
  public SimpleLuceneSearch(String index) throws CorruptIndexException, IOException {
	    this.INDEX      =index;// "/home/coulet/workspace/data_resource/mesh/indexOnMesh";
	    IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX)), true); 
	    SEARCHER  = new IndexSearcher(reader);
	    ANALYSER  = new StandardAnalyzer(Version.LUCENE_35);
  }
    
  public String getCuid(String meshId, String preferred) throws IOException, ParseException{ //meshId:D001416 AND preferred:true
   
    String field      = "meshId";
    String userQuery  = "meshId:"+meshId+" AND preferred:"+preferred;
    
    // only searching, so read-only=true
    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
    
    //System.out.println("Searching for: " + query.toString(field)+" in meSH MRCONSO");
    int hitsPerPage = 10;// result is ordered with lucene scored then true
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
    SEARCHER.search(query, collector);
    int numTotalHits = collector.getTotalHits();
    
    //display results
    //System.out.println("Found " + numTotalHits + " hits.");
    if(numTotalHits==1){
    	ScoreDoc[] results = collector.topDocs().scoreDocs;
		int docId = results[0].doc;
	    Document d = SEARCHER.doc(docId);
	    return d.get("cuId");
    }else if(numTotalHits>1){
        System.out.println("WARNING: there is several corresping CUI");
    }else if(numTotalHits==0){
        System.out.println("WARNING: there is no corresping CUI");
    }
    
   
    //System.out.println((i + 1) + ". " + d.get("cuId")+ ", meshId= " + d.get("meshId")+ ", term= " + d.get("term")+ ", preferred= " + d.get("preferred"));
	return "";
  }
  
  /**
   * 
   * @param CUI
   * @return preferred
   * @throws IOException
   * @throws ParseException
   */
  public String getPreferredNameFromCui(String cui) throws IOException, ParseException{ //term: ??
	   
	    String field      = "cuId";
	    String userQuery  = "cuId:"+cui+" AND preferred:true";
	    
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	    //System.out.println("Searching for: " + query.toString(field)+" in meSH MRCONSO");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits==1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc;
		    Document d = SEARCHER.doc(docId);
		    //System.out.println("         =>"+d.get("term"));
		    return d.get("term");
	    }else if(numTotalHits>1){
	        ScoreDoc[] results = collector.topDocs().scoreDocs;
	    	for(int i=0;i<results.length;++i) {
	    	      int docId = results[i].doc;
	    	      Document d = SEARCHER.doc(docId);
	    	      if(d.get("cuId").toLowerCase().equals(cui.toLowerCase())){
	    	    	  return d.get("term");
	    	      }
	    	      System.out.println((i + 1) + ". " + d.get("cuId")+ ", term= " + d.get("term"));
	    	    }
	        System.out.println("WARNING: there is several corresping term and no one matches with the cui");
	    }else if(numTotalHits==0){
	        //let's get the first non preferred term
	        userQuery  = "cuId:"+cui;
	        Query    query2     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	        int hitsPerPage2 = 10;// result is ordered with lucene scored then true
		    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage2, true); 
		    SEARCHER.search(query2, collector2);
		    int numTotalHits2 = collector2.getTotalHits();
		    if(numTotalHits2>0){
		    	ScoreDoc[] results2 = collector2.topDocs().scoreDocs;
				int docId = results2[0].doc;
			    Document d = SEARCHER.doc(docId);
			    return d.get("term");
		    }else{
		        System.out.println("WARNING: there is no corresping term for "+cui);
		    }
	    }
	    return "";
	  }
  
  /**
   * 
   * @param meshId
   * @param preferred
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public String getCuidFromLabel(String term) throws IOException, ParseException{ //term: ??
	   
	    String field      = "term";
	    String userQuery  = "\""+term+"\"";
	    
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	   // System.out.println("Searching for: " + query.toString(field)+" in meSH MRCONSO");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits==1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc;
		    Document d = SEARCHER.doc(docId);
		    //System.out.println("         =>"+d.get("cuId"));
		    return d.get("cuId");
	    }else if(numTotalHits>1){
	        ScoreDoc[] results = collector.topDocs().scoreDocs;
	    	for(int i=0;i<results.length;++i) {
	    	      int docId = results[i].doc;
	    	      Document d = SEARCHER.doc(docId);
	    	      if(d.get("term").toLowerCase().equals(term.toLowerCase())){
	    	    	  return d.get("cuId");
	    	      }	    	      
	    	    }
	        System.out.println("WARNING: there is several corresping CUI and no one matches");
	    }else if(numTotalHits==0){
	        System.out.println("WARNING: there is no corresping CUI");
	    }
	    return "";
	  }
  
    public String getCuidFromMimId(String mimId) throws IOException, ParseException{ //term: ??
	   
	    String field      = "mimId";
	    String userQuery  = mimId;
	    
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	    System.out.println("Searching for: " + query.toString(field)+" in OMIM MRCONSO");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits==1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc;
		    Document d = SEARCHER.doc(docId);
		    System.out.println("         =>"+d.get("cuId"));
		    return d.get("cuId");
	    }else if(numTotalHits>1){
	        ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc; // WE TAKE THE FIRST ONE
		    Document d = SEARCHER.doc(docId);
		    return d.get("cuId");
	    	
	    }else if(numTotalHits==0){
	        System.out.println("WARNING: there is no corresping CUI");
	    }
	    return "";
	  }
  
  /**
   * 
   * @param symbol
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public String getPharmgkbIdForDrug(String drugName) throws IOException, ParseException{ //symbol:warfarin
	   
	    String field      = "name";
	    String userQuery  = drugName;
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	    System.out.println("Searching for: " + query.toString(field)+"in PharmGKB drugs ");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits==1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc;
		    Document d = SEARCHER.doc(docId);
		    return d.get("paId");
	    }else if(numTotalHits>1){
	        System.out.println("WARNING: there is several corresping paId");
	    }else if(numTotalHits==0){
	        System.out.println("WARNING: there is no corresping paId");
	        // let's try alternate gene symbols
	        String userQuery2=drugName;
	        Query    query2     = new QueryParser(Version.LUCENE_35, "genericNames", ANALYSER).parse(userQuery2);
		    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage, true); 
		    SEARCHER.search(query2, collector2);
		    int numTotalHits2 = collector2.getTotalHits();
		    if(numTotalHits2>1){
		    	ScoreDoc[] results2 = collector2.topDocs().scoreDocs;
				int docId = results2[0].doc; // WE TAKE THE FIRST ONE
			    Document d = SEARCHER.doc(docId);
			    return d.get("paId");
		    }
	    }
	    //System.out.println((i + 1) + ". " + d.get("cuId")+ ", meshId= " + d.get("meshId")+ ", term= " + d.get("term")+ ", preferred= " + d.get("preferred"));
		return "";
	  }
  
      /**
       * get the PA ID from the gene symbol by querying a lucen index make form the genes.tsv file
       * @param symbol
       * @return
       * @throws IOException
       * @throws ParseException
       */
      public String getPharmgkbIdForGene(String symbol) throws IOException, ParseException{ //symbol:CYP2C9
	   
	    String field      = "symbol";
	    String userQuery  = symbol;
	    
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	    System.out.println("Searching for: " + query.toString(field)+"in PharmGKB genes ");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits==1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
			int docId = results[0].doc;
		    Document d = SEARCHER.doc(docId);
		    return d.get("paId");
	    }else if(numTotalHits>1){
	        System.out.println("WARNING: there is several corresping paId");
	    }else if(numTotalHits==0){
	        System.out.println("WARNING: there is no corresping paId");
	        // let's try alternate gene symbols
	        String userQuery2=symbol;
	        Query    query2     = new QueryParser(Version.LUCENE_35, "alternateSymbols", ANALYSER).parse(userQuery2);
		    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage, true); 
		    SEARCHER.search(query2, collector2);
		    int numTotalHits2 = collector2.getTotalHits();
		    if(numTotalHits2>1){
		    	ScoreDoc[] results2 = collector2.topDocs().scoreDocs;
				int docId = results2[0].doc; // WE TAKE THE FIRST ONE
			    Document d = SEARCHER.doc(docId);
			    return d.get("paId");
		    }
	    }
	    //System.out.println((i + 1) + ". " + d.get("cuId")+ ", meshId= " + d.get("meshId")+ ", term= " + d.get("term")+ ", preferred= " + d.get("preferred"));
		return "";
	  }
      
      /**
       * 
       * @param diseaseLabel
       * @return
       * @throws IOException
       * @throws ParseException
       */
       public String getPharmgkbIdForDisease(String diseaseLabel) throws IOException, ParseException{ //symbol:warfarin

        
         String field      = "diseaseLabel";
         String userQuery  = diseaseLabel;
         // only searching, so read-only=true
         Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);

        
         System.out.println("Searching for: " + query.toString(field)+"in PharmGKB diseases ");
         int hitsPerPage = 10;// result is ordered with lucene scored then true
         TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
         SEARCHER.search(query, collector);
         int numTotalHits = collector.getTotalHits();

        
         //display results
         //System.out.println("Found " + numTotalHits + " hits.");
         if(numTotalHits==1){
         ScoreDoc[] results = collector.topDocs().scoreDocs;
     int docId = results[0].doc;
         Document d = SEARCHER.doc(docId);
         return d.get("paId");
         }else if(numTotalHits>1){
             System.out.println("WARNING: there is several corresping paId");
             // let's see if one of the results has a label exactly similar
             ScoreDoc[] results = collector.topDocs().scoreDocs;
             for(int i=0;i<results.length;++i) {
	           int docId = results[i].doc;
	           Document d = SEARCHER.doc(docId);
	           if(d.get("diseaseLabel").toLowerCase().equals(diseaseLabel.toLowerCase())){
	        	   return d.get("paId");
	           }
             }
         }else if(numTotalHits==0){
             System.out.println("WARNING: there is no corresping paId");
         }
         //System.out.println((i + 1) + ". " + d.get("cuId")+ ", meshId= " + d.get("meshId")+ ", term= " + d.get("term")+ ", preferred= " + d.get("preferred"));
     return "";
       }

    /**
     * does an AE form sider is frequent or note (ie is minFreq is >= 0.01)   
     * @param stitchId
     * @param aeCui
     * @return
     * @throws IOException 
     * @throws CorruptIndexException 
     * @throws ParseException 
     */
	public boolean isFrequentAe(String stitchId, String aeCui) throws CorruptIndexException, IOException, ParseException {

		boolean isFreq = false;
		   
	    String field      = "stitchId";
	    String userQuery  = "stitchId:"+stitchId+" AND aeCuiId:"+aeCui;
	    
	    // only searching, so read-only=true
	    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
	    
	    //System.out.println("Searching for: " + query.toString(field)+" in meSH MRCONSO");
	    int hitsPerPage = 10;// result is ordered with lucene scored then true
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
	    SEARCHER.search(query, collector);
	    int numTotalHits = collector.getTotalHits();
	    
	    //display results
	    //System.out.println("Found " + numTotalHits + " hits.");
	    if(numTotalHits>1){
	    	ScoreDoc[] results = collector.topDocs().scoreDocs;
            for(int i=0;i<results.length;++i) {
	           int docId = results[i].doc;
	           Document d = SEARCHER.doc(docId);
	           if(!d.get("placebo").equals("placebo") && Float.parseFloat(d.get("minFreq"))>=0.01){
	        	   return true;
	           }
            }
	    }
	    
	   
	    //System.out.println((i + 1) + ". " + d.get("cuId")+ ", meshId= " + d.get("meshId")+ ", term= " + d.get("term")+ ", preferred= " + d.get("preferred"));
		return isFreq;
	}

	/**
	   * 
	   * @param MeshId
	   * @return CUI
	   * @throws IOException
	   * @throws ParseException
	   */
	  public String getCuiFromMeshId(String meshId) throws IOException, ParseException{ //term: ??
		   
		    String field      = "meshId";
		    String userQuery  = "meshId:"+meshId;
		    
		    // only searching, so read-only=true
		    Query    query     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
		    
		    //System.out.println("Searching for: " + query.toString(field)+" in meSH MRCONSO");
		    int hitsPerPage = 10;// result is ordered with lucene scored then true
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true); 
		    SEARCHER.search(query, collector);
		    int numTotalHits = collector.getTotalHits();
		    
		    //display results
		    //System.out.println("Found " + numTotalHits + " hits.");
		    if(numTotalHits==1){
		    	ScoreDoc[] results = collector.topDocs().scoreDocs;
				int docId = results[0].doc;
			    Document d = SEARCHER.doc(docId);
			    //System.out.println("         =>"+d.get("term"));
			    return d.get("cuId");
		    }else if(numTotalHits>1){
		        ScoreDoc[] results = collector.topDocs().scoreDocs;
		    	for(int i=0;i<results.length;++i) {
		    	      int docId = results[i].doc;
		    	      Document d = SEARCHER.doc(docId);
		    	      if(d.get("meshId").toLowerCase().equals(meshId.toLowerCase())){
		    	    	  return d.get("cuId");
		    	      }
		    	    }
		        System.out.println("WARNING: there is several corresping term and no one matches with the cui");
		    }else if(numTotalHits==0){
		        //let's get the first non preferred term
		        userQuery  = "meshId:"+meshId;
		        Query    query2     = new QueryParser(Version.LUCENE_35, field, ANALYSER).parse(userQuery);
		        int hitsPerPage2 = 10;// result is ordered with lucene scored then true
			    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage2, true); 
			    SEARCHER.search(query2, collector2);
			    int numTotalHits2 = collector2.getTotalHits();
			    if(numTotalHits2>0){
			    	ScoreDoc[] results2 = collector2.topDocs().scoreDocs;
					int docId = results2[0].doc;
				    Document d = SEARCHER.doc(docId);
				    return d.get("cuId");
			    }else{
			        System.out.println("WARNING: there is no corresping term for "+meshId);
			    }
		    }
		    return "";
		  }
	
}
