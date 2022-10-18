package deque;
import jh61b.junit.In;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;
public class MaxArrayDequeTest {
    public static class ComparatorInt implements Comparator<Integer> {
        public int compare(Integer a1, Integer a2) {
            return a1 - a2;
        }
    }
    public static class ComparatorIntf implements Comparator<Integer> {
        public int compare(Integer a1, Integer a2) {
            return a2 - a1;
        }
    }
    public static class ComparatorString implements Comparator<String> {
        public int compare(String a1, String a2) {
            return a1.compareTo(a2);
        }
    }
    @Test
    public void testInt() {
        ComparatorInt ic = new ComparatorInt();
        ComparatorIntf ic2 = new ComparatorIntf();
        MaxArrayDeque<Integer> a1 = new MaxArrayDeque(ic);
        for(int i = 1;i <= 50; i++) {
            a1.addLast(i);
            assertEquals(i, (int) a1.max());
            assertEquals(1, (int) a1.max(ic2));
        }
    }
    @Test
    public void testString() {
        ComparatorString sc = new ComparatorString();
        MaxArrayDeque<String> s1 = new MaxArrayDeque<>(sc);
        s1.addLast("aaa");
        s1.addLast("aaa");
        s1.addLast("bbb");
        s1.addLast("ddda");
        s1.addLast("ddda");
        assertEquals("ddda", (String) s1.max());
    }
}
