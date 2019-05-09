import java.io.File;

import java.io.IOException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class GeneBankSearch{
	
	private static String bTreeFile,queryFile;
	private static int debugLevel,cacheSize =0; 
	private static boolean useCache = false;
	private static GeneUtility util;
	
	/**
	
	java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file>  [<debug level>]
	
	java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> <cache size>
[<debug level>]

	*/
	public static void main(String... args){
		
	
		
		
		//Parse input
		//useCache=false;
		//debugLevel=0;
		bTreeFile="";
		queryFile="";
		util=new GeneUtility();
		try {
		
		
		//if(Args.length < 3 || Args.length > 5) {
		//	printOperation();
		//}
		
		if (args[0].equals("1")){
			useCache=true;
		} else if (!(args[0].equals("0") || args[0]==("1"))) {
			printOperation();
		}
		
		bTreeFile=args[1];
		queryFile=args[2];
		
		if (useCache){
			cacheSize=Integer.parseInt(args[3]);
			
		}
		if (args.length == 4&&!useCache){
			debugLevel=Integer.parseInt(args[3]);
		}
		
		if (args.length == 5) {
			debugLevel = Integer.parseInt(args[4]);
		}
		}catch(Exception e){
			printOperation();
		}

		//End parsing
		
			
		
		int x=0;
		
		
		try {
			//System.out.println("Trying search");
			BTree tree = new BTree(bTreeFile,cacheSize);
			Scanner scan = new Scanner (new File(queryFile));
			while (scan.hasNextLine()){
				String seq=scan.next();
				if (seq.length()!=tree.getSeqLength()){
					System.err.println("Mismatch between BTree sequence length and query file");
					System.exit(1);
				}
				//System.out.println(seq);
				int freq=tree.search(util.sequenceToLong(seq));
				if  (freq!=0){
				System.out.println(freq+"\t"+seq);
				}
				x++;
				//Print these
				
			}
			
			
			return;
			
			
			
			
		}catch(Exception e){
			System.err.println(x+" QUERIES RAN");
			//e.printStackTrace(System.out);
			System.err.println("End of Query file"); //hehe
			
			
		}
	}
	
	private static void printOperation() {	
		System.err.println("Operation : java GeneBankSearch" + "<0/1(no/with Cache)> <btree file> <query file>"	
	+ "[<debug level>]\n");	
		System.exit(1);	

 	}
}