package pa4.cs535.cs.iastate.edu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class QueryProcessor {

	private IndexBuilder index;
	private HashMap<String,BiwordDocumentFilter> filterMap;
	
	private final int BITSPERELEMENT = 8;
	
	public QueryProcessor(String folderPath){
		this.index = new IndexBuilder(folderPath);
		this.filterMap = new HashMap<String,BiwordDocumentFilter>();
		fillBiwordDocumentFilter(folderPath,filterMap);
	}
	
	private void fillBiwordDocumentFilter(String folderPath,HashMap<String,BiwordDocumentFilter> filterMap){
		File folder = new File(folderPath);	
		File[] listOfFiles = folder.listFiles();
		
		BiwordDocumentFilter filter;
		for(File f:listOfFiles){
			filter = new BiwordDocumentFilter(BITSPERELEMENT,f.getAbsolutePath());
			filterMap.put(f.getName(), filter);
		}
	}
	
	public void query(String q, int k){
		
		String tArray[] = q.split(" ");
		ArrayList<String> tPosting;
		
		String postArray[];
		String docName;
		ArrayList<Document> tDocList;
		HashMap<String,Document> docMap = new HashMap<String,Document>();
		
		double weight;
		double w;
		String tempName;
		for(String t:tArray){
			tDocList = this.index.getWeights(t);
			for(Document d:tDocList){
				tempName = d.getFileName();
				weight = d.getWeight();
				if(docMap.containsKey(tempName)){
					w = docMap.get(tempName).getWeight();
					docMap.get(tempName).setWeight(w+weight);
				}else{
					docMap.put(tempName, new Document(tempName,weight));
				}
			}
		}
		
		ArrayList<Document> docList = new ArrayList<Document>();
		
		for(String s:docMap.keySet()){
			docList.add(docMap.get(s));
		}
		
		Collections.sort(docList,Document.weightComparator);
		
		for(int i=0;i<k;i++){
			System.out.println(docList.get(i).getFileName()+"\t"+docList.get(i).getWeight());
		}
	}
}
