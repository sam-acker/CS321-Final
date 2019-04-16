

class GeneBankSearch{
	
	
	/**
	
	java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<debug level>]
	
	*/
	public static void main(String... Args){
		
		//Parse input
		try{
		boolean useCache=false;
		String btreeFile=Args[1];
		String gpkFileName=Args[2];
		int debugLevel=0;
		if (Args[0]=="1"){
			useCache=true;
		}
		if (Args.length==4){
			debugLevel=Integer.parseInt(Args[3]);
		}
		}catch(Exception e){
			System.out.println("Error; format should be following:\njava java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<debug level>]");
			return;
		}
		//End parsing
		
		
	}
	
	
	
	
	
	
	
}