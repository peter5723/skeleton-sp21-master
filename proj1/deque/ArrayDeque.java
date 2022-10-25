package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{
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

    private void updateNextFirst(boolean isAdd) {
        if (isAdd) {
            nextFirst = (nextFirst + capacity - 1) % capacity;
        } else {
            nextFirst = (nextFirst + capacity + 1) % capacity;
        }
    }

    private void updateNextLast(boolean isAdd) {
        if (isAdd) {
            nextLast = (nextLast + 1) % capacity;
        } else {
            nextLast = (nextLast + capacity - 1) % capacity;
        }

    }
    private boolean isFull() {
        return numOfElements == capacity;
    }
    /*resize
    adjust the size of the array and nextFirst and nextLast;
     */
    private void resize() {

        T[] tempArray = (T[]) new Object[capacity * 2];
        if (getFirstElementIndex() < getLastElementIndex()) {
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, size());
        } else {
            int length1 = size() - getFirstElementIndex();
            int length2 = size() - length1;
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, length1);
            System.arraycopy(items, 0, tempArray, length1, length2);
        }
        items = tempArray;
        capacity *= 2;
        nextFirst = capacity - 1;
        nextLast = size();
    }
    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resize();
        }
        items[nextFirst] = item; //add elements
        updateNextFirst(true);
        numOfElements++;
    }
    @Override
    public void addLast(T item) {
        if (isFull()) {
            resize();
        }
        items[nextLast] = item;
        updateNextLast(true);
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
    /*
    shrinkArray
     */
    private void shrinkArray() {
        if(size() <= 8) {
            return;
        }
        T[] tempArray = (T[]) new Object[capacity / 2];
        if (getFirstElementIndex() < getLastElementIndex()) {
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, size());
        } else {
            int length1 = size() - getFirstElementIndex();
            int length2 = size() - length1;
            System.arraycopy(items, getFirstElementIndex(), tempArray, 0, length1);
            System.arraycopy(items, 0, tempArray, length1, length2);
        }
        items = tempArray;
        capacity /= 2;
        nextFirst = capacity - 1;
        nextLast = size();
    }
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        if (numOfElements - 1 < capacity * 0.25) {
            shrinkArray();
        }
        numOfElements--;
        T tempItem = items[getFirstElementIndex()];
        items[getFirstElementIndex()] = null;
        updateNextFirst(false);
        return tempItem;
    }
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (numOfElements - 1 < capacity * 0.25) {
            shrinkArray();
        }
        numOfElements--;
        T tempItem = items[getLastElementIndex()];
        items[getLastElementIndex()] = null;
        updateNextLast(false);
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
            if (getFirstElementIndex() <= getLastElementIndex()) {
                return nowPos <= getLastElementIndex();
            } else {
                return (nowPos <= getLastElementIndex()) || (nowPos > getFirstElementIndex()) ||
                        (nowPos == getFirstElementIndex() && firstHasIter == false);
            }
        }
        public T next() {
            T returnItem = items[nowPos];
            if (nowPos == getFirstElementIndex() && firstHasIter == false) {
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
