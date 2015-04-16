package pa4.cs535.cs.iastate.edu;

import java.util.BitSet;

public class BloomFilter {
	protected int setSize;
	protected int bitsPerElement;
	
	protected int filterSize;
	protected int k;
	
	protected int dataCount;
	
	protected BitSet localFilter;
	
	public BloomFilter(int setSize, int bitsPerElement){
		
		this.setSize = setSize;
		
		this.bitsPerElement = bitsPerElement;
		
		this.k = (int) Math.ceil((Math.log(2)*this.bitsPerElement));
		
		this.filterSize = this.setSize*this.bitsPerElement;
		
		while(!isPrime(this.filterSize)){
			this.filterSize++;
		}
		
		localFilter = new BitSet(this.filterSize);
	}
	public int filterSize(){
		return filterSize;
	}
	
	public int dataSize(){
		return dataCount;
	}
	
	public int numHashes(){
		return k;
	}
	
	protected boolean isPrime(int n) {
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}
	
	protected void add(String s){
		System.err.println("Something wrong here. Please see the override function of sub-class");
	}
	
	protected boolean appears(String s){
		return false;
	}
	
	@Override
	public String toString(){
		String result = "setSize:"+setSize+"\nbitsPerElement:"+bitsPerElement +"\nhashFunctionNumber:"+k+"\nfilterSize:"+filterSize+"\nBitSet cardinality:"+localFilter.cardinality()+"\n";
		return result;
	}
	
	protected void clearFilter(){
		this.localFilter.clear();
	}
}
