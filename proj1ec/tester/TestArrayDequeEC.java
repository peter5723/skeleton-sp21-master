package tester;
import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.introcs.StdRandom;
public class TestArrayDequeEC {
    @Test
    public void randomTest1() {
        int N = 50000;
        StringBuilder s = new StringBuilder();
        ArrayDequeSolution<Integer> arr1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> arr2 = new StudentArrayDeque<>();
        for (int i = 0; i <= N; i++) {
            int randomNum = StdRandom.uniform(0, 6);
            int testnum = StdRandom.uniform(0, 100);
            if (randomNum == 0) {
                arr1.addLast(testnum);
                arr2.addLast(testnum);
                s.append("addLast("+testnum+")\n");
            } else if (randomNum == 1) {
                arr1.addFirst(testnum);
                arr2.addFirst(testnum);
                s.append("addFirst("+testnum+")\n");
            } else if (randomNum == 2) {
                s.append("size()\n");
                assertEquals(s.toString() ,arr1.size(), arr2.size());
            } else if (randomNum == 3 && arr1.size() > 0) {
                s.append("removeFirst()\n");
                assertEquals(s.toString(),arr1.removeFirst(), arr2.removeFirst());
            } else if (randomNum == 4 && arr1.size() > 0) {
                s.append("removeLast()\n");
                assertEquals(s.toString(),arr1.removeLast(), arr2.removeLast());
            } else if (randomNum == 5 && arr1.size() > 0) {
                int temp = StdRandom.uniform(0, arr2.size());
                s.append("get("+temp+")\n");
                assertEquals(s.toString(),arr1.get(temp), arr2.get(temp));
            }
        }
    }

}
