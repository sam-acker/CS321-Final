import java.util.LinkedList;

/**
Idk how right this was to start with, but quite a few modifications will
be neccesary anyways for the BTree project. 
=======
/*
CS321 HW1
CHRIS BENTLEY
 */



class Cache <T extends Comparable<T>> {

	private LinkedList < T > cacheList;
	private int cacheSize;

	public Cache(int cacheSize) {
		this.cacheSize = cacheSize;
		cacheList = new LinkedList < T > ();
	}

	public boolean getObject(T obj) {
		return cacheList.contains(obj);
	}

	public void addObject(T obj) {
		if (!getObject(obj) && cacheSize != 0) {
			if (cacheList.size() >= cacheSize) {
				cacheList.removeLast();
			}
			cacheList.addFirst(obj);
		}
	}
/*
	public T removeObject(T obj) {
		return cacheList.remove(obj);
	}
	*/
	
	public T removeObject(T comp){
		for (int i=0;i< cacheList.size();i++){
			if (cacheList.get(i).compareTo(comp)==0){
				return cacheList.remove(i);
			}
		}
		return null;
		
	}
	
	

	public void removeLast() {
		cacheList.removeLast();
	}

	public void clearCache() {
		cacheList.clear();
	}


	public void getHitRate() {
	}

	public void getMissRate() {

	}

	public boolean Empty() {
		return false;
	}









}