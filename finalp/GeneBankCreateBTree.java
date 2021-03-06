import java.io.File;
import java.io.FileNotFoundException;
/**
This file creates a B Tree, accepts arguments, and runs a .gbk file parser



@author Chris Bentley
 */

class GeneBankCreateBTree{


	private static BTree bTree;
	private static final int BLOCK_SIZE=4096; //BYTES

	/**

	java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]
	
	IF CACHE is true
	
	java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length>
	<cache size> [<debug level>]

	

	 */	
	public static void main(String... Args){

		int degree=0;
		int debugLevel=0;
		int seqLength=0;
		int cacheSize=0;
		String gbkFileName=" ";
		boolean useCache=false;
		
		GeneUtility util = new GeneUtility(); //TESTING
		
		

		//Parse arg input 
		try{
			degree=Integer.parseInt(Args[1]);
			gbkFileName=Args[2];
			seqLength=Integer.parseInt(Args[3]);
			if (Integer.parseInt(Args[0])==1){
				useCache=true;
			}
			if (Args.length==5&&useCache==false){
				debugLevel=Integer.parseInt(Args[4]);
			}
			
			if (useCache==true){
				cacheSize=Integer.parseInt(Args[4]);
				if (Args.length==6){
				debugLevel=Integer.parseInt(Args[5]);
				}
				//System.out.println("Using cache size "+cacheSize);
			}
			
		}catch(Exception e){
			System.err.println("Error; format should be following:\njava GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<debug level>]");
			return;
		}

		if (seqLength>31||seqLength<1){
			System.err.println("Error: Sequence length must be less than 31 and greater than 0");
			return;

		}
		if (degree==0){
			degree=optimumDegree(BLOCK_SIZE);
			
			
		}
		
		if (useCache==false){
			cacheSize=-1;
		}
		
		//System.out.println(degree);

		

		//End arg parsing


		//Begin GBK parsing + insertion into BTree
		try{
			
			
			bTree = new BTree(seqLength,degree,BLOCK_SIZE,cacheSize,gbkFileName);
			
			
			GbkReader gbkReader = new GbkReader(new File(gbkFileName),seqLength);
			//System.out.println(gbkReader.nextSequence());
			//System.out.println("result above");
			//Begin sequence retrieval and insertion
			while (gbkReader.gotoOrigin()){
				gbkReader.fillGeneBuffer();

				while(gbkReader.fillSeqBuffer()){
					int i=0;//testing
					while (gbkReader.hasNext()){

						i++;
						//insert sequence into tree

						//gbkReader.nextSequence() WILL GO THROUGH EVERY POSSIBLE SEQ IN FILE
						//USE IT TO INSERT INTO B TREE - MIGHT BE MADE INTO LONG FIRST
						
						Long nxtS=gbkReader.nextSequence();
						//System.out.println("INSERTING "+ util.longToSequence(nxtS,seqLength)+"  LONG KEY "+nxtS);
						
						bTree.insert(nxtS);
						//System.out.println(gbkReader.nextSequence());
					}


				}






			}
			
			//System.out.println(bTree.root.keys.size());
			//System.out.println(bTree.root.keys.get(0).returnKey());



			//gbkReader.fillGeneBuffer();
			//gbkReader.fillSeqBuffer();
			if (debugLevel==1){
			bTree.dumpToTextfile();
		}

			
			

		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("something went wrong");
			return;
		}
		
		//System.out.println("DONE");

		//End GBK parsing + insertion into BTree


	}



	/**
	Find optimum degree of BTree based on metadata sizes
			
			BTREENODE X
	|   meta data: parentNode 4 BYTES
	|	KEYS 12 BYTES EACH
	|	CHILDREN 4 BYTES EACH
	
	
	
	*/
	
		public static int optimumDegree(int blockSize){
		// UNITS IN BYTES // AN INT IS 4 BYTES // LONG 8 BYTES
		int deg=1;
		int metaSize=0;
		int dataSize=0;
		int keyCount=2*deg-1;
		
		while ((metaSize+dataSize)<blockSize){
			deg++;
			keyCount=2*deg-1;
			//12 FOR TREEOBJECT	4 FOR EACH CHILD
			dataSize=(keyCount*12)+((keyCount+1)*4);
		}
		deg--;
		return deg;
		
		
		
		
	}







}