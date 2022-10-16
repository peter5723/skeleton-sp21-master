package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> Ba = new BuggyAList<>();
        AListNoResizing<Integer> Ra = new AListNoResizing<>();
        Ba.addLast(4);
        Ba.addLast(5);
        Ba.addLast(6);
        Ra.addLast(4);
        Ra.addLast(5);
        Ra.addLast(6);
        assertEquals(Ba.size(), Ra.size());
        assertEquals(Ba.removeLast(), Ra.removeLast());
        assertEquals(Ba.removeLast(), Ra.removeLast());
        assertEquals(Ba.removeLast(), Ra.removeLast());
    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> bugL = new BuggyAList<>();
        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                bugL.addLast(randVal);

            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int bugSize = bugL.size();
                assertEquals(size, bugSize);

            } else if (operationNumber == 2 && L.size() != 0) {
                int temp = L.getLast();
                int bugTemp = bugL.getLast();
                assertEquals(temp, bugTemp);

            } else if (operationNumber == 3 && L.size() != 0) {
                int temp = L.removeLast();
                int bugTemp = bugL.removeLast();
                assertEquals(temp, bugTemp);


            }
        }
    }
}
