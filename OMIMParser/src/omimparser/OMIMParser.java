/*
 * To change this license header, choose License Headers reader Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template reader the editor.
 */
package omimparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ConcurrentSkipListSet;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Fabien
 */
public class OMIMParser {

    private SimpleLuceneSearch lucene;
    private FileInputStream in;
    private FileOutputStream out;
    private int diseaseColNbr, diseaseCount;
    private BufferedReader mim2gene;
    private ConcurrentSkipListSet<String> genes, diseases;

    public static final int GENEMAP1 = 1;
    public static final int GENEMAP2 = 2;

    public OMIMParser(String index, String in, String out, int type) throws Exception {
        lucene = new SimpleLuceneSearch(index);
        this.in = new FileInputStream(in);
        this.out = new FileOutputStream(out);
        genes = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
        diseases = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
        mim2gene = new BufferedReader(new InputStreamReader(new FileInputStream("mim2gene.txt")));
		
        switch (type) {
            case GENEMAP1:
                diseaseColNbr = 13;
                diseaseCount = 3;
                break;
            case GENEMAP2:
                diseaseColNbr = 11;
                diseaseCount = 1;
                break;

        }
    }

    /**
     * @param args diseaseColNbrPropertyhe command lreadere argumendiseaseColNbrPropertys
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            OMIMParser parser;
            parser = new OMIMParser(args[0], args[1], args[2], Integer.parseInt(args[3]));
            parser.toRDFFile();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public String getField(String line, int i) {
        int j = 0, k = 0;
        while (j != i) {
            if (line.charAt(k) == '|') {
                j++;
            }
            k++;
        }
        j = k;
        while (k < line.length() && line.charAt(k) != '|') {
            k++;
        }
        return line.substring(j, k);
    }

    public void toRDFFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
        OutputStreamWriter writer = new OutputStreamWriter(this.out);
        String line = reader.readLine();
        Triplet diseaseProperty = new Triplet();
        Triplet hasLabel = new Triplet();
        writer.write("@prefix <di: http://telecomnancy.eu/disease/> .\r\n");
        writer.write("@prefix <ge: http://telecomnancy.eu/gene/> .\r\n");
        writer.write("@prefix <omim: http://telecomnancy.eu/omim/> .\r\n");
        writer.write("@prefix <rdfs: http://www.w3c.org/2000/01rdf-schema#> .\r\n");
        String[] data;
        String tmp;
        int err = 0, total = 0;
        hasLabel.setProperty("rdfs:hasLabel");
        while (line != null) {
            tmp = getField(line, 5).split(",")[0];
            hasLabel.setSubject(tmp);
            tmp = GeneSymbolToID(tmp);
            hasLabel.setObject("ge:" + tmp);
            diseaseProperty.setObject("ge:" + tmp);
            diseaseProperty.setProperty("omim:involvedInMechanismOf");
            if(genes.add(tmp)){
                writer.write(hasLabel + "\r\n");
            }
            tmp = "";
            for (int i = 0; i < diseaseCount; i++) {
                tmp += getField(line, diseaseColNbr + i);
            }
            data = tmp.split(";");
            String id;
            for (String data1 : data) {
                try {
                    data1 = data1.replaceAll(".*, ?([0-9]+) ?\\([0-9]+\\)", "$1");
                    total ++;
                    id = lucene.getCuidFromMimId(data1);
                    if (id != null && !id.equals("")) {
                        diseaseProperty.setSubject("di:" + id);
                        writer.write(diseaseProperty + "\r\n");
                        if(diseases.add(id)){
                            hasLabel.setObject("di:" + id);
                            hasLabel.setSubject(data1);
                            writer.write(hasLabel + "\r\n");
                        }
                    }else{
                        err ++;
                    }
                } catch (ParseException e) {
                    err ++;
                }
            }
            line = reader.readLine();
        }
        writer.close();
        System.out.println(err + " error(s) over " + total);
    }
	
	    public String GeneSymbolToID(String geneSymbol) throws IOException{
    	String line;
    	String tmpLine[];
		line = mim2gene.readLine();
		while(line != null){
	    	tmpLine=line.split("	");
	    	if (!tmpLine[3].equals("-")) {/*
	            t.setObject("ge:" + tmpLine[2]);
	            t.setProperty("ge:hasNcbiId");
	            t.setSubject("\"" + tmpLine[3]+"\"");*/
	    	}
	    	line = mim2gene.readLine();
	    }
 	return null;
    }

}
