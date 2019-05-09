import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Stack;


import java.util.concurrent.TimeUnit;

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
            children = new ArrayList < Integer > (numKeys + 1);
            keys = new ArrayList < TreeObject > (numKeys);
        }

        /**
        Reconstruct a B Tree Node from disk for searching
        I have not tested this method yet
        */

        public BTreeNode(byte[] data, int index) {
            this.index = index;
            ByteBuffer bb = ByteBuffer.allocate(data.length);
            bb.put(data);
            children = new ArrayList < Integer > (numKeys + 1);
            keys = new ArrayList < TreeObject > (numKeys);
            //READ KEY DATA
            for (int i = 0; i < numKeys; i++) {

                if (bb.getLong((i * 12)) != -1) {
                    keys.add(new TreeObject(bb.getLong((i * 12)), bb.getInt((i * 12) + 8)));
                }
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
            ByteBuffer bb = ByteBuffer.allocate(blockSize);
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
    public int seqLength, degree, numKeys, blockSize, size, middleIndex;
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
        numKeys = 2 * degree - 1;
		this.blockSize=(numKeys*12)+((numKeys+1)*4);
        TFile = new TFileWriter(fileName + "." + seqLength + ".t");
		TFile.setBlockSize(this.blockSize);
        TFile.writeBOFMetaData(seqLength, degree, 12);
        root = new BTreeNode(degree, size++);
        TFile.writeData(root.toByte(), root.index);
		middleIndex = (int) Math.ceil(((double) numKeys / 2) - 1);
		//System.out.println(degree+"  deg  and mid:"+middleIndex);
		//Create cache
		if (cacheSize>0){
			cache=new Cache<BTreeNode>(cacheSize);
		}
    }


    /**
    reConstructor from disk
    */
    public BTree(String filename,int cacheSize) throws IOException {
        try {
            TFile = new TFileWriter(filename);
            int[] metadata = TFile.readBOFMetaData();
            seqLength = metadata[0];
            degree = metadata[1];
            numKeys = 2 * degree - 1;
            blockSize=(numKeys*12)+((numKeys+1)*4);
			TFile.setBlockSize(this.blockSize);
            root = new BTreeNode(TFile.readNodeData(0), 0);
			middleIndex = (int) Math.ceil(((double) numKeys / 2) - 1);
			//Create cache
			if (cacheSize>0){
				//System.out.println("Making cache");
				cache=new Cache<BTreeNode>(cacheSize);
			}

        } catch (Exception e) {
           // e.printStackTrace(System.out);
            System.err.println("ERROR: An unexpected file exception has occured BTREE RECON");
            return;
        }
    }


    /**
    insert into BTree (runs the process)

    */
    public void insert(long key) throws IOException {

        //For actual insertion, check if root needs to be split, this is a special case (root creates 2 nodes below it, if root
        //has children, children are split to 2 new root child nodes's children

        if (root.isFull()) {
            rootSplit();
        }
        toInsert = root;
        objInsert(new TreeObject(key));
		

    }
	/**
	Actually insert a key into the Btree
	
	*/

    public void objInsert(TreeObject obj) throws IOException {
        //actually insert key
        if (toInsert.isLeaf()) {
            //We have found the place to insert
            //figure out correct index to place into
            for (int i = 0; i < toInsert.keys.size(); i++) {
                int comp = obj.compareTo(toInsert.keys.get(i));
                if (comp == -1) {
                    //Correct key spot, insert
                    toInsert.keys.add(i, obj);
                    TFile.writeData(toInsert.toByte(), toInsert.index);
					//if (obj.returnKey()==135l){
				//System.out.println("NEW gact");
			//}
                    //DONE
                    return;
                } else if (comp == 0) {
                    //Duplicate
                    toInsert.keys.get(i).increaseFrequency();
                    TFile.writeData(toInsert.toByte(), toInsert.index);
					//if (obj.returnKey()==4l){
				//System.out.println("NEW aaca");
			//}
                    //DONE
                    return;
                }
            }
            //If we reach here we know we need to add key to last spot
            //System.out.println("Found last key spot");
            toInsert.keys.add(obj);
            TFile.writeData(toInsert.toByte(), toInsert.index);
			//if (obj.returnKey()==135l){
				//System.out.println("NEW gact END INDEX "+toInsert.index+"Key size "+toInsert.keys.size());
				//for (int i=0;i<toInsert.keys.size();i++){
				//	System.out.println(toInsert.keys.get(i).returnKey());
				//}
				
				
			//}
            //DONE
            return;
        } else {
            int i = 0;
			if (obj.returnKey()==135&&toInsert.index==5){
				//System.out.println("Being compared to "+toInsert.keys.get(i).returnKey());
				//for (int j=0;j<toInsert.keys.size();j++){
				//	System.out.println(toInsert.keys.get(j).returnKey());
				//}
				}
            while (obj.compareTo(toInsert.keys.get(i)) != -1) {
				if (obj.compareTo(toInsert.keys.get(i)) == 0){
                    toInsert.keys.get(i).increaseFrequency();
                    TFile.writeData(toInsert.toByte(), toInsert.index);
                    //DONE
                    return;
				}
				//if (obj.returnKey()==4l&&toInsert.index==7){
				//System.out.println("Being compared to "+toInsert.keys.get(i).returnKey());
				//}
                i++;
                if (i >= toInsert.keys.size()) {
                    break;
                }
            }
			//if (obj.returnKey()==135){
				//System.out.println("toInsert index "+toInsert.index+" Going to i "+ toInsert.children.get(i));
			//}
			BTreeNode nextInsert=null;
			if (cache!=null){
				nextInsert = cache.removeObject(new BTreeNode(degree,toInsert.children.get(i)));
			}
			if (nextInsert==null){
				nextInsert = new BTreeNode(TFile.readNodeData(toInsert.children.get(i)), toInsert.children.get(i));
			}
			if (cache!=null){
				cache.addObject(nextInsert);
			}
			boolean recheck=false;
            //check if child is full, if so split
            if (nextInsert.isFull()) {
                parent = toInsert;
                split(nextInsert, i);
				recheck=true;
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
			
			if (recheck){
			i=0;
			//if (obj.returnKey()==4l&&toInsert.index==7){
				//System.out.println("Being compared to "+toInsert.keys.get(i).returnKey());
				//for (int j=0;j<toInsert.keys.size();j++){
			//		System.out.println(toInsert.keys.get(j).returnKey());
			//	}
			//	}
            while (obj.compareTo(toInsert.keys.get(i)) != -1) {
				if (obj.compareTo(toInsert.keys.get(i)) == 0){
                    toInsert.keys.get(i).increaseFrequency();
                    TFile.writeData(toInsert.toByte(), toInsert.index);
                    //DONE
                    return;
				}
				if (obj.returnKey()==4l&&toInsert.index==7){
				//System.out.println("Being compared to "+toInsert.keys.get(i).returnKey());
				}
                i++;
                if (i >= toInsert.keys.size()) {
                    break;
                }
            }
			if (obj.returnKey()==4l){
				//System.out.println("toInsert index "+toInsert.index+" Going to i "+ toInsert.children.get(i));
			}
			nextInsert=null;
			if (cache!=null){
				nextInsert = cache.removeObject(new BTreeNode(degree,toInsert.children.get(i)));
			}
			if (nextInsert==null){
				nextInsert = new BTreeNode(TFile.readNodeData(toInsert.children.get(i)), toInsert.children.get(i));
			}
			if (cache!=null){
				cache.addObject(nextInsert);
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
        //System.out.println("ATTEMPTING NODE SPLIT ON INDEX " + index + " NODE KEY SIZE: " + node.keys.size()+"\nChild size="+node.children.size());
        BTreeNode newNode = new BTreeNode(degree, size++);
        //We want to get (1,2,3) MIDDLE NODE(2)
        //OR (1,2,3,4) MIDDLE LEFT (2)
       // middleIndex = (int) Math.ceil(((double) node.keys.size() / 2) - 1);
        //TreeObject c = root.keys.get(middleIndex);
        //Parent is parent
        //Current node is node
        //other node is newNode
		//if (node.index==5){
			//System.out.println("Splitting node 1..."+"Parent index:"+parent.index+"given index: "+index);
		//}
        //Move keys to new node
        int rKeySize = node.keys.size();
        for (int i = middleIndex + 1; i < rKeySize; i++) {
            newNode.keys.add(node.keys.remove(middleIndex + 1));
        }
        //Move children to new node
        //System.out.println("SPLIT MOVE CHILDREN ON middleindex="+middleIndex);
		int bugFix=node.children.size();
        for (int i = middleIndex + 1; i < bugFix; i++) {
           // if (i < root.children.size()||node.index>10) {
				//if (i < numKeys+1) {
                newNode.children.add(node.children.remove(middleIndex + 1));
           // }
        }

        //Add new node to parent's children
        parent.children.add(index + 1, newNode.index);
		TreeObject temp=node.keys.remove(middleIndex);
		//if (node.index==5){
			//System.out.println("Moving key "+temp.returnKey()+" to parent");
		//}

        //Move middle key to parent
        parent.keys.add(index, temp);
		//if (node.index==5){
		//for (int i=0;i<parent.keys.size();i++){
		//	System.out.println(parent.keys.get(i).returnKey());
		//}
		//}
        TFile.writeData(parent.toByte(), parent.index);
        TFile.writeData(node.toByte(), node.index);
        TFile.writeData(newNode.toByte(), newNode.index);

    }

    /**
	Custom method to split the root while retaining the original root block
	
    */

    public void rootSplit() {
        // 0 1 2 3    size=4, /2 = 2 -1 = 1
        // 0 1 2      size=3, /2 = 1.5 -1 = .5 ciel>1
        //int middleIndex = (int) Math.ceil(((double) root.keys.size() / 2) - 1);
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
        //Update the root's keys
        root.keys.clear();
        root.keys.add(c);

        // 0 1 2 
        //0 1 2 3 

        //Move children to left
        for (int i = 0; i < middleIndex + 1; i++) {
            if (i < root.children.size()) {
                left.children.add(root.children.get(i));
            }
        }
        //Move children to right
        for (int i = middleIndex + 1; i < root.children.size(); i++) {
            if (i < root.children.size()) {
                right.children.add(root.children.get(i));
            }
        }
        root.children.clear();
        root.children.add(left.index);
        root.children.add(right.index);
        TFile.writeData(root.toByte(), root.index);
        TFile.writeData(left.toByte(), left.index);
        TFile.writeData(right.toByte(), right.index);
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
                    //return search.keys.get(i).returnFrequency();
					return search.index;
                }
                if (comp == -1) {
                    //Stop here,
                    if (search.isLeaf()) {
                        return 0;

                    }
					BTreeNode oldSearch=search;
					search=null;
					if (cache!=null){
						search = cache.removeObject(new BTreeNode(degree,oldSearch.children.get(i)));
					}
					if (search==null){
						search =new BTreeNode(TFile.readNodeData(oldSearch.children.get(i)), oldSearch.children.get(i));
					}else{
					}
					if (cache!=null){
						cache.addObject(search);
					}
                    break;

                }
				
                if (i == search.keys.size() - 1) {
					if (search.isLeaf()) {
                        return 0;

                    }
					
					BTreeNode oldSearch=search;
					search=null;
					if (cache!=null){
						search = cache.removeObject(new BTreeNode(degree,oldSearch.children.get(i+1)));
					}
					if (search==null){
						search =new BTreeNode(TFile.readNodeData(oldSearch.children.get(i+1)), oldSearch.children.get(i+1));
					}else{
					}
					if (cache!=null){
						cache.addObject(search);
					}
					
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
		GeneUtility util = new GeneUtility();
		Stack<Integer> nodeIndexStack=new Stack<Integer>();
		Stack<Integer> childIndexStack=new Stack<Integer>();
		nodeIndexStack.push(root.index);
		childIndexStack.push(0);
		boolean ascend=false;
		int ci=0;
		int depth=1;

		//System.out.println("Root children: "+root.children.size());
		while (!nodeIndexStack.empty()){
			int in=nodeIndexStack.pop();
			
			//System.out.println("Traversing to index "+in+"\nchild stack size: "+childIndexStack.size()+" and Depth:"+depth);
			
			//try{
				//Thread.sleep(200);
			//}catch(Exception e){}
			
			
			try{
			}catch(Exception e){}
			
			
			BTreeNode temp = new BTreeNode(TFile.readNodeData(in),in);
			
			//if (in==11){
				
				//for(int i=0;i<temp.keys.size();i++){
			//		System.out.println(temp.keys.get(i).returnKey());
					//System.out.println(temp.children.get(i));
				//}
				
				//System.out.println("IS A LEAF? "+temp.isLeaf()+"At depth "+depth);
				//System.exit(1);
			//}
			
			//System.out.println("Temp index:"+temp.index+"  AND temp children size:"+temp.children.size());
			
			if (temp.isLeaf()){
				//IS A LEAF
				
				//System.out.println("Node is leaf");
				
				childIndexStack.pop();
				//System.out.println("Popping a "+childIndexStack.pop());
				for (int i=0;i<temp.keys.size();i++){
					//Write to file
					//System.out.println(temp.keys.get(i).returnKey());
					String toWrite=temp.keys.get(i).returnFrequency()+"\t"+util.longToSequence(temp.keys.get(i).returnKey(),seqLength);
					bwr.write(toWrite);
					bwr.newLine();
				}
				//Print the parent's next key
				if (nodeIndexStack.empty()){
					//no parent, must be root and a leaf
					//flush the stream
					bwr.flush();
					
					//close the stream
					bwr.close();
					//System.out.println("Exit 0");
					return;
					
				}
				int ip= nodeIndexStack.pop();
				parent = new BTreeNode(TFile.readNodeData(ip),ip);
				nodeIndexStack.push(ip);
				//childIndexStack.push(0);
				//Increment the child index and re add to stacks
				if (ci<parent.children.size()-1){
					bwr.write(parent.keys.get(ci).returnFrequency()+"\t"+util.longToSequence(parent.keys.get(ci).returnKey(),seqLength));
					//bwr.write("\tTHIS IS A MIDDLE KEY CASE");
					//if (parent.keys.get(ci).returnKey()==138){
											//flush the stream
					//bwr.flush();
					
					//close the stream
					//bwr.close();
						//System.exit(1);
					//}
					bwr.newLine();
				//Increment parent's child index
				ci++;
				depth--;
				}else{
					depth--;
					//System.out.println("MARK FOR ASCENSION AT CI="+ci);
					ci=0;
					ascend=true;
				}
				
			}else{
				//IS NOT A LEAF
				
				//System.out.println("Node is not a leaf");
				
				if (!ascend){
					//We will descend
					//Get node to descend into
					if (childIndexStack.empty()){
						//Mission complete
						nodeIndexStack=new Stack<Integer>();
						//System.out.println("Exit 1");
						break;
						
					}
					
					
					int cq=childIndexStack.pop();
					
					if (cq<temp.children.size()){
						//System.out.println("Descend to "+cq);
						depth++;
						// DESCEND
						nodeIndexStack.push(in);		//Re add this node to stack						
						nodeIndexStack.push(temp.children.get(cq)); //Add new child node to stack
						
						//
						//if (cq<temp.children.size()-1){
							//ascend=true;
						cq++;
						childIndexStack.push(cq);
						//System.out.println("Do not re add cq");
						//}
						childIndexStack.push(0);
						}
					else{
						//We have ran out this node
						//if (temp.index==66){

							
						//}
						
						//System.out.println("Functionality needed ||||||||||||||");
						//Alternate ascension i guess
						if (!nodeIndexStack.empty()||depth==2){
						int ip= nodeIndexStack.pop();
						parent = new BTreeNode(TFile.readNodeData(ip),ip);
						nodeIndexStack.push(ip);
						//System.out.println(depth);
						cq=childIndexStack.pop();
						if (depth==2){
							parent=root;
						//	System.out.println(ip+"   WHYYYYYYYYYYYYYYYYY	"+cq);
						//	System.out.println(root.keys.size());
						//	System.out.println(root.keys.get(0).returnKey());
						}
						
						
						
						
						childIndexStack.push(cq);
						if (cq-1<parent.keys.size()){
						bwr.write(parent.keys.get(cq-1).returnFrequency()+"\t"+util.longToSequence(parent.keys.get(cq-1).returnKey(),seqLength));
						//bwr.write("\tTHIS IS AN ELEVATED ASCENSION MIDDLE KEY CASE");
						bwr.newLine();
						}
						}
						
						depth--;
						//childIndexStack.push(cq);
						//ascend=true;
					}
				}else{
				//ASCEND
				//System.out.println("Ascending");
				childIndexStack.pop();
				if (nodeIndexStack.empty()||childIndexStack.empty()){
					//Mission complete
					//if (nodeIndexStack.empty()){
						//System.out.println("Exit 2 no nodes");
					//}
					//if (childIndexStack.empty()){
						//System.out.println("Exit 2 no childs");
					//}
					
					
					nodeIndexStack=new Stack<Integer>();
					//System.out.println("Exit 2");
						break;
						
				}
				
				int ip= nodeIndexStack.pop();
				parent = new BTreeNode(TFile.readNodeData(ip),ip);
				nodeIndexStack.push(ip);
				//System.out.println(depth);
				//if (depth<4){
					//System.out.println(ip+"   WHYYYYYYYYYYYYYYYYY");
				//}
				int cq=childIndexStack.pop();
				childIndexStack.push(cq);
				if (cq-1<parent.children.size()-1){
				bwr.write(parent.keys.get(cq-1).returnFrequency()+"\t"+util.longToSequence(parent.keys.get(cq-1).returnKey(),seqLength));
				//bwr.write("\tTHIS IS AN ASCENSION MIDDLE KEY CASE");
				bwr.newLine();
				}
				
				depth--;

				ascend=false;
				
				
				}
			}
		}
		//flush the stream
		bwr.flush();
		//close the stream
		bwr.close();

    }
	
	public int getSeqLength(){
		return seqLength;
	}
}