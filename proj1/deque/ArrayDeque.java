package deque;

import java.util.Iterator;

public class ArrayDeque <T> implements Deque<T>, Iterable<T>{
    private int numOfElements;
    private int capacity; //the whole length of the array
    private T[] items;
    private int nextFirst;
    private int nextLast;
    public ArrayDeque() {
        this.capacity = 8;
        this.items = (T[]) new Object[8];
        this.nextLast = 4;
        this.nextFirst = 3;
        this.numOfElements = 0;
    }
    private int getFirstElementIndex() {
        return (nextFirst + 1) % capacity;
    }
    private int getLastElementIndex() {
        return (nextLast + capacity - 1) % capacity;
    }
    private boolean isFull() {
        return numOfElements == capacity;
    }

    private void resize(int newCapacity) {
        if (newCapacity < 8) {
            return;
        }
        T[] tempArray = (T[]) new Object[newCapacity];

        if (getFirstElementIndex() < getLastElementIndex()) {
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, size());
        } else {
            int length1 = size() - getFirstElementIndex();
            int length2 = size() - length1;
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, length1);
            System.arraycopy(items, 0, tempArray, length1, length2);
        }
        items = tempArray;
        capacity = newCapacity;
        nextFirst = capacity - 1;
        nextLast = size();
    }

    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resize(capacity*2);
        }
        items[nextFirst] = item; //add elements
        nextFirst = (nextFirst + capacity - 1) % capacity; //update nextFirst
        numOfElements++;
    }
    @Override
    public void addLast(T item) {
        if (isFull()) {
            resize(capacity*2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % capacity; //update nextLast
        numOfElements++;
    }

    @Override
    public int size() {
        return numOfElements;
    }
    @Override
    public void printDeque() {
        int first = getFirstElementIndex();
        int last = getLastElementIndex();
        for (int i = first; i != last; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.print("\n");
    }
    @Override
    public T get(int index) {
        if (index >= numOfElements) {
            return null;
        }
        return items[(nextFirst + 1 + index + capacity) % capacity];
    }
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        if (numOfElements - 1 < capacity * 0.25) {
            resize(capacity / 2);
        }
        numOfElements--;
        T tempItem = items[getFirstElementIndex()];
        items[getFirstElementIndex()] = null;
        nextFirst = (nextFirst + capacity + 1) % capacity;
        return tempItem;
    }
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (numOfElements - 1 < capacity * 0.25) {
            resize(capacity / 2);
        }
        numOfElements--;
        T tempItem = items[getLastElementIndex()];
        items[getLastElementIndex()] = null;
        nextLast = (nextLast + capacity - 1) % capacity;
        return tempItem;
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int nowPos;
        private boolean firstHasIter = false;
        public ArrayDequeIterator() {
            nowPos = getFirstElementIndex();
        }
        public boolean hasNext() {
            if(isEmpty()) {
                return false;
            }
            if (getFirstElementIndex() <= getLastElementIndex()) {
                return nowPos <= getLastElementIndex();
            } else {
                return (nowPos <= getLastElementIndex()) || (nowPos > getFirstElementIndex()) ||
                        (nowPos == getFirstElementIndex() && !firstHasIter);
            }
        }
        public T next() {
            T returnItem = items[nowPos];
            if (nowPos == getFirstElementIndex() && !firstHasIter) {
                firstHasIter = true;
            }
            nowPos = (nowPos + 1) % capacity;
            return returnItem;
        }
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || o.getClass() != this.getClass()) {
            return false;
        }
        ArrayDeque<T> o1 = (ArrayDeque<T>) o;
        if(o1.size() != this.size()) {
            return false;
        }
        for(int i = 0; i < size(); i++) {
            if(!o1.get(i).equals(this.get(i))) {
                return false;
            }
        }
        return true;
    }
}
