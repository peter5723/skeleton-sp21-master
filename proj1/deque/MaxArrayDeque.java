package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> myComparator;
    //就是使用util, 就是你自己忘记private了
    public MaxArrayDeque(Comparator<T> c) {
        super();
        myComparator = c;
    }

    public T max() {
        T max0 = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T temp = this.get(i);
            int cmd = myComparator.compare(max0, temp);
            if (cmd < 0) {
                max0 = temp;
            }
        }
        return max0;
    }

    public T max(Comparator<T> c) {
        T max0 = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T temp = this.get(i);
            int cmd = c.compare(max0, temp);
            if (cmd < 0) {
                max0 = temp;
            }
        }
        return max0;
    }
}
