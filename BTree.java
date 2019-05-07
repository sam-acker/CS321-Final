import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
/**
BTree class

@author Chris Bentley
*/
class BTree {


    /**

    Node class
    BECAUSE THIS IS A NESTED CLASS, WE CAN KEEP THE VARIABLES PRIVATE BUT STILL DIRECTLY MANIPULATE THEM FROM THIS CLASS

    */

    class BTreeNode implements Comparable<BTreeNode>{

        //private boolean root; //if parentIndex==null we can assume root?
        private int index;
        //KEYS

        public ArrayList < TreeObject > keys;

        //CHILDREN
        private ArrayList < Integer > children;


        /**
        Constructor

        */

        public BTreeNode(int degree, int index) {
            this.index = index;
            //children=new int[numKeys+1];
            children = new ArrayList < Integer > (numKeys + 1);
            //for (int i=0;i<children.length;i++){
            //	children[i]=-1;
            //}

            //keys=new TreeObject[numKeys];
            keys = new ArrayList < TreeObject > (numKeys);
            //for (int j=0;j<keys.length;j++){
            //	keys[j]=new TreeObject();
            //}

        }

        /**
        Reconstruct a B Tree Node from disk for searching
        I have not tested this method yet
        */

        public BTreeNode(byte[] data, int index) {
            this.index = index;
            ByteBuffer bb = ByteBuffer.allocate(data.length);
            //System.out.println("Reconstructing... data length: "+data.length);
            bb.put(data);
            children = new ArrayList < Integer > (numKeys + 1);
            keys = new ArrayList < TreeObject > (numKeys);
            //READ META DATA
            //parentIndex=bb.getInt(0);
            //READ KEY DATA
            for (int i = 0; i < numKeys; i++) {

                if (bb.getLong((i * 12)) != -1) {
                    keys.add(new TreeObject(bb.getLong((i * 12)), bb.getInt((i * 12) + 8)));

                }

                // 0M4K12F16K24F28
            }
            int nIndex = (12 * numKeys);
            //READ CHILDREN DATA
            for (int j = 0; j < numKeys + 1; j++) {
                if (bb.getInt((4 * j) + nIndex) != -1) {
                    children.add(bb.getInt((4 * j) + nIndex));
                }
            }
        }




        /**
        returns true if full, false if not full
        */
        public boolean isFull() {
            //System.out.println("NUM KEYS "+numKeys+"  AND KEYS SIZE "+keys.size());
            if (keys.size() >= numKeys) {
                return true;
            }
            return false;
        }

        /**
        returns true if the node is a leaf
        */
        public boolean isLeaf() {
            if (children.size() == 0) {
                return true;
            }
            return false;
        }


        /**
        Converts itself into a byte[4096]
        will be passed into TFileWriter for writing to file
        */
        public byte[] toByte() {
            //public ByteBuffer toByte(){
            //byte[] newData=new byte[4096]
            ByteBuffer bb = ByteBuffer.allocate(blockSize);

            //ADD METADATA

            //TEST
            //parentIndex=69;
            //
            //bb.putInt(parentIndex);

            //ADD KEYS
            for (int i = 0; i < keys.size(); i++) {
                bb.putLong(keys.get(i).returnKey());
                bb.putInt(keys.get(i).returnFrequency());
            }
            for (int i = 0; i < (numKeys - keys.size()); i++) {
                bb.putLong(-1l); //invalid keys to represent empty slots
                bb.putInt(-1);
            }
            //ADD CHILDREN
            for (int i = 0; i < children.size(); i++) {
                bb.putInt(children.get(i));

            }
            for (int i = 0; i < ((numKeys + 1) - children.size()); i++) {
                bb.putInt(-1);

            }
            //Looking at this in a hex editor, it is obvious there are some 0's in the file at the end between the last child and the
            //end of the actual block, this is fine however because the program will NEVER scan these.

            return bb.array();
        }
		@Override
		public int compareTo(BTreeNode i){
			if (index==i.index){
				return 0;
			}
			return -1;
			
			
		}
    }

    //Begin BTree.java class
    public int seqLength, degree, numKeys, blockSize, size;
    public BTreeNode root, parent, toInsert;
    private TFileWriter TFile;
	private Cache<BTreeNode> cache;


    /**
    Constructor
    */
    public BTree(int seqLength, int degree, int blockSize, int cacheSize, String fileName) throws IOException {
        size = 0;
        this.seqLength = seqLength;
        this.degree = degree;
        this.blockSize = blockSize;
        numKeys = 2 * degree - 1;
        //TFile=new TFileWriter(seqLength,degree,(fileName+"."+seqLength+".t"));
        TFile = new TFileWriter(fileName + "." + seqLength + ".t");
        TFile.writeBOFMetaData(seqLength, degree, 12);
        root = new BTreeNode(degree, size++);
        TFile.writeData(root.toByte(), root.index);
		
		//Create cache
		if (cacheSize>0){
			cache=new Cache<BTreeNode>(cacheSize);
		}
    }


    /**
    reConstructor from disk
    */
    public BTree(String filename) throws IOException {

        try {
            TFile = new TFileWriter(filename);

            int[] metadata = TFile.readBOFMetaData();
            seqLength = metadata[0];
            degree = metadata[1];
            numKeys = 2 * degree - 1;
            blockSize = 4096;
            root = new BTreeNode(TFile.readNodeData(0), 0);

        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.err.println("ERROR: An unexpected file exception has occured BTREE RECON");
            return;
        }


    }




    /**
    insert into BTree

    */
    public void insert(long key) throws IOException {

        //For actual insertion, check if root needs to be split, this is a special case (root creates 2 nodes below it, if root
        //has children, children are split to 2 new root child nodes's children

        if (root.isFull()) {
            System.out.println("ATTEMPTING ROOT SPLIT");
            rootSplit();
        }

        //If root is fine, find node to insert to, if full, split then add key

        toInsert = root;
        objInsert(new TreeObject(key));

        //If not full, add as normal

    }

    public void objInsert(TreeObject obj) throws IOException {
        //actually insert key
        if (toInsert.isLeaf()) {
            //System.out.println("leafCheck");
            //We have found the place to insert
            //figure out correct index to place into
            for (int i = 0; i < toInsert.keys.size(); i++) {
                int comp = obj.compareTo(toInsert.keys.get(i));
                if (comp == -1) {
                    //Correct key spot, insert
                    toInsert.keys.add(i, obj);
                    TFile.writeData(toInsert.toByte(), toInsert.index);
                    //DONE
                    return;
                } else if (comp == 0) {
                    //Duplicate
                    //System.out.println("Found Duplicate");
                    toInsert.keys.get(i).increaseFrequency();
                    TFile.writeData(toInsert.toByte(), toInsert.index);
                    //DONE
                    return;
                }
            }
            //If we reach here we know we need to add key to last spot
            //System.out.println("Found last key spot");
            toInsert.keys.add(obj);
            TFile.writeData(toInsert.toByte(), toInsert.index);
            //DONE
            return;
        } else {

            int i = 0;
            while (obj.compareTo(toInsert.keys.get(i)) != -1) {
                i++;
                if (i >= toInsert.keys.size()) {
                    //i--;
                    break;
                }

            }
			//System.out.println("DEBUG:\nToinsert("+toInsert.index+") children size:"+toInsert.children.size()+"\nOn i="+i+" key amount="+toInsert.keys.size());
			BTreeNode nextInsert=null;
			if (cache!=null){
				nextInsert = cache.removeObject(new BTreeNode(degree,toInsert.children.get(i)));
			}
			if (nextInsert==null){
				nextInsert = new BTreeNode(TFile.readNodeData(toInsert.children.get(i)), toInsert.children.get(i));
			}else{
				//System.out.println("SUCCESSFUL CACHE PULL");
			}
			if (cache!=null){
				cache.addObject(nextInsert);
			}
            //check if child is full, if so split
            if (nextInsert.isFull()) {
                parent = toInsert;
                split(nextInsert, i);

                //Search toInsert again before going to child
                for (int j = 0; j < toInsert.keys.size(); j++) {
                    int comp = obj.compareTo(toInsert.keys.get(j));
                    if (comp == 0) {
                        //Duplicate
                        toInsert.keys.get(j).increaseFrequency();
                        TFile.writeData(toInsert.toByte(), toInsert.index);
                        //DONE
                        return;
                    }
                }
            }
            toInsert = nextInsert;
            objInsert(obj);
            //recursively call objInsert after setting toInsert to the child
            return;
        }
    }




    /**
    Nodes split when full
    */
    public void split(BTreeNode node, int index) {

        //Splitting will only create 1 new node (Exception: root splits)
        System.out.println("ATTEMPTING NODE SPLIT ON INDEX " + index + " NODE KEY SIZE: " + node.keys.size()+"\nChild size="+node.children.size());
		
        BTreeNode newNode = new BTreeNode(degree, size++);

        //We want to get (1,2,3) MIDDLE NODE(2)
        //OR (1,2,3,4) MIDDLE LEFT (2)
        int middleIndex = (int) Math.ceil(((double) node.keys.size() / 2) - 1);
        //TreeObject c = root.keys.get(middleIndex);
        System.out.println("MIDDLE INDEX: " + middleIndex);
        //Parent is parent
        //Current node is node
        //other node is newNode
        //Move keys to new node
        System.out.println("SPLIT MOVE KEYS");
        int rKeySize = node.keys.size();
        for (int i = middleIndex + 1; i < rKeySize; i++) {
            newNode.keys.add(node.keys.remove(middleIndex + 1));
        }

        //Move children to new node
        System.out.println("SPLIT MOVE CHILDREN ON middleindex="+middleIndex);
		int bugFix=node.children.size();
        for (int i = middleIndex + 1; i < bugFix; i++) {
           // if (i < root.children.size()||node.index>10) {
				//if (i < numKeys+1) {
                newNode.children.add(node.children.remove(middleIndex + 1));
           // }
        }

        //Add new node to parent's children
        parent.children.add(index + 1, newNode.index);

        //Move middle key to parent
        parent.keys.add(index, node.keys.remove(middleIndex));
		//if (size==993){
		//	System.out.println("Stopping at index 992, split node index="+node.index+"\nnew Node children size="+newNode.children.size()+"\nNewnode key size="+newNode.keys.size());
		//	System.exit(0);
		//}
        TFile.writeData(parent.toByte(), parent.index);
        TFile.writeData(node.toByte(), node.index);
        TFile.writeData(newNode.toByte(), newNode.index);

    }

    /**
    Might need tweaking; untested
	
    */

    public void rootSplit() {
        // 0 1 2 3    size=4, /2 = 2 -1 = 1
        // 0 1 2      size=3, /2 = 1.5 -1 = .5 ciel>1
        int middleIndex = (int) Math.ceil(((double) root.keys.size() / 2) - 1);
        TreeObject c = root.keys.get(middleIndex);

        BTreeNode left = new BTreeNode(degree, size++);
        BTreeNode right = new BTreeNode(degree, size++);

        //Move keys to left node
        for (int i = 0; i < middleIndex; i++) {
            left.keys.add(root.keys.get(i));
        }

        //Move keys to right node
        for (int i = middleIndex + 1; i < root.keys.size(); i++) {
            right.keys.add(root.keys.get(i));
        }
        System.out.println("Processed keys");
        //Update the root's keys
        root.keys.clear();
        root.keys.add(c);

        // 0 1 2 
        //0 1 2 3 

        System.out.println("Prepare process left child");

        //Move children to left
        for (int i = 0; i < middleIndex + 1; i++) {
            if (i < root.children.size()) {
                left.children.add(root.children.get(i));
            }
        }
        System.out.println("Processed left children");
        //Move children to right
        for (int i = middleIndex + 1; i < root.children.size(); i++) {
            if (i < root.children.size()) {
                right.children.add(root.children.get(i));
            }
        }
        System.out.println("Processed children");
        root.children.clear();
        root.children.add(left.index);
        root.children.add(right.index);
        TFile.writeData(root.toByte(), root.index);
        TFile.writeData(left.toByte(), left.index);
        TFile.writeData(right.toByte(), right.index);

        System.out.println("Root split successful");
    }


    /**
    Search BTree

    */
    public int search(long key) throws IOException {
        TreeObject treeKey = new TreeObject(key);
        BTreeNode search = root;
        while (true) {
            //Start comparing keys
            for (int i = 0; i < search.keys.size(); i++) {
                int comp = treeKey.compareTo(search.keys.get(i));
                if (comp == 0) {
                    //WHAT LUCK
                    return search.keys.get(i).returnFrequency();
                }
                if (comp == -1) {
                    //Stop here,
                    if (search.isLeaf()) {
                        return 0;

                    }
                    search = new BTreeNode(TFile.readNodeData(search.children.get(i)), search.children.get(i));
                    break;

                }
				
                if (i == search.keys.size() - 1) {
					if (search.isLeaf()) {
                        return 0;

                    }
                    search = new BTreeNode(TFile.readNodeData(search.children.get(i + 1)), search.children.get(i + 1));
                    break;

                }
            }


        }

    }

    /**
    Print the tree to a txt file (if debug==1)
    */
    public void dumpToTextfile()throws IOException {
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("dump.txt")));
		
		
		
		
		bwr.write("bush did 9/11");
		
		
		
		
		
		
		
		
		
		
		//flush the stream
		bwr.flush();
		
		//close the stream
		bwr.close();

    }




}