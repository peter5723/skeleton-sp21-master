package deque;
import edu.princeton.cs.algs4.StdRandom;
import jh61b.junit.In;
import org.junit.Test;
import static org.junit.Assert.*;
public class ArrayDequeTest {


    @Test
    public void testAddNotFull() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        arr1.addFirst(1);
        arr1.addFirst(2);
        arr1.addFirst(3);
        arr1.addFirst(4);
        arr1.addFirst(5);
        assertEquals(arr1.size(), 5);
        for (int i = 0; i <= 4; i++){
            assertEquals(5 - i, (int) arr1.get(i));
        }
    }

    @Test
    public void testGet() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for (int i = 0; i < 6; i++) {
            arr1.addLast(i);
        }
        assertEquals(6, arr1.size());
        for (int i = 0; i < 6; i++) {
            assertEquals(i, (int) arr1.get(i));
        }

    }
    @Test
    public void testWithResize1() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for (int i = 0; i < 8; i++) {
            arr1.addLast(i);
        }
        assertEquals(8, arr1.size());
        for (int i = 0; i < 8; i++) {
            assertEquals(i, (int) arr1.get(i));
        }
    }
    @Test
    public void testWithResize2() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for (int i = 0; i < 36; i++) {
            arr1.addLast(i);
        }
        assertEquals(36, arr1.size());
        for (int i = 0; i < 36; i++) {
            assertEquals(i, (int) arr1.get(i));
        }
    }

    @Test
    public void testRemove() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for (int i = 0; i < 100; i++) {
            arr1.addLast(i);
        }
        assertEquals(100, arr1.size());
        for (int i = 0; i <= 96; i++) {
            assertEquals(i, (int) arr1.removeFirst());
        }
        assertEquals(99, (int) arr1.removeLast());
        assertEquals(98, (int) arr1.removeLast());
        assertEquals(97, (int) arr1.removeLast());
        assertEquals(null, arr1.removeLast());
    }

    @Test
    public void testRandom() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        arr1.addFirst(1);
        arr1.addLast(2);
        arr1.addFirst(3);
        assertEquals(3, (int) arr1.removeFirst());
        assertEquals(2, (int) arr1.removeLast());
        assertEquals(1, (int) arr1.get(0));
        assertEquals(1, (int) arr1.removeLast());
        assertEquals(null, arr1.removeFirst());
        for (int i=0; i<10; i++) {
            arr1.addFirst(i);
        }
        for (int i=0; i<9; i++) {
            assertEquals(9 - i, (int) arr1.get(i));
        }
        assertEquals(10, arr1.size());
        arr1.printDeque();
    }

    @Test
    public void testRemove2(){
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for(int i=0; i<=7; i++){
            arr1.addFirst(i);
        }
        for(int i=0; i<=7; i++){
            assertEquals(7-i, (int)arr1.removeFirst());
        }
        for (int i = 0; i <= 8; i++) {
            arr1.addLast(i);
        }
        for (int i = 0; i <= 8; i++) {
            assertEquals(i, (int)arr1.removeFirst());
        }
    }

    @Test
    public void testIterator() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        for(int i=0; i<=7; i++){
            arr1.addLast(i);
        }
        int i = 0;
        for(Integer item:arr1){
            assertEquals((int)item, (int)arr1.get(i));

            i++;
        }

        ArrayDeque<Integer> arr2 = new ArrayDeque();
        int j = 0;
        for(int v=0; v<=1000; v++){
            arr2.addLast(v);
        }
        for(Integer item:arr2){
            assertEquals((int)item, (int)arr2.get(j));

            j++;
        }
    }
    @Test
    public void testEqual() {
        ArrayDeque<Integer> arr1 = new ArrayDeque();
        assertTrue(!arr1.equals(null));
        ArrayDeque<Integer> arr2 = new ArrayDeque();
        for(int i=0; i<=7; i++){
            arr1.addLast(i);
            arr2.addLast(i);
        }
        assertTrue(arr1.equals(arr2));

    }
    @Test
    public void randomTest() {
        int N = 50000;
        ArrayDeque<Integer> L = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            int randVal = StdRandom.uniform(0, 100);
            switch (operationNumber) {
                case 0:
                    L.addLast(randVal);
                    break;
                case 1:
                    L.addFirst(randVal);
                    break;
                case 2:
                    L.get(0);
                    break;
                case 3:
                    L.removeLast();
                    break;
                case 4:
                    L.removeFirst();
                    break;
            }
        }
    }
    private class Dog {
        public String name;
        public int size;
        public Dog(String name, int size) {
            this.name = name;
            this.size = size;
        }
        public boolean equals(Object o) {
            if (!(o instanceof Dog)) {
                return false;
            }
            Dog o1 = (Dog) o;
            return this.size == o1.size && this.name.equals(o1.name);
        }
    }
    @Test
    public void testEqualDog() {
        ArrayDeque<Dog> arr1 = new ArrayDeque<>();
        ArrayDeque<Dog> arr2 = new ArrayDeque<>();
        Dog dog1 = new Dog("lan", 5);
        Dog dog2 = new Dog("lan", 5);
        Dog dog3 = new Dog("lan", 5);
        Dog dog4 = new Dog("lan", 5);
        arr1.addLast(dog1);
        arr1.addLast(dog2);
        arr2.addLast(dog3);
        arr2.addLast(dog4);
        assertTrue(arr1.equals(arr2));
    }
}
