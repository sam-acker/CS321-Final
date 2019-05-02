import java.io.File;
import java.IOException;
import java.io.FileReader;
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
		
		try{
		
		
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
		
		
	}
	private static String reverseString(String x) {
		if(x.length() == 1)
			return x;
		return "" + x.charAt(x.length() - 1) + reverseString(x.substring(0, x.length() -1));
		
	}
	
	private static void printOperation() {
		System.err.println("Operation : java GeneBankSearch" + "<0/1(no/with Cache)> <btree file> <query file>"
	+ "[<debug level>]\n");
		System.exit(1);
		
	}
	
	
	
	
	
	
}