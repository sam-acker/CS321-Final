import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



class GbkReader{
	
	Scanner reader;
	File file;
	int seqLength;
	
	public GbkReader(File file, int seqLength){
		this.file=file;
		this.seqLength=seqLength;
	}
	
	public void prepareReader() throws FileNotFoundException{
		//try{
		reader = new Scanner(file);
		//}catch(FileNotFoundException e){
		//	System.err.println("An error has occured opening the file: File not found");
		//	return;
		//}
		//Look for origin
		reader.findInLine("ORIGIN");
		
	}
	
	
	
	
	
}