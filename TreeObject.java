

/**
This class will be a single 'object' in a BTreeNode.

@author Big ben
*/

class TreeObject implements Comparable<TreeObject> {
	
	private long key; //This is the sequence in long format
	private int frequency; //This is the frequency of the key

	 public TreeObject(long key, int frequency) {
		this.key = key;
		this.frequency = frequency;
	}
	
	 public TreeObject(long key) {
		this.key = key; 
		frequency = 1;
	}
	
	
	
	public long getKey() {
		return key;
	}
	
	public void increaseFrequency() {
		frequency ++;
	}
	
	public int frequency() {
		return frequency;
	}
	public int compareTo(TreeObject x) {
		
				}

	
}