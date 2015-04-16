package pa4.cs535.cs.iastate.edu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class IndexBuilder {

	private String folderPath;
	private Set<Document> docSet;
	
	private HashMap<String,Integer> index;
	private HashMap<String,ArrayList<String>> posting;
	
	public IndexBuilder(String folderPath){
		this.folderPath = folderPath;
		this.index = new HashMap<String,Integer>();
		posting = new HashMap<String,ArrayList<String>>();
		docSet = new TreeSet<Document>();
	}
	
	public void buildIndex(){
		File folder = new File(this.folderPath);	
		File[] listOfFiles = folder.listFiles();
		System.out.println(this.folderPath+" contains "+listOfFiles.length+" files.\n");
		Document d;
		int count;
		String tempString;
		for(int i=0;i<listOfFiles.length;i++){
//			System.out.println(listOfFiles[i].getAbsolutePath());
			d = new Document(listOfFiles[i].getAbsolutePath());	
			this.docSet.add(d);
			for(String s:d.getTerms().keySet()){
				if(this.index.containsKey(s)){
					count = this.index.get(s);
					count++;
					this.index.put(s,count);
				}else{
					this.index.put(s,1);
				}
				tempString = d.getFileName()+","+d.getTerms().get(s);
				this.posting.get(s).add(tempString);
			}
		}
		
	}
	
	public float weight(String t,String d){
		
	}
}
