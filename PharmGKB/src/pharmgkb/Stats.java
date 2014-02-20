package pharmgkb;

public class Stats {
	
	private int nbWarning;
	private int nbAnnotation;
	private int nbGenes;
	
	public Stats () {
		nbWarning = 0;
		nbAnnotation = 0;
		nbGenes = 0;
	}
	
	public void incWarning() {
		nbWarning ++;
	}
	
	public void incAnnot() {
		nbAnnotation ++;
	}
	
	public void incGene() {
		nbGenes ++;
	}
	
	public int getNbWarning() {
		return nbWarning;
	}
	
	public int getNbAnnotation() {
		return nbAnnotation;
	}
	
	public int getNbGenes() {
		return nbGenes;
	}
	
	public String toString () {
		return "Warnings :" + nbWarning + " ; Annotations : " + nbAnnotation + " ; Genes " + nbGenes; 
	}
}
