# BTree Project

## BTree disk layout
The BTree is structured on the disk as follows:

First 12 bytes contain 3 ints containing metadata of sequence length, degree, and root offset

-----------------
The next block is the root BTreeNode, which STAYS constant as the first block. Due to this design root offset is not actually
needed as stored metadata. The root contains its keys and children. No meta data is included in our BTreeNodes. There is no need
for any information to be stored on disk other than keys or children due to the design of the code.

-----------------
Each following block is a BTreeNode, which contains:
1) Keys
2) Children

No meta data needed

-----------------

## Usage and description

### Running
To compile, navigate to the folder containing the files and run
$ javac *.java

To create BTree, run 

$ java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> debug level(optional)
	
Or, if cache is used:
	
$ java GeneBankCreateBTree 0/1(no/with Cache) degree gbk file sequence length
cache size debug level(optional)
  
To run a search query on BTree, use 

$ java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file>  debug level(optional)
	
Or, if cache is used:

$ java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> <cache size>
debug level(optional)

### Additional information

Running the program on test5.gbk with sequence length 31 and no cache (degree: optimal) runs in about 1 minute 3 seconds on tested computer. Running with cache can improve the runtime all the way down to about 50 seconds, however larger cache sizes will actually cause the program to take longer. I suspect this is due to the time it takes to iterate through the cache to check if an object is contained in it.



## Group members
Chris Bentley

Sam Acker

Ben Kang
