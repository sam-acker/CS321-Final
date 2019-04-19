import java.io.File;
import java.io.FileNotFoundException;


class GeneBankCreateBTree{
	
	
	private static BTree bTree;
	
	/**
	
	java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]

	*/	
	public static void main(String... Args){
		
		int degree=0;
		int debugLevel=0;
		int seqLength=0;
		String gbkFileName=" ";
		boolean useCache=false;
		
		//Parse arg input 
		try{
		degree=Integer.parseInt(Args[1]);
		gbkFileName=Args[2];
		seqLength=Integer.parseInt(Args[3]);
		if (Args[0]=="1"){
			useCache=true;
		}
		if (Args.length==5){
			debugLevel=Integer.parseInt(Args[4]);
		}
		}catch(Exception e){
			System.err.println("Error; format should be following:\njava GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]");
			return;
		}
		//End arg parsing
		
		
		//Begin GBK parsing + insertion into BTree
		try{
			bTree = new BTree();
			
			GbkReader gbkReader = new GbkReader(new File(gbkFileName),seqLength);
			gbkReader.gotoOrigin();
			gbkReader.fillGeneBuffer();
			gbkReader.fillSeqBuffer();
		
		}catch(FileNotFoundException e){
			System.err.println("An error has occured opening the file: File not found");
			return;
		}
		
		
		
		//End GBK parsing + insertion into BTree
		
		
	}
	
	
	
	
	
	
	
}