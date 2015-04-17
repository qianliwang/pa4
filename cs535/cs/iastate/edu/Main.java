package pa4.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String args[]){
		String folderPath = null;
		int k;
		QueryProcessor queryProcessor;
		String q;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			System.out.println("Please input the folder:");
//			folderPath = br.readLine();
			folderPath = "D:\\programmer\\workspace\\cs535_S2015\\space\\";
			queryProcessor = new QueryProcessor(folderPath);
			System.out.println("Please input the query word:");
//			q = br.readLine();
			q = "LUNAR SCIENCE AND ACTIVITIES";
			System.out.println("Please input the number of k:");
//			k = Integer.parseInt(br.readLine());
			k = 10;
			System.out.println("Query start...");
			queryProcessor.query(q.toLowerCase(), k);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
