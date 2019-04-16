import java.util.LinkedList;

/*
CS321 HW1
CHRIS BENTLEY
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