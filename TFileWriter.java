import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
/**
This class will write our .data.[k].t files / interface with the file


*/


class TFileWriter{
	
	
	private int seqLength,degree;
	private String fileName;
	private RandomAccessFile RAFile;
	
	
	
	/**
	Constructor
	fileName will be the name OF THE T FILE
	*/
	
	public TFileWriter(int seqLength, int degree, String fileName){
		this.degree=degree;
		this.seqLength=seqLength;
		try{
		RAFile=new RandomAccessFile(fileName,"rw"); //read-write
		}catch(FileNotFoundException e){
			System.err.println("ERROR: An unexpected file exception has occured");
			return;
		}
		
		
		
	}
	
	
	
	/**
	Constructor WITH CACHE
	fileName will be the name OF THE T FILE
	*/
	
	public TFileWriter(int seqLength, int degree, String fileName, int cacheSize){
		this.degree=degree;
		this.seqLength=seqLength;
		try{
		RAFile=new RandomAccessFile(fileName,"rw"); //read-write
		}catch(FileNotFoundException e){
			System.err.println("ERROR: An unexpected file exception has occured");
			return;
		}
		
		
	}
	
	
	
	/**
	Write to file
	BTREENODE WILL REQUIRE AN INTERNAL CLASS TO PASS TO HERE
	*/
	
	public void writeData(){
		
		
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
	public void writeBOFMetaData(int seqLength, int deg, int rootByteOffset){
		try{
		RAFile.seek(0l);
		RAFile.writeInt(seqLength);
		RAFile.writeInt(deg);
		RAFile.writeInt(rootByteOffset);
		}catch (IOException e){
			System.err.println("ERROR: An unexpected IO error has occured");
			return;
		}
		
	}
	
	
	
	
	/**
	Read from file
	
	*/
	
	public void readData(){
		
		
		
	}
	
	
	
	
	
	
	
}