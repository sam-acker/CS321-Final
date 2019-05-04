import java.io.File;

import java.IOException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class GeneBankSearch{
	
	private static String btreeFile;
	private static String queryFile;
	private static int cacheSize = 0;
	private static int deubugLevel =0; 
	private static boolean useCahce = false;
	
	
	/**
	
	java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file>  [<debug level>]
	
	*/
	public static void main(String... Args){
		
	
		
		
		//Parse input
		
		// Capacity for the ByteBuffer
		int capacity = ;
		try {
			//ByteBuffer bb = ByteBuffer.allocate(capacity);
			//bb.
			//get args at spot 0
			String fileName = Args[0];
			//open file of name args at 0
			
			//convert binary file to byte array
			byte[] bArray = file coverted to byte array;
			BTree bt = new BTree(bArray);
			int freq = bt.search(/*given sequence*/);
		
		
		if(Args.length < 3 || > 5) {
			printOperation();
		}
		
		if (Args[0]=="1"){
			useCache=true;
		} else if (!(Args[0].equals("0") || args[0]==("1"))) {
			printOperation();
		}
		
		String btreeFile=Args[1];
		String gpkFileName=Args[2];
		
		
		if (Args.length == 4){
			debugLevel=Integer.parseInt(Args[3]);
		}
		
		if (Args.length == 5) {
			debugLevel = Integer.parseInt(Args[4]);
		}
		}catch(Exception e){
			System.out.println("Error; format should be following:\njava java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<debug level>]");
			return;
		}
		String sequence = " ";
		String degree = " ";
		//End parsing
		try {
			Scanner scan = new Scanner (new File(queryFile));
		}
		
	}

	private static void printOperation() {
		System.err.println("Operation : java GeneBankSearch" + "<0/1(no/with Cache)> <btree file> <query file>"
	+ "[<debug level>]\n");
		System.exit(1);
		
	}
	
	
	
	
	
	
}