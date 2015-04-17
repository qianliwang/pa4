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
	
	private int numOfDocs;
	
	public IndexBuilder(String folderPath){
		this.folderPath = folderPath;
		this.index = new HashMap<String,Integer>();
		posting = new HashMap<String,ArrayList<String>>();
		docSet = new TreeSet<Document>();
		numOfDocs = 0;
		buildIndex();
	}
	
	
	public HashMap<String, Integer> getIndex() {
		return index;
	}

	public HashMap<String, ArrayList<String>> getPosting() {
		return posting;
	}


	private void buildIndex(){
		File folder = new File(this.folderPath);	
		File[] listOfFiles = folder.listFiles();
		this.numOfDocs = listOfFiles.length;
		System.out.println(this.folderPath+" contains "+listOfFiles.length+" files.\n");
		Document d;
		int count;
		String tempString;
		ArrayList<String> tempStrList;
		for(int i=0;i<listOfFiles.length;i++){
//			System.out.println(listOfFiles[i].getAbsolutePath());
			d = new Document(listOfFiles[i].getAbsolutePath());	
//			this.docSet.add(d);
			for(String s:d.getTerms().keySet()){
				if(this.index.containsKey(s)){
					count = this.index.get(s);
					count++;
					this.index.put(s,count);
				}else{
					this.index.put(s,1);
				}
				tempString = d.getFileName()+","+d.getTerms().get(s);
				if(this.posting.containsKey(s)){
					this.posting.get(s).add(tempString);
				}else{
					tempStrList = new ArrayList<String>();
					tempStrList.add(tempString);
					this.posting.put(s, tempStrList);
				}
				
			}
		}
		
	}
	
	public double getWeight(String t,String d){
		ArrayList<String> termPosting = this.posting.get(t);
		String array[];
		int tf_td = 0;
		double weight;
		for(String s:termPosting){
			array = s.split(",");
			if(array[0].equals(d)){
				tf_td = Integer.parseInt(array[1]);
				break;
			}
		}
		
		int df_t = this.index.get(t);
		
		weight = Math.log(1.0+tf_td)*Math.log10(this.numOfDocs/df_t);
		
		return weight;
	}
	
	public ArrayList<Document> getWeights(String t){
		ArrayList<Document> tDocList = new ArrayList<Document>();
		
		ArrayList<String> termPosting = this.posting.get(t);
		int df_t = this.index.get(t);
		int tf_td = 0;
		
		String array[];
		double weight;
		
		Document d;
		
		for(String s:termPosting){
			array = s.split(",");
			tf_td = Integer.parseInt(array[1]);
			weight = Math.log(1.0+tf_td)*Math.log10(this.numOfDocs/df_t);
			d = new Document(array[0],weight);
			tDocList.add(d);
		}
		
		return tDocList;
	}
}
