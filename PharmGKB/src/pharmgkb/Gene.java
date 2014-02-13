package pharmgkb;

import java.util.ArrayList;
import java.util.List;


public class Gene {

	private String id;
	private String gkbName;
	private String drugName;
	private String cui;
	private String levelOfEvidence;
	private List<String> clinicalAnnotationTypes;
	private List<String> relatedDrugs;
	
	public Gene (String[] line){
		String[] temp;
		drugName = "'sup?";
		cui = "smth";
		clinicalAnnotationTypes = new ArrayList<String>();
		relatedDrugs = new ArrayList<String>();
		
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
			relatedDrugs.add(var);
		}
	}
	
	public void setGkbName(String s){
		this.gkbName = s;
	}
	
	public void setDrugName(String s){
		this.drugName = s;
	}
	
	public void setCui(String s){
		this.cui = s;
	}
	
	public String getId(){
		return id;
	}
	
	public String getDrugName(){
		return drugName;
	}
	
	@Override
	public String toString() {
		return id +"   " + gkbName + "   "+ levelOfEvidence + "   "+ clinicalAnnotationTypes.toString() + "   "+ relatedDrugs;
	}
}
