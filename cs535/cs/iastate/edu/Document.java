package pa4.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pa3.cs535.cs.iastate.edu.Vertex;


public class Document {
	
	private HashMap<String,Integer> terms;
	private String filePath;

	private TreeSet<String> biwordSet;
	
	public Document(String filePath){
		this.terms = new HashMap<String,Integer>();
		this.filePath = filePath;
		this.biwordSet = new TreeSet<String>();
		preProcessing(this.filePath,this.terms,this.biwordSet);
	}
	

	
	public String getFilePath() {
		return filePath;
	}
	
	public HashMap<String,Integer> getTerms(){
		return this.terms;
	}
	
	public String getFileName(){
		
		String name;
		File f = new File(this.filePath);
		name = f.getName();
		return name;
	}

	public TreeSet<String> getBiwordSet(){
		return this.biwordSet;
	}
	private void preProcessing(String folderPath,HashMap<String,Integer> termMap,TreeSet<String> biwordSet){
		FileInputStream fstream;
		BufferedReader br = null;
		try {
			fstream = new FileInputStream(folderPath);
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = null;
			
			Pattern pattern = Pattern.compile("([A-Za-z]{3,})");
			Matcher matcher;
			String tempString;
			int tempCount;

			String firstStr;
			String secondStr;
			Queue<String> q = new LinkedList();
			
			while ((strLine = br.readLine()) != null) {
				matcher = pattern.matcher(strLine);
				while(matcher.find()){
					tempString = matcher.group().toLowerCase();
					if(!tempString.matches("(?i)the")){
						
						if(termMap.containsKey(tempString)){
							tempCount = termMap.get(tempString);
							tempCount++;
							termMap.put(tempString, tempCount);
						}else{
							termMap.put(tempString, 1);
						}
						
						q.add(tempString);
						
						if(q.size()>1){
							firstStr = q.poll();
							secondStr = q.peek();
							biwordSet.add(firstStr+" "+secondStr);
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
