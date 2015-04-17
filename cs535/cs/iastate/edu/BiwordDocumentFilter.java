package pa4.cs535.cs.iastate.edu;

import java.util.TreeSet;

public class BiwordDocumentFilter {
	private String pathName;
	private String fileName;
	
	private String filePath;
	
	private BloomFilterDet bfd;
	
	private int bitsPerElement;
	
	
	public BiwordDocumentFilter(int bitsPerElement,String fileName,String pathName){
		this.bitsPerElement = bitsPerElement;
		this.fileName = fileName;
		this.pathName = pathName;
		
	}
	
	public BiwordDocumentFilter(int bitsPerElement,String filePath){
		this.bitsPerElement = bitsPerElement;
		this.filePath = filePath;
		this.fileName = filePath;

	}
	
	public String getDocument(){
		return this.fileName;
	}
	
	public int filterSize(){
		if(bfd!=null)
			return bfd.filterSize;
		else{
			System.err.println("Filter is empty!\n");
			return -1;
		}
	}
	public int dataSize(){
		if(bfd!=null)
			return bfd.dataSize();
		else{
			System.err.println("Filter is empty!\n");
			return -1;
		}
	}
	public int numHashes(){
		if(bfd!=null)
			return bfd.numHashes();
		else{
			System.err.println("Filter is empty!\n");
			return -1;
		}
	}
	
	public boolean appears(String s){
		if(bfd!=null)
			return bfd.appears(s);
		else{
			System.err.println("Filter is empty!\n");
			return false;
		}
	}
	
	public void addDocument(Document d){
		
		TreeSet<String> biwordSet = d.getBiwordSet();
		if(biwordSet != null){
			int dataSize = biwordSet.size();
			bfd = new BloomFilterDet(dataSize,bitsPerElement);
			for(String s: biwordSet){
				bfd.add(s);
//				System.out.println(s);
			}
		}
	}

	@Override
	public String toString(){
		String result = "File Path: "+ this.filePath+"\nWord in document: "+ this.bfd.dataCount+"\n";
		return result;
	}

}
