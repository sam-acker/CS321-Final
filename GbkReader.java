import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
This class parses .gbk files into sequences of desried length

THIS FILE MAY RECIEVE FURTHER DEVELOPMENT IF NEEDED

@author Chris Bentley
*/

class GbkReader{
	
	Scanner reader;
	File file;
	int seqLength;
	StringBuffer geneBuffer;
	StringBuffer seqBuffer;
	int seqBufferIndex=0;
	int seqIndex=0;
	/**
	This constructor creates the scanner for the file and establishes some class variables.
	
	*/
	
	public GbkReader(File file, int seqLength)throws FileNotFoundException{
		this.file=file;
		this.seqLength=seqLength;
		reader = new Scanner(file);
	}
	
	/**
	gotoOrigin will simply move the scanner to the next ORIGIN in the gpk file,
	if there is none, then it will return false (instead of true.) 
	
	*/
	public boolean gotoOrigin() {
		
		//Look for origin
		while(reader.findInLine("ORIGIN")==null){
			if (reader.hasNextLine()){
				reader.nextLine();
			}else{
				return false;
			}
		}
		return true;
	}
	/**
	fillGeneBuffer will take an entire gene segment (origin to //) and add it to 
	a stringbuffer. This is done for easy substringing from null base pair values.
	There is no return state because this will only be called once per gotoOrigin
	*/
	
	public void fillGeneBuffer(){
		//Buffer the next string sequence (origin-//)
		seqBufferIndex=0;
		geneBuffer = new StringBuffer();
		String newLine="";
		while(reader.findInLine("//")==null){
			newLine=reader.nextLine();
			//remove the line numbers
			newLine=newLine.replaceAll("[0-9]","");
			newLine=newLine.replaceAll(" ","");
			//add to buffer
			geneBuffer.append(newLine);
		}
	}
	
	/**
	fillSeqBuffer will segment the gene buffer into chunks between null base pair values,
	one at a time into a stringbuffer. 
	True will be returned if any new bases are added, and false if the gene buffer has 
	already been iterated.
	
	*/
	
	public boolean fillSeqBuffer(){
		seqBuffer = new StringBuffer();
		seqIndex=0;
		int startIndex=seqBufferIndex;
		if (seqBufferIndex==geneBuffer.length()){
			return false;
		}
		seqBufferIndex=geneBuffer.indexOf("n",seqBufferIndex);
		//System.out.println(seqBufferIndex+"\n");
		if (seqBufferIndex==-1){
			//No null bases detected in this gene
			seqBufferIndex=geneBuffer.length();
		}
		if (startIndex==seqBufferIndex){
			seqBufferIndex++;
		}else{
			seqBuffer.append(geneBuffer.substring(startIndex,seqBufferIndex));
		}
		// r e c u r s i o n
		if (seqBuffer.length()<seqLength){
			return fillSeqBuffer();
		}else{
		//System.out.print(seqBuffer.toString()+"\n");
		return true;
		}
	}
	
	/**
	hasNext() will return true if there is one or more sequence left in the seqBuffer
	
	*/
	
	public boolean hasNext(){
		if ((seqBuffer.length()-(seqIndex+seqLength))>=seqLength){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	nextSequence will output the next sequence of size seqLength
	
	*/
	
	
	public String nextSequence(){
		seqIndex++;
		return seqBuffer.substring(seqIndex-1,seqIndex-1+seqLength);
	}
	
	
}