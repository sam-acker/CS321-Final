import java.util.LinkedList;

/**
Idk how right this was to start with, but quite a few modifications will
be neccesary anyways for the BTree project. 

*/


class Cache < T > {

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

    public void removeObject(T obj) {
        cacheList.remove(obj);
    }

    public void clearCache() {
        cacheList.clear();
    }









}