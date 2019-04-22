import java.io.File;
import java.io.FileNotFoundException;

/**
This file creates a B Tree, accepts arguments, and runs a .gbk file parser

STILL IN DEVELOPMENT


@author Chris Bentley
 */

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

		if (seqLength>31||seqLength<1){
			System.err.println("Error: Sequence length must be less than 31 and greater than 0");
			return;

		}




		//End arg parsing


		//Begin GBK parsing + insertion into BTree
		try{
			//bTree = new BTree();

			GbkReader gbkReader = new GbkReader(new File(gbkFileName),seqLength);
			//System.out.println(gbkReader.nextSequence());
			//System.out.println("result above");
			//Begin sequence retrieval and insertion
			while (gbkReader.gotoOrigin()){
				gbkReader.fillGeneBuffer();

				while(gbkReader.fillSeqBuffer()){

					while (gbkReader.hasNext()){


						//insert sequence into tree

						//gbkReader.nextSequence() WILL GO THROUGH EVERY POSSIBLE SEQ IN FILE
						//USE IT TO INSERT INTO B TREE - MIGHT BE MADE INTO LONG FIRST
						System.out.println(gbkReader.nextSequence());
					}


				}






			}



			//gbkReader.fillGeneBuffer();
			//gbkReader.fillSeqBuffer();

		}catch(FileNotFoundException e){
			System.err.println("An error has occured opening the file: File not found");
			return;
		}



		//End GBK parsing + insertion into BTree


	}








}