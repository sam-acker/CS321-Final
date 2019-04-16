


class GeneBankCreateBTree{
	
	
	
	/**
	
	java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]

	*/	
	public static void main(String... Args){
		
		//Parse input
		try{
		boolean useCache=false;
		int degree=Integer.parseInt(Args[1]);
		String gpkFileName=Args[2];
		int debugLevel=0;
		int seqLength=Integer.parseInt(Args[3]);
		if (Args[0]=="1"){
			useCache=true;
		}
		if (Args.length==5){
			debugLevel=Integer.parseInt(Args[4]);
		}
		}catch(Exception e){
			System.out.println("Error; format should be following:\njava GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]");
			return;
		}
		//End parsing
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
}