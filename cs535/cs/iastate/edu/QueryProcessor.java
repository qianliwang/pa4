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
		HashMap<String,Document> docMap = this.index.getDocumentMap();
		fillBiwordDocumentFilter(folderPath,filterMap,docMap);
	}
	
	private void fillBiwordDocumentFilter(String folderPath,HashMap<String,BiwordDocumentFilter> filterMap,HashMap<String,Document> docMap){
		File folder = new File(folderPath);	
		File[] listOfFiles = folder.listFiles();
		
		BiwordDocumentFilter filter;
		Document d;
		for(File f:listOfFiles){
			filter = new BiwordDocumentFilter(BITSPERELEMENT,f.getAbsolutePath());
			d = docMap.get(f.getName());
			filter.addDocument(d);
			filterMap.put(f.getName(), filter);
		}
	}
	
	public void query(String q, int k){
		
		String tArray[] = q.split(" ");
		ArrayList<String> tPosting;
		
		String postArray[];
		String docName;
		ArrayList<WeightedDocument> tDocList;
		HashMap<String,WeightedDocument> docMap = new HashMap<String,WeightedDocument>();
		
		double weight;
		double w;
		String tempName;
		double queryWeight[] = new double[tArray.length];
		int termFrequency;
		double queryLength = 0;
		for(int i=0;i<tArray.length;i++){
			termFrequency = this.index.getIndex().get(tArray[i]);
			queryWeight[i] = calcWeight(1,termFrequency,this.filterMap.keySet().size());
			queryLength = queryLength + queryWeight[i]*queryWeight[i];
		}
		
		queryLength = Math.sqrt(queryLength);
		
		for(int i=0;i<queryWeight.length;i++){
			queryWeight[i] = queryWeight[i]/queryLength;
		}
		
		for(int i=0;i<tArray.length;i++){
			tDocList = this.index.getWeights(tArray[i]);
			for(WeightedDocument d:tDocList){
				tempName = d.getName();
				weight = d.getWeight();
				weight = weight*queryWeight[i];
				if(docMap.containsKey(tempName)){
					w = docMap.get(tempName).getWeight();
					docMap.get(tempName).setWeight(w+weight);
				}else{
					docMap.put(tempName, new WeightedDocument(tempName,weight));
				}
			}
		}
		
		ArrayList<String> biwordList = new ArrayList<String>();
		for(int i=0;i<tArray.length-1;i++){
			biwordList.add(tArray[i]+" "+tArray[i+1]);
		}
		
		ArrayList<WeightedDocument> docList = new ArrayList<WeightedDocument>();
		
		WeightedDocument wd;
		BiwordDocumentFilter biDF;
		int tempHit;
		for(String s:docMap.keySet()){
			wd = docMap.get(s);
			biDF = this.filterMap.get(s);
			tempHit = getBiwordHit(biDF,biwordList);
			wd.setNumOfBiwordHit(tempHit);
			docList.add(wd);
		}
		
		Collections.sort(docList,WeightedDocument.weightComparator);
		
		ArrayList<WeightedDocument> biwordWeightedList = new ArrayList<WeightedDocument>();
		
		int limit = Math.min(docList.size(), 2*k);
		
		System.out.println("*************Sorted By Weight*************");
		
		for(int i=0;i<limit;i++){
			System.out.println(docList.get(i).getName()+"\t"+docList.get(i).getWeight());
			biwordWeightedList.add(docList.get(i));
		}
		
		Collections.sort(biwordWeightedList,WeightedDocument.biWordHitComparator);
		System.out.println("*************Sorted By Biword Hit then*************");
		for(int i=0;i<k;i++){
			System.out.println(biwordWeightedList.get(i).getName()+"\t"+biwordWeightedList.get(i).getWeight()+"\t" + biwordWeightedList.get(i).getNumOfBiwordHit());
		}
	}
	
	private int getBiwordHit(BiwordDocumentFilter filter,ArrayList<String> biwordList){
		int num = 0;
		for(String s:biwordList){
			if(filter.appears(s)){
				num++;
			}
		}
		return num;
	}
	private double calcWeight(int tf_td,int df_t, int numOfDocs){
		double weight = Math.log(1.0+tf_td)*Math.log10(numOfDocs/df_t);
		return weight;
	}
}
