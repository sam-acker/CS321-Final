import java.io.File;

import java.io.IOException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class GeneBankSearch{
	
	private static String bTreeFile,queryFile;
	private static int debugLevel =0; 
	private static boolean useCache = false;
	private static GeneUtility util;
	
	/**
	
	java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file>  [<debug level>]
	
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
		
		if (args[0]=="1"){
			useCache=true;
		} else if (!(args[0].equals("0") || args[0]==("1"))) {
			printOperation();
		}
		
		bTreeFile=args[1];
		queryFile=args[2];
		
		
		if (args.length == 4){
			debugLevel=Integer.parseInt(args[3]);
		}
		
		if (args.length == 5) {
			debugLevel = Integer.parseInt(args[4]);
		}
		}catch(Exception e){
			printOperation();
		}
		String sequence = " ";
		String degree = " ";
		//End parsing
		
			
		
		
		
		
		try {
			BTree tree = new BTree(bTreeFile);
			Scanner scan = new Scanner (new File(queryFile));
			while (scan.hasNextLine()){
				String seq=scan.next();
				int freq=tree.search(util.sequenceToLong(seq));
				
				//Print these
				
			}
			
			
			
			
			
			
			
		}catch(Exception e){
			System.err.println("ERROR: UNKNOWN");
			
			
		}
	}
	
	private static void printOperation() {	
		System.err.println("Operation : java GeneBankSearch" + "<0/1(no/with Cache)> <btree file> <query file>"	
	+ "[<debug level>]\n");	
		System.exit(1);	

 	}
}