package pharmgkb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NameFinder {
	
	public HashMap<String, String> dictEntrez;
	public HashMap<String, String> dictName;
	
	public NameFinder(String fileName){
		dictEntrez = new HashMap<String, String>();
		dictName = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] processedLine;
			br.readLine();
			while ((line = br.readLine()) != null) {
				processedLine= line.split("\t");
				dictEntrez.put(processedLine[0], processedLine[1]);	
				dictName.put(processedLine[0], processedLine[3]);	
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getDictEntrez() {
		return dictEntrez;
	}
	
	public HashMap<String, String> getDictName() {
		return dictName;
	}
}
