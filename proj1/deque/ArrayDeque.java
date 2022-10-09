package deque;

public class ArrayDeque<T> {
    private int numOfElements;
    private int capacity; //the whole length of the array
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private boolean isEmpty;

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
    private void updateNextFirst() {
        nextFirst = (nextFirst + capacity - 1) % capacity;
    }

    private void updateNextLast() {
        nextLast = (nextLast + 1) % capacity;
    }
    private boolean isFull() {
        return numOfElements == capacity;
    }
    /*resize
    adjust the size of the array and nextFirst and nextLast;
     */
    private void resize() {

    }
    public void addFirst(T item) {
        if (isFull()) {
            resize();
        }
        items[nextFirst] = item;
        updateNextFirst();
        numOfElements++;
    }
    public void addLast(T item) {
        if (isFull()) {
            resize();
        }
        items[nextLast] = item;
        updateNextLast();
        numOfElements++;
    }

    public boolean isEmpty() {
        return numOfElements == 0;
    }

    public int size() {
        return numOfElements;
    }
    public void printDeque() {
        int first = (nextFirst + 1) % capacity;
        int last = (nextLast + capacity - 1) % capacity;
        for (int i = first; i != last; i = (i + 1) % capacity) {
            System.out.print(items[i]);
        }
        System.out.print("\n");
    }


}
