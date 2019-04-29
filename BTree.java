import java.util.LinkedList;

/**
BTree class


*/
class BTree{
	
	
	
	
	/**
	
	Node class
	BECAUSE THIS IS A NESTED CLASS, WE CAN KEEP THE VARIABLES PRIVATE BUT STILL DIRECTLY MANIPULATE THEM FROM THIS CLASS
	
	*/
	
	class BTreeNode{
		
		private boolean root;
		
		//KEYS
		LinkedList<TreeObject> keys;
		
		//CHILDREN
		LinkedList<Integer> children;
		
		
		
		/**
		Constructor
		
		*/
		
		public BTreeNode(){
			
			
			
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
	public BTree(){
		
		
		
		
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
