import java.nio.ByteBuffer;

/**
BTree class


*/
class BTree{
	
	private int seqLength,degree,numKeys,blockSize;
	
	
	/**
	
	Node class
	BECAUSE THIS IS A NESTED CLASS, WE CAN KEEP THE VARIABLES PRIVATE BUT STILL DIRECTLY MANIPULATE THEM FROM THIS CLASS
	
	*/
	
	class BTreeNode{
		
		//private boolean root; //if parentIndex==null we can assume root?
		private int parentIndex,index;
		//KEYS -- WE MAY WANT TO REPLACE THESE WITH ARRAY LISTS
		
		private TreeObject[] keys;
		
		//CHILDREN -- WE MAY WANT TO REPLACE THESE WITH ARRAY LISTS
		private int[] children;
		
		/**
		Constructor
		
		*/
		
		public BTreeNode(int degree,int index){
			this.index=index;
			children=new int[numKeys+1];
			
			for (int i=0;i<children.length;i++){
				children[i]=-1;
			}
			
			keys=new TreeObject[numKeys];
			
			for (int j=0;j<keys.length;j++){
				keys[j]=new TreeObject();
			}
			
		}
		
		/**
		Reconstruct a B Tree Node from disk for searching
		I have not tested this method yet
		*/
		
		public BTreeNode(byte[] data,int index){
			this.index=index;
			ByteBuffer bb = ByteBuffer.allocate(data.length);
			//READ META DATA
			parentIndex=bb.getInt(0);
			//READ KEY DATA
			for (int i=0;i<numKeys;i++){
				keys[i]=new TreeObject(bb.getLong((i*12)+4),bb.getInt((i*12)+12));
				// 0M4K12F16K24F28
			}
			int nIndex=4+(12*numKeys);
			//READ CHILDREN DATA
			for (int j=0;j<numKeys+1;j++){
				children[j]=bb.getInt((4*j)+nIndex);
			}
		}
		
		
		
		
		/**
		returns true if full, false if not full
		*/
		
		public boolean full(){
			return false;
		}
		
		
		/**
		Converts itself into a byte[4096]
		will be passed into TFileWriter for writing to file
		*/
		public byte[] toByte(){
			//public ByteBuffer toByte(){
			//byte[] newData=new byte[4096]
			ByteBuffer bb= ByteBuffer.allocate(blockSize);
			
			//ADD METADATA
			
			//TEST
			//parentIndex=69;
			//
			bb.putInt(parentIndex);
			
			//ADD KEYS
			for (int i=0;i<keys.length;i++){
				bb.putLong(keys[i].returnKey());
				bb.putInt(keys[i].returnFrequency());
				
			}
			//ADD CHILDREN
			for (int j=0;j<children.length;j++){
				try{
				bb.putInt(children[j]);
				}catch(Exception e){
					System.out.println("FATAL:\nKEYS SIZE: "+keys.length+"\nCHILDREN SIZE: "+children.length+"\nAT J= "+j);

				}
			}
			System.out.println(bb.toString());
			//return bb;
			return bb.array();
		}
		
		
	}
	
	
	
	/**
	Constructor
	
	
	*/
	public BTree(int seqLength,int degree,int blockSize){
		this.seqLength=seqLength;
		this.degree=degree;
		this.blockSize=blockSize;
		numKeys=2*degree-1;
		
		
		
		
		/*
		//TEST CODE APPEARS TO WORK WELL
		BTreeNode dummy= new BTreeNode(degree,13);
		ByteBuffer nt=dummy.toByte();
		System.out.println(nt.getInt(0));
		System.out.println(nt.getLong(4));
		//
		*/
	}
	
	/**
	insert into BTree
	
	*/
	public void insert(){
		
		
		
	}
	
	
	/**
	Nodes split when full
	
	*/
	
	public void split(){
		
		
		
	}
	
	/**
	Search BTree
	
	*/
	public void search(){
		
		
	}
	
	/**
	Print the tree to a txt file (if debug==1)
	*/
	public void dumpToTextfile(){
		
		
		
	}
	
	
	
	
}
