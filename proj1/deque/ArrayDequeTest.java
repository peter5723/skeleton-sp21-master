package deque;
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
}
