package pharmgkb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.ToStringUtils;

import tncy.plbc.SimpleLuceneSearch;


public class Gene {

	private String id;
	private String gkbName;
	private String drugName;
	private String levelOfEvidence;
	private List<String> clinicalAnnotationTypes;
	private HashMap<String, String> relatedDrugs;
	
	public Gene (String[] line){
		String[] temp;
		drugName = "'sup?";
		clinicalAnnotationTypes = new ArrayList<String>();
		relatedDrugs = new HashMap<String, String>();
		
		//--------- Extraction du gkbname et de l'id --------------
		this.id = line[2].substring(line[2].indexOf("(")+1, line[2].indexOf(")"));
		//TODO utiliser le gkdbname pour trouver l'id avec genes.zip
		gkbName = "nounou tu m'enquiquinnes";
		//--------- Extraction du reste --------------
		this.levelOfEvidence = line[3];
		temp = line[4].split(";");
		for (String var : temp) {
			clinicalAnnotationTypes.add(var);
		}
		
		temp = line[11].split(";");
		for (String var : temp) {
			relatedDrugs.put(var, findCui(var));
		}
	}
	
	private String printDrugs() {
		String res = " [";
		for (Map.Entry<String, String> var : relatedDrugs.entrySet()) {
			res += var.getKey() + "   " + var.getValue()+ "   ";
		}
		return res + "]";
	}
	
	public List<String> getClinicalAnnotationTypes() {
		return this.clinicalAnnotationTypes;
	}
	
	
	public Map<String, String> getRelatedDrugs() {
		return this.relatedDrugs;
	}
	
	
	public String getGkbName() {
		return this.gkbName;
	}
	
	public void setGkbName(String s){
		this.gkbName = s;
	}
	
	public void setDrugName(String s){
		this.drugName = s;
	}
	
	public String getId(){
		return id;
	}
	
	public String getDrugName(){
		return drugName;
	}
	
	public String toString() {
		return id +"   " + gkbName + "   "+ levelOfEvidence + "   "+ clinicalAnnotationTypes.toString() + "   "+ printDrugs() ;
 	}
	
	public String findCui(String inputDrugLabel){
		String drugCui="error";
		SimpleLuceneSearch searchInMrConso;
		try {
			searchInMrConso = new SimpleLuceneSearch("index/indexOnMesh2012");
			drugCui=searchInMrConso.getCuidFromLabel(inputDrugLabel);
			return (drugCui);
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return drugCui;
	}
}
