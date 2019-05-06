/**
This class will provide some useful tools for dealing with DNA sequences
@author Chris Bentley
*/

class GeneUtility {

	/**
	Constructor
	*/

	public GeneUtility() {}

	/**
	This class will convert a string gene sequence to a unique long identifier.
	@param sequence - a string to be converted to long consisting of a,t,c, and g
	
	@return a long value of the string
	*/

	public long sequenceToLong(String sequence) {
		sequence=sequence.toLowerCase();
		sequence = sequence.replaceAll("a", "00");
		sequence = sequence.replaceAll("t", "11");
		sequence = sequence.replaceAll("c", "01");
		sequence = sequence.replaceAll("g", "10");
		try {
			return Long.parseLong(sequence, 2); //cvt from base 2
		} catch(Exception e) {
			System.err.println("Critical error: unreadable dna sequence");
			return 0L;
		}
	}

	/**
	This class will convert a long into a string sequence
	I believe this is only required for debug dump. Searches will likely convert to a long and then use that key.
	@param number - A long to be converted to DNA
	@param seqSize - an int representing the correct size of the sequence
	
	@return a string consisting of a,t,c, and g.
	
	*/

	public String longToSequence(long number, int seqSize) {
		String sequence = Long.toString(number, 2); //cvt to base 2
		//System.out.println("BINARY "+sequence);
		int j = (seqSize*2) - sequence.length();
		String adder = "";
		//Lossy conversion from binary to long to binary, add back in lost 0's
		while (j > 0) {
			adder += "0";
			j--;
		}
		sequence = adder + sequence;
		//System.out.println("BINARYN "+sequence);
		StringBuffer buff = new StringBuffer();
		int i = 2;
		//Parse binary back to string
		while (i <= sequence.length()) {
			String temp = sequence.substring(i - 2, i);
			switch (temp) {

			case "00":
				buff.append("a");
				break;
			case "11":
				buff.append("t");
				break;
			case "01":
				buff.append("c");
				break;
			case "10":
				buff.append("g");
				break;
			}
			i += 2;
		}
		return buff.toString();
	}

}