package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> myComparator;
    //如果报错的话我再自己写一个Comparator接口好了
    //感觉没什么必要, 因为比较器的接口就一句话
    public MaxArrayDeque(Comparator<T> c) {
        super();
        myComparator = c;
    }

    public T max() {
        T max0 = this.get(0);
        for(int i = 1; i < this.size(); i++) {
            T temp = this.get(i);
            int cmd = myComparator.compare(max0, temp);
            if(cmd < 0) {
                max0 = temp;
            }
        }
        return max0;
    }

    public T max(Comparator<T> c) {
        T max0 = this.get(0);
        for(int i = 1; i < this.size(); i++) {
            T temp = this.get(i);
            int cmd = c.compare(max0, temp);
            if(cmd < 0) {
                max0 = temp;
            }
        }
        return max0;
    }
}
