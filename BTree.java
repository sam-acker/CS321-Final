
class BTree{
	
	BTreeNode root; //root
	
	public BTree() {
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
	
	
	
	
	
}
