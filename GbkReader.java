import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
This class parses .gbk files into sequences of desried length

@author Chris Bentley
*/

class GbkReader {

	Scanner reader;
	File file;
	int seqLength;
	StringBuffer geneBuffer;
	StringBuffer seqBuffer;
	int seqBufferIndex = 0;
	int seqIndex = 0;
	GeneUtility util;
	
	/**
	This constructor creates the scanner for the file and establishes some class variables.
	
	@param file - The .gbk file to process
	@param seqLength - Length of sequence to process
	*/

	public GbkReader(File file, int seqLength) throws FileNotFoundException {
		this.file = file;
		this.seqLength = seqLength;
		reader = new Scanner(file);
		util = new GeneUtility();
	}

	/**
	gotoOrigin will simply move the scanner to the next ORIGIN in the gpk file,
	if there is none, then it will return false (instead of true.) 
	
	@return boolean representing if it has found and moved to an origin
	
	*/
	public boolean gotoOrigin() {
		//Look for origin
		while (reader.findInLine("ORIGIN") == null) {
			if (reader.hasNextLine()) {
				reader.nextLine();
			} else {
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

	public void fillGeneBuffer() {
		//Buffer the next string sequence (origin-//)
		seqBufferIndex = 0;
		geneBuffer = new StringBuffer();
		String newLine = "";
		while (reader.findInLine("//") == null) {
			newLine = reader.nextLine();
			//remove the line numbers
			newLine = newLine.replaceAll("[0-9]", "");
			newLine = newLine.replaceAll(" ", "");
			//add to buffer
			geneBuffer.append(newLine);
		}
	}

	/**
	fillSeqBuffer will segment the gene buffer into chunks between null base pair values,
	one at a time into a stringbuffer. 
	True will be returned if any new bases are added, and false if the gene buffer has 
	already been iterated.
	
	@return boolean representing if there are more bases in gene buffer to process
	
	*/

	public boolean fillSeqBuffer() {
		seqBuffer = new StringBuffer();
		seqIndex = 0;
		int startIndex = seqBufferIndex;
		if (seqBufferIndex == geneBuffer.length()) {
			return false;
		}
		seqBufferIndex = geneBuffer.indexOf("n", seqBufferIndex);
		if (seqBufferIndex == -1) {
			//No null bases detected in this gene
			seqBufferIndex = geneBuffer.length();
		}
		if (startIndex == seqBufferIndex) {
			seqBufferIndex++;
		} else {
			seqBuffer.append(geneBuffer.substring(startIndex, seqBufferIndex));
		}
		// r e c u r s i o n
		if (seqBuffer.length() < seqLength) {
			return fillSeqBuffer();
		} else {
			return true;
		}
	}

	/**
	hasNext() will return true if there is one or more sequence left in the seqBuffer
	
	@return boolean representing if there is another sequence in the seqBuffer
	*/

	public boolean hasNext() {
		if ((seqBuffer.length() - (seqIndex + seqLength)) >= seqLength) {
			return true;
		} else {
			return false;
		}
	}

	/**
	nextSequence will output the next sequence of size seqLength as a long
	
	@return 64-bit long number representing the sequence
	*/

	public long nextSequence() {
		seqIndex++;
		String bpSeq = seqBuffer.substring(seqIndex - 1, seqIndex - 1 + seqLength);
		//System.out.println("PASSING IN: "+bpSeq);
		long out=util.sequenceToLong(bpSeq);
		//if (bpSeq.equals("tttag")){
			//System.out.println(out);
		//}
		return out;
	}

}