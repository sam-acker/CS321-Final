
/**
This class will provide some useful tools


*/


class GeneUtility{
	
	
	/**
	Constructor
	
	*/
	
	public GeneUtility(){
		
		
		
		
	}
	
	
	/**
	This class will convert a string gene sequence to a unique long identifier.
	
	*/
	
	public long sequenceToLong(String sequence){
		sequence=sequence.replaceAll("a","00");
		sequence=sequence.replaceAll("t","11");
		sequence=sequence.replaceAll("c","01");
		sequence=sequence.replaceAll("g","10");
		try{
			return Long.parseLong(sequence,2);
		}catch(Exception e){
			System.err.println("Critical error: unreadable dna sequence");
			return 0L;
		}
	}
	
	
	
	
	
	
}