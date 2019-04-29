

/**
BTree class


*/
class BTree{
	
	private int seqLength;
	
	
	/**
	
	Node class
	BECAUSE THIS IS A NESTED CLASS, WE CAN KEEP THE VARIABLES PRIVATE BUT STILL DIRECTLY MANIPULATE THEM FROM THIS CLASS
	
	*/
	
	class BTreeNode{
		
		private boolean root;
		private int parentIndex;
		private int index;
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
		
		
	}
	
	
	
	/**
	Constructor
	
	
	*/
	public BTree(int seqLength){
		this.seqLength=seqLength;
		
		
		
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
