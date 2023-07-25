package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {

    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    BSTNode root = null;
    int size = 0;
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node==null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp>0) {
            return containsKey(node.right, key);
        } else if (cmp<0) {
            return containsKey(node.left, key);
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode node, K key) {
        if (root==null) return null;
        int cmp=key.compareTo(node.key);
        if (cmp>0) {
            return get(node.right, key);
        } else if (cmp<0) {
            return get(node.left, key);
        } else {
            return node.value;
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode node) {
        if (node==null) {
            return 0;
        }
        else {
            return size(node.left) + size(node.right) + 1;
        }
    }

    @Override
    public void put(K key, V value) {
        root =  put(root, key, value);
    }

    private BSTNode put(BSTNode root, K key, V value) {
        if (root==null) {
            return new BSTNode(key, value, null, null);
        }
        int cmp = key.compareTo(root.key);
        if (cmp>0) {
            root.right = put(root.right, key, value);
        } else if (cmp<0) {
            root.left = put(root.left, key, value);
        } else {
            root.value = value;
        }
        return root;
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
