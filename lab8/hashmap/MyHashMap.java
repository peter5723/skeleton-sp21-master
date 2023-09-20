package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int M = 16;
    //num of buckets
    private int N = 0;
    //num of key-value pairs
    //N should less than M*loadFactor for higher efficient.
    private double loadFactor = 0.75;
    //value N/M

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(M);
    }

    public MyHashMap(int initialSize) {
        M = initialSize;
        buckets = createTable(M);


    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        M = initialSize;
        loadFactor = maxLoad;
        N = (int) (M * loadFactor);
        buckets = createTable(M);

    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
        //I use hashset here and it can be others.
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        N = 0;
        buckets = createTable(M);
    }

    private int myHash(K key, int M) {
        return (key.hashCode() & 0x7fffffff) % M;
    }
    @Override
    public boolean containsKey(K key) {
        return get(key)!=null;
    }

    @Override
    public V get(K key) {
        int i = myHash(key,M);
        for(Node node:buckets[i]){
            if(node.key.equals(key)){
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public void put(K key, V value) {
        buckets = put(buckets, key, value);
    }
    private Collection<Node>[] put(Collection<Node>[] buckets, K key , V value) {
        //I think you should judge resize first

        Collection<Node>[] tempBuckets = buckets.clone();
        if ( N >= M * loadFactor) {
            tempBuckets = resize(tempBuckets, 2*M);
        }

        int i = myHash(key, M);
        for (Node node:tempBuckets[i]) {
            if(node.key.equals(key)){
                node.value = value;
                return tempBuckets;
            }
        }
        N++;
        Node node = createNode(key, value);
        tempBuckets[i].add(node);
        return tempBuckets;
    }
    private Collection<Node>[] resize(Collection<Node>[] buckets, int newM) {
        Collection<Node>[] tempBuckets = createTable(newM);
        for(Collection<Node> bucket:buckets) {
            for(Node node:bucket) {
                int i = myHash(node.key, newM);
                tempBuckets[i].add(node);
            }
        }
        M = newM;
        return tempBuckets;
    }
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for(Collection<Node> bucket:buckets) {
            for(Node node:bucket) {
                keys.add(node.key);
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new keyIter();
    }

    private class keyIter implements Iterator<K> {
        private int i = 0;
        private Set<K> keys = keySet();
        private Iterator<K> it = keys.iterator();
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public K next() {
            return it.next();
        }
    }
}
