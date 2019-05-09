import java.util.LinkedList;

/**
Cache 

@author Chris Bentley
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
		cacheList.addFirst(obj);
		if (cacheList.size() > cacheSize) {
				cacheList.removeLast();
			}
		
	}
	
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


	public boolean Empty() {
		return false;
	}









}