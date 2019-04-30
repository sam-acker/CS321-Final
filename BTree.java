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
		//KEYS
		private TreeObject[] keys;
		
		//CHILDREN
		private int[] children;
		
		/**
		Constructor
		
		*/
		
		public BTreeNode(int degree,int index){
			this.index=index;
			children=new int[2 * degree - 1];
			keys=new TreeObject[2 * degree - 1];
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
			//byte[] newData=new byte[4096]
			ByteBuffer bb= ByteBuffer.allocate(4096);
			
			//ADD METADATA
			bb.putInt(parentIndex);
			
			//ADD KEYS
			for (int i=0;i<keys.length;i++){
				bb.putLong(keys[i].returnKey());
				bb.putInt(keys[i].returnFrequency());
				
			}
			
			//ADD CHILDREN
			for (int j=0;j<children.length;j++){
				bb.putInt(children[j]);
				
				
			}
			
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
