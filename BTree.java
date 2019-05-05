import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.RandomAccessFile; 
import java.io.IOException;
import java.io.FileNotFoundException;
/**
BTree class


*/
class BTree{


	/**

	Node class
	BECAUSE THIS IS A NESTED CLASS, WE CAN KEEP THE VARIABLES PRIVATE BUT STILL DIRECTLY MANIPULATE THEM FROM THIS CLASS

	*/

	class BTreeNode{

		//private boolean root; //if parentIndex==null we can assume root?
		private int parentIndex,index;
		//KEYS

		public ArrayList<TreeObject> keys;

		//CHILDREN
		private ArrayList<Integer> children;


		/**
		Constructor

		*/

		public BTreeNode(int degree,int index){
			this.index=index;
			//children=new int[numKeys+1];
			children=new ArrayList<Integer>(numKeys+1);
			//for (int i=0;i<children.length;i++){
			//	children[i]=-1;
			//}

			//keys=new TreeObject[numKeys];
			keys=new ArrayList<TreeObject>(numKeys);
			//for (int j=0;j<keys.length;j++){
			//	keys[j]=new TreeObject();
			//}

		}

		/**
		Reconstruct a B Tree Node from disk for searching
		I have not tested this method yet
		*/

		public BTreeNode(byte[] data,int index){
			this.index=index;
			ByteBuffer bb = ByteBuffer.allocate(data.length);
			bb.put(data);
			children=new ArrayList<Integer>(numKeys+1);
			keys=new ArrayList<TreeObject>(numKeys);
			//READ META DATA
			parentIndex=bb.getInt(0);
			//READ KEY DATA
			for (int i=0;i<numKeys;i++){
				
				if (bb.getLong((i*12)+4)!=-1){
					keys.add(new TreeObject(bb.getLong((i*12)+4),bb.getInt((i*12)+12)));
					
				}
				
				// 0M4K12F16K24F28
			}
			int nIndex=4+(12*numKeys);
			//READ CHILDREN DATA
			for (int j=0;j<numKeys+1;j++){
				if (bb.getInt((4*j)+nIndex)!=-1){
				children.add(bb.getInt((4*j)+nIndex));
				}
			}
		}




		/**
		returns true if full, false if not full
		*/
		public boolean isFull(){
			System.out.println("NUM KEYS "+numKeys+"  AND KEYS SIZE "+keys.size());
			if(keys.size() >= numKeys) {
				return true;
			}
			return false;
		}

		/**
		returns true if the node is a leaf
		*/
		public boolean isLeaf() {
			if (children.size()==0){
				return true;
			}
			return false;
		}


		/**
		Converts itself into a byte[4096]
		will be passed into TFileWriter for writing to file
		*/
		public byte[] toByte(){
			//public ByteBuffer toByte(){
			//byte[] newData=new byte[4096]
			ByteBuffer bb= ByteBuffer.allocate(blockSize);

			//ADD METADATA

			//TEST
			//parentIndex=69;
			//
			bb.putInt(parentIndex);

			//ADD KEYS
			for (int i=0;i<keys.size();i++){
				bb.putLong(keys.get(i).returnKey());
				bb.putInt(keys.get(i).returnFrequency());
			}
			for (int i=0;i<(numKeys-keys.size());i++){
				bb.putLong(-1l); //invalid keys to represent empty slots
				bb.putInt(-1);
			}



			//ADD CHILDREN
			for (int i=0;i<children.size();i++){
				//try{
				bb.putInt(children.get(i));

				//}catch(Exception e){
					//System.out.println("FATAL:\nKEYS SIZE: "+keys.length+"\nCHILDREN SIZE: "+children.length+"\nAT J= "+j);

				//}


			}
			for (int i=0;i<((numKeys+1)-children.size());i++){
				bb.putInt(-1);

			}



			//System.out.println(bb.toString());
			//return bb;
			return bb.array();
		}


	}

	//Begin BTree.java class
	public int seqLength,degree,numKeys,blockSize,size;
	public BTreeNode root, parent, toInsert;
	private TFileWriter TFile;
	

	/**
	Constructor


	*/
	public BTree(int seqLength,int degree,int blockSize,int cacheSize,String fileName) throws IOException{
		size=0;
		this.seqLength=seqLength;
		this.degree=degree;
		this.blockSize=blockSize;
		numKeys=2*degree-1;
		TFile=new TFileWriter(seqLength,degree,(fileName+"."+seqLength+".t"));
		TFile.writeBOFMetaData(seqLength,degree,12); 
		root=new BTreeNode(degree,size++);
		TFile.writeData(root.toByte(),root.index);
		/*
		//TEST CODE APPEARS TO WORK WELL
		BTreeNode dummy= new BTreeNode(degree,13);
		ByteBuffer nt=dummy.toByte();
		System.out.println(nt.getInt(0));
		System.out.println(nt.getLong(4));
		//
		*/
	}


	/**
	reConstructor from disk
	*/
	public BTree(String filename){
		
		try{
		RandomAccessFile RAFile=new RandomAccessFile(filename,"r");
		
		ByteBuffer bb = ByteBuffer.allocate((int)RAFile.length());
		byte[] data = new byte[(int)RAFile.length()];
		RAFile.read(data);
		bb.put(data);
		seqLength=bb.getInt(0);
		degree=bb.getInt(4);

		int rootByteOffset=bb.getInt(8);
		byte[] nodeArray= new byte[4096];
		bb.get(nodeArray,rootByteOffset,4096);
		root=new BTreeNode(nodeArray,rootByteOffset);



		}catch(Exception e){
			System.err.println("ERROR: An unexpected file exception has occured BTREE RECON");
			return;
		}


	}




	/**
	insert into BTree

	*/
	public void insert(long key) throws IOException{
		
		/*
		To read a btreenode, use BTreeNode x=new BTreeNode(TFile.readNodeData(children[y]),children[y])
		where x is your new node to read and y is the index of the children you are trying to read
		this SHOULD reconstruct the BTreeNode so you can use it as normal, I am still testing these methods
		
		This is assumming no cache, I am still working on cache implementation, although I will likely have the method to call remain the same so you shouldn't
		need to test if we are using cache
		*/
		
		//We could check for null root here, or just init the class by writing an empty root node
		//if(root == null) {
		//	root = new BTreeNode(degree, 0);
		//	root.keys.add(new TreeObject(key));
		//}
		
		//For actual insertion, check if root needs to be split, this is a special case (root creates 2 nodes below it, if root
		//has children, children are split to 2 new root child nodes's children
		
		if (root.isFull()){
			System.out.println("ATTEMPTING ROOT SPLIT");
			rootSplit();
			//return;
		}
		
		
		
		
		//If root is fine, find node to insert to, if full, split then add key
		
		toInsert=root;
		objInsert(new TreeObject(key));
		
		
		
		
		//If not full, add as normal
		
		
		
		

	}
	
	public void objInsert(TreeObject obj) throws IOException{
		//actually insert key
		//System.out.println("REACHED OBJ INSERT");
		
		if (toInsert.isLeaf()){
			//System.out.println("leafCheck");
			//We have found the place to insert
			//figure out correct index to place into
			for (int i=0;i<toInsert.keys.size();i++){
				int comp=obj.compareTo(toInsert.keys.get(i));
				if (comp==-1){
					//System.out.println("Found correct key spot");
					//Correct key spot, insert
					toInsert.keys.add(i,obj);
					TFile.writeData(toInsert.toByte(),toInsert.index);
					//DONE
					return;
					
				}else if (comp==0){
					//Duplicate
					//System.out.println("Found Duplicate");
					toInsert.keys.get(i).increaseFrequency();
					TFile.writeData(toInsert.toByte(),toInsert.index);
					//DONE
					return;
				}
				
				
			}
			//If we reach here we know we need to add key to last spot
			//System.out.println("Found last key spot");
			toInsert.keys.add(obj);
			TFile.writeData(toInsert.toByte(),toInsert.index);
			//DONE
			return;
		}else{

			int i=0;
			while (obj.compareTo(toInsert.keys.get(i))!=-1){
				i++;
				if (i>=toInsert.keys.size()){
					//i--;
					break;
				}
				
			}
			BTreeNode nextInsert=new BTreeNode(TFile.readNodeData(toInsert.children.get(i)),toInsert.children.get(i));
			
			
			//check if child is full, if so split
			if (nextInsert.isFull()){
				parent=toInsert;
				split(nextInsert,i);
				
				//Search toInsert again before going to child
				//
				//
				//
				//
				//
				
				
			}
			toInsert=nextInsert;
			objInsert(obj);
			//recursively call objInsert after setting toInsert to the child
			
			
			
			
			
			
			return;
		}
	}
	
	


	/**
	Nodes split when full
	*/
	public void split(BTreeNode node, int index){

		//Splitting will only create 1 new node (Exception: root splits)
		System.out.println("ATTEMPTING NODE SPLIT ON INDEX "+index);
		BTreeNode newNode = new BTreeNode(degree, size++);

		//We want to get (1,2,3) MIDDLE NODE(2)
		//OR (1,2,3,4) MIDDLE LEFT (2)
		int middleIndex=(int)Math.ceil((root.keys.size()/2)-1);
		TreeObject c = root.keys.get(middleIndex);

		
		//This code is usable just needs reworking
		// Place nodes right of c into new node, left stays in current node
		// c will be given to node's parent node (children split between 2 nodes but no child goes to parent)
		
		//Parent is parent
		//Current node is node
		//other node is newNode
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		//places lesser values in new left node
		int i = 0;
		for(; i < node.keys.size()/2; i++) {
			n1.keys.set(i, node.keys.get(i));
			n1.children.set(i, node.children.get(i));
		}
		n1.children.set(i, node.children.get(i));

		//places greater values in new right node
		i = node.keys.size()/2 + 1;
		int j = 0;
		for(; i < node.keys.size(); i++, j++) {
			n2.keys.set(j, node.keys.get(i));
			n2.children.set(j, node.children.get(i));
		}
		n2.children.set(j, node.children.get(i));

		if(t.compareTo(c) == -1) {

		}

		*/


		TFile.writeData(parent.toByte(),parent.index);
		TFile.writeData(node.toByte(),node.index);
		TFile.writeData(newNode.toByte(),newNode.index);

	}
	
	/**
	Might need tweaking; untested
	
	*/
	
	
	public void rootSplit(){
		// 0 1 2 3    size=4, /2 = 2 -1 = 1
		// 0 1 2      size=3, /2 = 1.5 -1 = .5 ciel>1
		int middleIndex=(int)Math.ceil((root.keys.size()/2)-1);
		TreeObject c = root.keys.get(middleIndex);
		
		BTreeNode left= new BTreeNode(degree, size++);
		BTreeNode right= new BTreeNode(degree, size++);
		
		//Move keys to left node
		for (int i=0;i<middleIndex;i++){
			left.keys.add(root.keys.get(i));
		}
		
		//Move keys to right node
		for (int i=middleIndex+1;i<root.keys.size();i++){
			right.keys.add(root.keys.get(i));
		}
		System.out.println("Processed keys");
		//Update the root's keys
		root.keys.clear();
		root.keys.add(c);
		
		// 0 1 2 
		//0 1 2 3 
		
		System.out.println("Prepare process left child");
		//int middleChildIndex=(int)Math.ceil((root.children.size()/2)-1);
		
		/*
		ARE CHILDREN BEING ADDED CORRECTLY? 
		*/
		//Move children to left
		for (int i=0;i<middleIndex+1;i++){
			if (i<root.children.size()){
				left.children.add(root.children.get(i));
			}
		}
		System.out.println("Processed left children");
		//Move children to right
		for (int i=middleIndex+1;i<root.children.size();i++){
			if (i<root.children.size()){
				right.children.add(root.children.get(i));
			}
		}
		System.out.println("Processed children");
		root.children.clear();
		root.children.add(left.index);
		root.children.add(right.index);
		TFile.writeData(root.toByte(),root.index);
		TFile.writeData(left.toByte(),left.index);
		TFile.writeData(right.toByte(),right.index);
		
		System.out.println("Root split successful");
	}
	

	/**
	Search BTree

	*/
	public int search(long key){


	
	//return FREQUENCY
	return 0;
	}

	/**
	Print the tree to a txt file (if debug==1)
	*/
	public void dumpToTextfile(){



	}




}
