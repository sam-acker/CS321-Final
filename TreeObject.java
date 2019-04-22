

/**
This class will be a single 'object' in a BTreeNode.

@author
*/

class TreeObject{
	
	private long key; //This is the sequence in long format
	private int frequency; //This is the frequency of the key
	
	/**
	Constructor
	
	*/
	
	public TreeObject(long key){
		this.key=key;
		frequency=1;
	}
	/**
	Increment the frequency for when duplicates show up
	*/
	public void increaseFrequency(){
		frequency++;
	}
	
	
	
}