package deque;

public class ArrayDeque<T> {
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
    public int getNextFirst() {
        return nextFirst;
    }
    public int getNextLast() {
        return nextLast;
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
    public void addFirst(T item) {
        if (isFull()) {
            resize();
        }
        items[nextFirst] = item; //add elements
        updateNextFirst(true);
        numOfElements++;
    }
    public void addLast(T item) {
        if (isFull()) {
            resize();
        }
        items[nextLast] = item;
        updateNextLast(true);
        numOfElements++;
    }

    public boolean isEmpty() {
        return numOfElements == 0;
    }

    public int size() {
        return numOfElements;
    }
    public void printDeque() {
        int first = getFirstElementIndex();
        int last = getLastElementIndex();
        for (int i = first; i != last; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.print("\n");
    }

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
}
