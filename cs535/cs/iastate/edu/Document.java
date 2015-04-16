package pa4.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {
	
	private HashMap<String,Integer> terms;
	private String filePath;
	private ArrayList<Float> weightVector;
	
	public Document(String filePath){
		this.terms = new HashMap<String,Integer>();
		this.filePath = filePath;
		preProcessing();
		this.weightVector = new ArrayList<Float>();
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public HashMap<String,Integer> getTerms(){
		return this.terms;
	}
	
	public String getFileName(){
		File f = new File(this.filePath);
		String fName = f.getName();
		f = null;
		return fName;
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
}
