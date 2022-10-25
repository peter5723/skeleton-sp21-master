package tester;
import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.introcs.StdRandom;
public class TestArrayDequeEC {
    @Test
    public void randomTest1() {
        int N = 50000;

        ArrayDequeSolution<Integer> arr1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> arr2 = new StudentArrayDeque<>();
        for (int i = 0; i <= N; i++) {
            int randomNum = StdRandom.uniform(0, 6);
            int testnum = StdRandom.uniform(0, 100);
            if (randomNum == 0) {
                arr1.addLast(testnum);
                arr2.addLast(testnum);
            } else if (randomNum == 1) {
                arr1.addFirst(testnum);
                arr2.addFirst(testnum);
            } else if (randomNum == 2) {
                assertEquals("oh, fuck, your size"+arr2.size()+"not equal to" +
                        "correct size" + arr1.size() ,arr1.size(), arr2.size());
            } else if (randomNum == 3 && arr1.size() > 0) {
                assertEquals("o, your remove first num is not equal to correct num: ",arr1.removeFirst(), arr2.removeFirst());
            } else if (randomNum == 4 && arr1.size() > 0) {
                assertEquals("o, your remove last num is not equal to correct num: ",arr1.removeLast(), arr2.removeLast());
            } else if (randomNum == 5 && arr1.size() > 0) {
                int temp = StdRandom.uniform(0, arr2.size());
                assertEquals("o, your num from get is not equal to correct num: ",arr1.get(temp), arr2.get(temp));
            }
        }
    }

}
