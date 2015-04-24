package pa4.cs535.cs.iastate.edu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class IndexBuilder {

	private String folderPath;
	private ArrayList<Document> docList;
	
	private HashMap<String,Integer> index;
	private HashMap<String,ArrayList<String>> posting;
	
	private HashMap<String,Double> docLengthMap;
	
	private int numOfDocs;
	
	public IndexBuilder(String folderPath){
		this.folderPath = folderPath;
		this.index = new HashMap<String,Integer>();
		this.posting = new HashMap<String,ArrayList<String>>();
		this.docList = new ArrayList<Document>();
		this.docLengthMap = new HashMap<String,Double>();
		
		this.numOfDocs = 0;
		buildIndex(this.folderPath,this.index,this.posting,this.docList);
		findDocumentLength(this.index,this.docList,this.docLengthMap);
	}
	
	
	public HashMap<String, Integer> getIndex() {
		return index;
	}

	public HashMap<String, ArrayList<String>> getPosting() {
		return posting;
	}

	public HashMap<String,Document> getDocumentMap(){
		HashMap<String,Document> docMap = new HashMap<String,Document>();
		for(Document d: this.docList){
			docMap.put(d.getFileName(), d);
		}
		return docMap;
	}
	
	private void buildIndex(String folderPath,HashMap<String,Integer> index,HashMap<String,ArrayList<String>> posting,ArrayList<Document> docList){
		File folder = new File(folderPath);	
		File[] listOfFiles = folder.listFiles();
		this.numOfDocs = listOfFiles.length;
		
		System.out.println(folderPath+" contains "+listOfFiles.length+" files.\n");
		Document d;
		int count;
		String tempString;
		ArrayList<String> tempStrList;
		for(int i=0;i<listOfFiles.length;i++){
//			System.out.println(listOfFiles[i].getAbsolutePath());
			d = new Document(listOfFiles[i].getAbsolutePath());	
			docList.add(d);
			for(String s:d.getTerms().keySet()){
				if(index.containsKey(s)){
					count = index.get(s);
					count++;
					index.put(s,count);
				}else{
					index.put(s,1);
				}
				tempString = d.getFileName()+","+d.getTerms().get(s);
				if(posting.containsKey(s)){
					posting.get(s).add(tempString);
				}else{
					tempStrList = new ArrayList<String>();
					tempStrList.add(tempString);
					posting.put(s, tempStrList);
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
		
		weight = calcWeight(tf_td,df_t,this.numOfDocs);
		
		return weight;
	}
	
	private double calcWeight(int tf_td,int df_t, int numOfDocs){
		double weight = Math.log(1.0+tf_td)*Math.log10(numOfDocs/df_t);
		return weight;
	}
	
	public ArrayList<WeightedDocument> getWeights(String t){
		ArrayList<WeightedDocument> tDocList = new ArrayList<WeightedDocument>();
		
		ArrayList<String> termPosting = this.posting.get(t);
		int df_t = this.index.get(t);
		int tf_td = 0;
		
		String array[];
		double weight;
		
		WeightedDocument d;
		
		for(String s:termPosting){
			array = s.split(",");
			tf_td = Integer.parseInt(array[1]);
			weight = calcWeight(tf_td,df_t,this.numOfDocs)/this.docLengthMap.get(array[0]);
			d = new WeightedDocument(array[0],weight);
			tDocList.add(d);
		}
		
		return tDocList;
	}
	
	private void findDocumentLength(HashMap<String,Integer> index,ArrayList<Document> docList,HashMap<String,Double> docLengthMap){
		
		String tempDocName;
		double termWeight;
		double squareSum = 0;
		double docLength;
		
		for(Document d:docList){
			squareSum = 0;
			tempDocName = d.getFileName();
			for(String term:d.getTerms().keySet()){
				termWeight = calcWeight(d.getTerms().get(term),index.get(term),this.numOfDocs);
				squareSum = squareSum+termWeight*termWeight;
			}
			docLength = Math.sqrt(squareSum);
//			System.out.println(d.getFileName()+" length : "+squareSum);
			docLengthMap.put(tempDocName, docLength);
		}
	}
	
	public void PrintIndexPostings(){
		for(String s: this.index.keySet()){
			System.out.println(s+"\t"+this.index.get(s)+this.posting.get(s)+"\t");
		}
	}
}
