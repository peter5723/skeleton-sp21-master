package flik;


import org.junit.Test;
import static org.junit.Assert.*;
public class FilkTest {
    @Test
    public void test0() {
        int i = -1000;
        int j = -1000;
        for ( ; i < 10000; i++, j++) {
            assertTrue(Flik.isSameNumber(i, j));
        }
    }
}
