package pa4.cs535.cs.iastate.edu;

import java.util.Comparator;

public class WeightedDocument {
	private String fileName;
	private double weight;
	
	private double length;
	private int numOfBiwordHit;
	
	public WeightedDocument(String fileName,double weight){
		this.fileName = fileName;
		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getName(){
		return this.fileName;
	}
	
	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public int getNumOfBiwordHit() {
		return numOfBiwordHit;
	}

	public void setNumOfBiwordHit(int numOfBiwordHit) {
		this.numOfBiwordHit = numOfBiwordHit;
	}

	public static Comparator<WeightedDocument> weightComparator 
    = new Comparator<WeightedDocument>() {

		public int compare(WeightedDocument d1, WeightedDocument d2) {

			if (d1.getWeight() == d2.getWeight()){
				return 0;
			}
			else{
				return d1.getWeight() < d2.getWeight() ? 1 : -1;
			}
		}
	};
	public static Comparator<WeightedDocument> biWordHitComparator 
    = new Comparator<WeightedDocument>() {

		public int compare(WeightedDocument d1, WeightedDocument d2) {

			if (d1.getNumOfBiwordHit() == d2.getNumOfBiwordHit()){
				return d1.getWeight() < d2.getWeight() ? 1 : -1;
			}
			else{
				return d1.getNumOfBiwordHit() < d2.getNumOfBiwordHit() ? 1 : -1;
			}
		}
	};
}
