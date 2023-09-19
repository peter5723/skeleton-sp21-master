package hashmap;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

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
        for (int i = 0; i < M; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        M = initialSize;
        buckets = createTable(M);
        for (int i = 0; i < M; i++) {
            buckets[i] = createBucket();
        }

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
        for (int i = 0; i < M; i++) {
            buckets[i] = createBucket();
        }

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
        return null;
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
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        M = N = 0;
        buckets = null;
    }

    private int myHash(K key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }
    @Override
    public boolean containsKey(K key) {
        return get(key)!=null;
    }

    @Override
    public V get(K key) {
        int i = myHash(key);
        for(Node node:buckets[i]){
            if(node.key==key){
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
        int i = myHash(key);
        Collection<Node>[] tempBuckets = buckets.clone();
        //first see whether buckets[i] contains key;
        for (Node node:tempBuckets[i]) {
            if (node.key==key) {
                node.value = value;
                return tempBuckets;
            }
        }

        //not contain
        N++;
        if (N>M*loadFactor) {
            int newM = 2*M;
            resize(tempBuckets, newM);
        }
        else {
            Node node = createNode(key, value);
            tempBuckets[i].add(node);
            return tempBuckets;
        }
    }
    private Collection<Node>[] resize(Collection<int M) {
        Collection<Node>[] tempBuckets = createTable(M);
        for (K key:keySet()){
            tempBuckets.
        }

    }
    @Override
    public Set<K> keySet() {
        return null;
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
        return null;
    }
}
