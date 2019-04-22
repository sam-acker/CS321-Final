/**
This class will provide some useful tools


*/


class GeneUtility{
	
	
	/**
	Constructor
	
	*/
	
	public GeneUtility(){}
	
	
	/**
	This class will convert a string gene sequence to a unique long identifier.
	
	*/
	
	public long sequenceToLong(String sequence){
		sequence=sequence.replaceAll("a","00");
		sequence=sequence.replaceAll("t","11");
		sequence=sequence.replaceAll("c","01");
		sequence=sequence.replaceAll("g","10");
		System.out.println(sequence);
		try{
			return Long.parseLong(sequence,2);//cvt from base 2
		}catch(Exception e){
			System.err.println("Critical error: unreadable dna sequence");
			return 0L;
		}
	}
	
	/**
	This class will convert a long into a string sequence
	
	*/
	
	public String longToSequence(long number,int seqSize){
		String sequence=Long.toString(number,2); //cvt to base 2
		int j=seqSize-sequence.length();
		String adder="";
		while (j>0){
			adder+="0";
			j--;
		}
		sequence=adder+sequence;
		StringBuffer buff= new StringBuffer();
		int i=2;
		while(i<=sequence.length()){
			String temp=sequence.substring(i-2,i);
			switch(temp){
				
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
			i+=2;
		}
		return buff.toString();
	}
	
	
	
	
}