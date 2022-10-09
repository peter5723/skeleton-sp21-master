package deque;



public class LinkedListDeque<T> {
    private class Node {
        private T item;
        private Node next;
        private Node prev;
        Node(T i, Node n1, Node n2)  {
            item = i;
            next = n1;
            prev = n2;
        }
    }

    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }


    public void addFirst(T item) {
        size += 1;
        Node temp = new Node(item, sentinel.next, sentinel);
        sentinel.next = temp;
        temp.next.prev = temp;
    }

    public void addLast(T item) {
        size += 1;
        Node temp = new Node(item, sentinel, sentinel.prev);
        sentinel.prev = temp;
        temp.prev.next = temp;
    }

    public boolean isEmpty() {
        return sentinel.next == sentinel && sentinel.prev == sentinel;
    }

    public int size() {
        return size;
    }


    private void printDequeNode(Node i) {
        if  (i == sentinel) {
            return;
        }
        System.out.println(i.item + " ");
        printDequeNode(i.next);
    }
    public void printDeque() {
        printDequeNode(sentinel.next);
        System.out.print("\n");
    }
    public T removeFirst() {
        T tempItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        if (size > 0) {
            size--;
        }
        return tempItem;
    }
    public T removeLast() {
        T tempItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        if (size > 0) {
            size--;
        }
        return tempItem;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    private T getRecursiveNode(int index, Node p) {
        if (index == 0) {
            return p.item;
        }
        if (p == sentinel) {
            return null;
        }
        return getRecursiveNode(index - 1, p.next);
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveNode(index, sentinel.next);
    }


}
