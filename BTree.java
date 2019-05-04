import java.nio.ByteBuffer;
import java.util.ArrayList;
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

		private ArrayList<TreeObject> keys;

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
			//READ META DATA
			parentIndex=bb.getInt(0);
			//READ KEY DATA
			for (int i=0;i<numKeys;i++){
				keys.add(new TreeObject(bb.getLong((i*12)+4),bb.getInt((i*12)+12)));
				// 0M4K12F16K24F28
			}
			int nIndex=4+(12*numKeys);
			//READ CHILDREN DATA
			for (int j=0;j<numKeys+1;j++){
				children.add(bb.getInt((4*j)+nIndex));
			}
		}




		/**
		returns true if full, false if not full
		*/
		public boolean isFull(){
			if(keys.size() >= numKeys) {
				return true;
			}
			return false;
		}

		/**
		returns true if the node is a leaf
		*/
		public boolean isLeaf() {
			return children == null;
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



			System.out.println(bb.toString());
			//return bb;
			return bb.array();
		}


	}

	//Begin BTree.java class
	private int seqLength,degree,numKeys,blockSize;
	private BTreeNode root;



	/**
	Constructor


	*/
	public BTree(int seqLength,int degree,int blockSize,int cacheSize){
		this.seqLength=seqLength;
		this.degree=degree;
		this.blockSize=blockSize;
		numKeys=2*degree-1;




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
	public BTree(byte[] data){
		ByteBuffer bb = ByteBuffer.allocate(data.length);
		bb.put(data);
		seqLength=bb.getInt(0);
		degree=bb.getInt(4);

		int rootByteOffset=bb.getInt(8);
		byte[] nodeArray= new byte[4096];
		bb.get(nodeArray,rootByteOffset,4096);
		root=new BTreeNode(nodeArray,rootByteOffset);






	}




	/**
	insert into BTree

	*/
	public void insert(TreeObject t){
		if(root == null) {
			root = new BTreeNode(degree, 0);
			root.keys.add(t);
		}

	}


	/**
	Nodes split when full
	*/
	public void split(BTreeNode node, TreeObject t){

		//new nodes for split
		BTreeNode n1 = new BTreeNode(degree, 0);
		BTreeNode n2 = new BTreeNode(degree, 0);

		//Key directly to the left of the middle (E.x. node:[10, 20, 30]  c = 10)
		TreeObject c = node.keys.get(node.keys.size()/2);

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
