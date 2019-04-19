import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



class GbkReader{
	
	Scanner reader;
	File file;
	int seqLength;
	StringBuffer geneBuffer;
	StringBuffer seqBuffer;
	int seqBufferIndex=0;
	
	public GbkReader(File file, int seqLength){
		this.file=file;
		this.seqLength=seqLength;
	}
	
	public void gotoOrigin() throws FileNotFoundException{
		reader = new Scanner(file);
		//Look for origin
		while(reader.findInLine("ORIGIN")==null){
			reader.nextLine();
		}
	}
	
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
	
	
	public void fillSeqBuffer(){
		seqBuffer = new StringBuffer();
		int startIndex=seqBufferIndex;
		seqBufferIndex=geneBuffer.indexOf("n",seqBufferIndex);
		if (seqBufferIndex==-1){
			//No null bases detected in this gene
			seqBufferIndex=geneBuffer.length();
		}
		seqBuffer.append(geneBuffer.substring(startIndex,seqBufferIndex));
	}
	
	
	
	public boolean hasNext(){
		return true; 
	}
	
	
	
	public String nextSequence(){
		
		
		return null;
	}
	
	
}