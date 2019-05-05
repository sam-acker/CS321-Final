/**
This class will be a single 'object' in a BTreeNode.

@author Big ben
*/
class TreeObject implements Comparable < TreeObject > {

    private long key; //This is the sequence in long format //8 BYTES
    private int frequency; //This is the frequency of the key //4 BYTES

    public TreeObject(long key, int frequency) {
        this.key = key;
        this.frequency = frequency;
    }

    public TreeObject(long key) {
        this.key = key;
        frequency = 1;
    }

    /**
    This will represent an EMPTY tree object
    this makes the tobyte method of btreenode more effecient
    MIGHT BE DELETED USING ARRAYLISTS
    
     public TreeObject() {
    	this.key = -1L; 
    	frequency = -1;
    }
    */
    public void increaseFrequency() {
        frequency++;
    }

    public int returnFrequency() {
        return frequency;
    }

    public long returnKey() {
        return key;
    }

    public int compareTo(TreeObject x) {
        if (this.key > x.key) {
            return 1;
        } else if (this.key < x.key) {
            return -1;
        } else
            return 0;
    }
    public String toString() {
        return null;
    }

}