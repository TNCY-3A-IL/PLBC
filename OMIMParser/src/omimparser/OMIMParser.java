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
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Fabien
 */
public class OMIMParser {

    private SimpleLuceneSearch lucene;
    private FileInputStream in;
    private FileOutputStream out;
    private int disease, diseaseCount;
    private FileInputStream mim2gene;

    public static final int GENEMAP1 = 1;
    public static final int GENEMAP2 = 2;

    public OMIMParser(String index, String in, String out, int type) throws Exception {
        lucene = new SimpleLuceneSearch(index);
        this.in = new FileInputStream(in);
        this.out = new FileOutputStream(out);
        //mim2gene = new FileInputStream("mim2gene.txt");
		//new BufferedReader(new InputStreamReader(mim2gene)),new OutputStreamWriter(out));
		
        switch (type) {
            case GENEMAP1:
                disease = 13;
                diseaseCount = 3;
                break;
            case GENEMAP2:
                disease = 11;
                diseaseCount = 1;
                break;

        }
    }

    /**
     * @param args the command lreadere arguments
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
        Triplet t = new Triplet();
        writer.write("@prefix <di: http://telecomnancy.eu/disease/> .\r\n");
        writer.write("@prefix <ge: http://telecomnancy.eu/gene/> .\r\n");
        writer.write("@prefix <omim: http://telecomnancy.eu/omim/> .\r\n");
        String[] data;
        String tmp;
        int err = 0, total = 0;
        while (line != null) {
            t.setObject("ge:" + getField(line, 5).split(",")[0]);
            t.setProperty("omim:involvedInMechanismOf");
            tmp = "";
            for (int i = 0; i < diseaseCount; i++) {
                tmp += getField(line, disease + i);
            }
            data = tmp.split(";");
            String id;
            for (String data1 : data) {
                try {
                    data1 = data1.replaceAll(".*, ?([0-9]+) ?\\([0-9]+\\)", "$1");
                    total ++;
                    id = lucene.getCuidFromMimId(data1);
                    if (id != null && !id.equals("")) {
                        t.setSubject("di:" + id);
                        writer.write(t + "\r\n");
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
	
	    public static void GeneSymbolToID(BufferedReader in, OutputStreamWriter out) throws IOException{
    	String line;
    	String tmpLine[];
		line = in.readLine();
		Triplet t = new Triplet();
		while(line != null){
	    	tmpLine=line.split("	");
	    	if (!tmpLine[3].equals("-")) {
	            t.setObject("ge:" + tmpLine[2]);
	            t.setProperty("ge:hasNcbiId");
	            t.setSubject("\"" + tmpLine[3]+"\"");
                out.write(t + "\n");
	    	}
	    	line = in.readLine();
	    }
 	
    }

}
