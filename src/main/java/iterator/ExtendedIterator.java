package iterator;

import java.util.Iterator;

public interface ExtendedIterator<Object> extends Iterator<Object> {
    Object previous();
    boolean hasPrevious();
    void goFirst();
    void goLast();
}
