/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package omimparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
/**
 *
 * @author Fabien
 */
public class OMIMParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
        FileInputStream in = new FileInputStream("C:\\Users\\Fabien\\Downloads\\genemap");
        FileOutputStream out = new FileOutputStream("C:\\Users\\Fabien\\Downloads\\out.txt");
        
            toRDFFile(new BufferedReader(new InputStreamReader(in)), new OutputStreamWriter(out));
        }catch(IOException e){
            
        }
        
    }
    
    public static String getField(String line, int i){
        int j = 0, k = 0;
        while(j != i){
            if(line.charAt(k) == '|')
                j++;
            k++;
        }
        j = k;
        while(k < line.length() && line.charAt(k) != '|'){
            k++;
        }
        return line.substring(j, k);
    }
    
    public static void toRDFFile(BufferedReader in,OutputStreamWriter out) throws IOException{
        String line = in.readLine();
        Triplet t = new Triplet();
        out.write("PREFIX di: http://telecomnancy.eu/disease/\n");
        out.write("PREFIX ge: http://telecomnancy.eu/gene/\n");
        out.write("PREFIX omim: http://telecomnancy.eu/omim/\n");
        String[] data;
        String tmp;
        while(line != null){
            t.setObject("ge:" + getField(line,5).split(",")[0]);
            t.setProperty("omim:involvedInMechanismOf");
            tmp = getField(line,13) + getField(line,14) + getField(line,15);
            data = tmp.split(";");
            for (String data1 : data) {
                try {
                    data1 = data1.replaceAll(".*, ?([0-9]+) ?\\([0-9]+\\)","$1");
                    t.setSubject("di:" + Integer.parseInt(data1));
                    out.write(t + "\n");
                }catch(Exception e){
                    
                }
                
            }
            line = in.readLine();
        }
        out.close();
    }
    
}
