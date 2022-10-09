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
        assertEquals(arr1.size(),5);

    }
}
