

/**
This class will be a single 'object' in a BTreeNode.

@author
*/

class TreeObject{
	
	private long key; //This is the sequence in long format
	private int frequency; //This is the frequency of the key

	 TreeObject(long key, int frequency) {
		this.key = key;
		this.frequency = frequency;
	}
	
	 TreeObject(long key) {
		this.key = key; 
		this.frequency = 1;
	}
	
	public long getKey() {
		return key;
	}
	
	public int frequency() {
		return frequeny
	}

	
}