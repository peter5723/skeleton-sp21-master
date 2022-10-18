package flik;


import org.junit.Test;
import static org.junit.Assert.*;
public class FilkTest {
    @Test
    public void test0() {
        int i = 0;
        int j = 0;
        for( ; i < 10000; i++, j++) {
            assertTrue(Flik.isSameNumber(i, j));
        }
    }
}
