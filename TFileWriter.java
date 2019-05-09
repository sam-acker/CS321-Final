import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
/**
This class will write our .data.[k].t files / interface with the file

@author Chris Bentley
*/


class TFileWriter{
	
	
	//private int seqLength,degree;
	private String fileName;
	private RandomAccessFile RAFile;
	private int blockSize;
	
	
	
	/**
	Constructor
	fileName will be the name OF THE T FILE
	*/
	/*
	public TFileWriter(int seqLength, int degree, String fileName){
		//this.degree=degree;
		//this.seqLength=seqLength;
		
		
		try{
			File f=new File(fileName);
			fileName=f.getName();
			
			//System.out.println(fileName);
		RAFile=new RandomAccessFile(fileName,"rw"); //read-write
		}catch(FileNotFoundException e){
			System.err.println("ERROR: An unexpected file exception has occured HERE");
			return;
		}
		
		
		
	}
	*/
	
	public TFileWriter(String fileName){
		//this.degree=degree;
		//this.seqLength=seqLength;
		
		
		try{
			File f=new File(fileName);
			fileName=f.getName();
			
			//System.out.println(fileName);
		RAFile=new RandomAccessFile(fileName,"rw"); //read-write
		}catch(FileNotFoundException e){
			System.err.println("ERROR: An unexpected file exception has occured HERE");
			return;
		}
		
		
		
	}
	
	public void setBlockSize(int blockSize){
		this.blockSize=blockSize;
		//System.out.println(blockSize);
	}
	
	
	
	/**
	Constructor WITH CACHE
	fileName will be the name OF THE T FILE
	*/
	
	public TFileWriter(int seqLength, int degree, String fileName, int cacheSize,int blockSize){
		//this.degree=degree;
		//this.seqLength=seqLength;
		this.blockSize=blockSize;
		try{
		RAFile=new RandomAccessFile(fileName,"rw"); //read-write
		}catch(FileNotFoundException e){
			System.err.println("ERROR: An unexpected file exception has occured ");
			return;
		}
		
		
	}
	
	
	
	
	/**
	Write to file
	BTREENODE WILL REQUIRE AN INTERNAL CLASS TO PASS TO HERE
	*/
	
	public void writeData(byte[] data,int nodeOffset){
		try{
		RAFile.seek((nodeOffset*blockSize)+12);
		RAFile.write(data);
		}catch(IOException e){
			System.err.println("ERROR: An unexpected IO error has occured");
			return;
		}
	}
	
	/**
	Write meta data to the start of the file
	-----------------
	Sequence length
	Degree            
	Root byte offset
	-----------------
	4096 Bytes
	-----------------
	4096 Bytes
	...
	
	*/
	public void writeBOFMetaData(int seqLength, int deg, int rootByteOffset) throws IOException{
		RAFile.seek(0);
		RAFile.writeInt(seqLength);
		RAFile.writeInt(deg);
		RAFile.writeInt(rootByteOffset);
	}
	
	
	
	
	
	
	/**
	Read from file
	Should work - untested
	*/
	
	public byte[] readNodeData(int nodeOffset) throws IOException{
		byte[] nodeArray= new byte[blockSize];
		RAFile.seek((nodeOffset*blockSize)+12);
		RAFile.readFully(nodeArray,0,blockSize);
		return nodeArray;
	}
	
	
	/**
	Returns the meta data in an array
	[0]= sequence length
	[1]= degree
	[2]= root byte offset
	
	*/
	public int[] readBOFMetaData() throws IOException {
		RAFile.seek(0);
		int[] mdata=new int[3];
		mdata[0]=RAFile.readInt();
		mdata[1]=RAFile.readInt();
		mdata[2]=RAFile.readInt();
		return mdata;
	}
	
	
	
	
	
	
	
}