import java.util.LinkedList;
import java.util.Queue;

class BTree{
	
	int degree;
	BTreeNode root; //Root of Tree
	Queue<BTreeNode> queue = new LinkedList<BTreeNode>();	//Queue for Searching
	
	public BTree(int degree) {
		this.degree = degree;
		root = null;
	}
	
	//Node in tree
	class BTreeNode{
		public long key;	//base 4 binary DNA sequences of length k (1 <= k <= 31)
		public BTreeNode left;
		public BTreeNode right;

		public BTreeNode(long key) {	
			this.key = key;
			left = null;
			right = null;
		}
	}
	
	//Calls insertRecursive
	void insert(long key) {
		root = insertRecursive(root, key);
	}
	
	//Recursive function to insert nodes to BTree
	BTreeNode insertRecursive(BTreeNode root, long key) {
		if(root == null) {
			root = new BTreeNode(key);
			return root;
		}
		
		if(key < root.key) {
			root.left = insertRecursive(root.left, key);
		} else if(key > root.key) {
			root.right = insertRecursive(root.right, key);

		}
		return root;
	}
	
	//Returns number of times a specified DNA sequence occurs within the tree
	int seqSearch(long key) {
		if(root == null)
			return -1;
		
		int retVal = 0;
		
		queue.clear();
		queue.add(root);
		while(!queue.isEmpty()) {
			BTreeNode n = queue.remove();
			
			if(n.key == key) {
				retVal++;
			}
			
			if(n.left != null)
				queue.add(n.left);
			if(n.right != null)
				queue.add(n.right);
		}
		
		return retVal;
	}
	
	
	
	
	
}
