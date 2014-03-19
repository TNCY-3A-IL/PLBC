import java.io.*;

public class Parser {

	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void parser(String fileName) throws IOException{
		File inputFile = new File(fileName); 
		File outputFile = new File("data/sider_parser.txt"); 

		FileReader in = new FileReader(inputFile); 
		FileWriter out = new FileWriter(outputFile); 

		BufferedReader reader = new BufferedReader(in);
		BufferedWriter writter = new BufferedWriter(out);
		
		String outLine = "PREFIX dr: http://telecomnancy.eu/drug/"+"\n"+
						"PREFIX di: http://telecomnancy.eu/disease/"+"\n"+
						"PREFIX se: http://telecomnancy.eu/sider2/"+"\n";
		String line = "";
		int i=0;
		DrugNameToCui drugNameCui = new DrugNameToCui();
		String tempName = "";
		String tempCui = "";
		while(line!=null){
			i++;
			System.out.println("i : "+i);
			line=reader.readLine();
			if(line!=null){
				String[] tabLine= new String[10];
				tabLine=line.split("\t");

				if(tabLine[5].equals("PT")){
					
					if(!tempName.equals(tabLine[3])){
						tempName = tabLine[3];
						tempCui = drugNameCui.SearchDrugId(tabLine[3]);
					}
						
					outLine = tempCui + "\t" +tabLine[3]+ "\t" + tabLine[6] + "\t\"" + tabLine[7] +"\"\n";
					writter.write(outLine);
				}
			}

		}
		reader.close();
		writter.close();
		in.close();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		parser("data/meddra_adverse_effects.tsv");

	}

}
