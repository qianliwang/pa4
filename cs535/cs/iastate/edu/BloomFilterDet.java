package pa4.cs535.cs.iastate.edu;

public class BloomFilterDet extends BloomFilter{
	
	private final long FNV_32_PRIME = 0x01000193L;
	private final long FNV_32_INIT = 0x811c9dc5L;
	
	public BloomFilterDet(int setSize, int bitsPerElement){
		
		super(setSize,bitsPerElement);
		
	}
	
	public void add(String s){
		String tempS = s.toLowerCase();
		int fnvValue = detHash(tempS);
//		localFilter.set(fnvValue%this.filterSize);

		int hashPosition;
	
		long tempValue = tempS.hashCode() & 0xFFFFFFFFL;
		
		for(int i=0;i<k;i++){
			hashPosition = (int)((fnvValue+tempValue*i)%this.filterSize);
//			hashPosition = Math.abs(hashPosition);
			localFilter.set(hashPosition);
		}
		this.dataCount++;
	}
/*	
	public void add(String s){
		long fnvValue = FNV32(s);
		localFilter.set((int)(fnvValue%this.filterSize));
//		int hashPosition = fnvValue;
		long hashPosition = fnvValue;
		long tempValue = s.hashCode() & 0xFFFFFFFFL;
		for(int i=1;i<k;i++){
//			hashPosition = Math.abs(hashPosition);
			localFilter.set((int)((hashPosition+tempValue*i)%this.filterSize));
		}
		this.dataCount++;
	}*/
	
	public boolean appears(String s){
		boolean isHit = true;
		String tempS = s.toLowerCase();
		int fnvValue = FNV32(tempS);
		isHit = localFilter.get(fnvValue);
		
		if(isHit == false)
			return false;
		
		int hashPosition = fnvValue;
		long tempValue = tempS.hashCode() & 0xFFFFFFFFL;
		for(int i=1;i<k;i++){
			hashPosition = (int)((fnvValue+tempValue*i)%this.filterSize);
			isHit = localFilter.get(hashPosition);
			if(isHit == false)
				break;
			
		}
		return isHit;
		
	}
	
/*	public boolean appears(String s){
		boolean isHit = true;
		long fnvValue = FNV32(s);
		isHit = localFilter.get((int) (fnvValue % filterSize));
		
		if(isHit == false)
			return false;
		
		long hashPosition = fnvValue;
		long tempValue = s.hashCode() & 0xFFFFFFFFL;
		for(int i=1;i<k;i++){
			isHit = localFilter.get((int)((hashPosition+tempValue*i)%this.filterSize));
			if(isHit == false)
				break;
			
		}
		return isHit;
		
	}*/
	
	private int detHash(String s){
		return FNV32(s);
	}
	
	private int FNV32(String s){

		long h = FNV_32_INIT;
		
		char[] charArray = s.toCharArray();

		for(int i=0;i<charArray.length-1;i++){
			h = h^charArray[i];
			h = (h*FNV_32_PRIME) & 0xFFFFFFFFL;
		}
		h = h % this.filterSize; 
		return (int)h;
	}
	
/*	private long FNV32(String s){

		long h = FNV_32_INIT;
		
		String lowerCaseVar = s.toLowerCase();
		
		char[] charArray = lowerCaseVar.toCharArray();

		for(int i=0;i<charArray.length-1;i++){
			h = h^charArray[i];
			h = (h*FNV_32_PRIME) & 0xFFFFFFFFL;
		}
//		h = h % this.filterSize; 
		return h;
	}*/
	
	@Override
	public String toString(){
		String result = "BloomFilter Deterministic Hash:\n";
		result = result + super.toString();
		return result;
	}
	
}
