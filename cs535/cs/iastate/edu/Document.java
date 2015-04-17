package pa4.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Document {
	
	private HashMap<String,Integer> terms;
	private String filePath;

	private String fileName;
	private double weight;
	
	public Document(String filePath){
		this.terms = new HashMap<String,Integer>();
		this.filePath = filePath;
		preProcessing();

	}
	
	public Document(String fileName,double weight){
		this.fileName = fileName;
		this.weight = weight;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public HashMap<String,Integer> getTerms(){
		return this.terms;
	}
	
	public String getFileName(){
		
		String name;
		
		if(this.fileName!=null){
			name = this.fileName;
		}else if(this.filePath!=null){
			File f = new File(this.filePath);
			name = f.getName();
			f = null;
		}else{
			name = null;
		}
		
		return name;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	private void preProcessing(){
		FileInputStream fstream;
		BufferedReader br = null;
		try {
			fstream = new FileInputStream(this.filePath);
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = null;
			
			Pattern pattern = Pattern.compile("([A-Za-z]{3,})");
			Matcher matcher;
			String tempString;
			int tempCount;
			while ((strLine = br.readLine()) != null) {
				matcher = pattern.matcher(strLine);
				while(matcher.find()){
					tempString = matcher.group().toLowerCase();
					if(!tempString.matches("(?i)the")){
						if(this.terms.containsKey(tempString)){
							tempCount = this.terms.get(tempString);
							tempCount++;
							this.terms.put(tempString, tempCount);
						}else{
							this.terms.put(tempString, 1);
						}
					}
					
//					System.out.println(tempString);
				}
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static Comparator<Document> weightComparator 
    = new Comparator<Document>() {

		public int compare(Document d1, Document d2) {

			if (d1.getWeight() == d2.getWeight()){
				return 0;
			}
			else{
				return d1.getWeight() < d2.getWeight() ? 1 : -1;
			}
		}
	};
}
