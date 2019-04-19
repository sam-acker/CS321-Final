import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



class GbkReader{
	
	Scanner reader;
	File file;
	int seqLength;
	StringBuffer buffer;
	
	public GbkReader(File file, int seqLength){
		this.file=file;
		this.seqLength=seqLength;
	}
	
	public void prepareReader() throws FileNotFoundException{
		reader = new Scanner(file);
		//Look for origin
		while(reader.findInLine("ORIGIN")==null){
			reader.nextLine();
		}
		
		
		
		//Buffer the next string sequence
		buffer = new StringBuffer();
		String newLine="";
		
		while(reader.findInLine("//")==null){
			newLine=reader.nextLine();
			//remove the line numbers
			newLine=newLine.replaceAll("[0-9]","");
			newLine=newLine.replaceAll(" ","");
			//add to buffer
			buffer.append(newLine);
		}
		System.out.println(buffer.toString());
		
		
		
		
		
	}
	
	
	
	
	
	
	public boolean hasNext(){
		return true; 
	}
	
	
	
	public String nextSequence(){
		
		
		return null;
	}
	
	
}