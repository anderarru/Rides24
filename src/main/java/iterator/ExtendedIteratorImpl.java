package iterator;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ExtendedIteratorImpl<T> implements ExtendedIterator<T> {
    private List<T> elements;
    private int currentPosition;

    public ExtendedIteratorImpl(Vector<T> elements) {
        this.elements = elements;
        this.currentPosition = 0;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < elements.size();
    }

    @Override
    public T next() {
        return elements.get(currentPosition++);
    }

    @Override
    public T previous() {
        return elements.get(--currentPosition);
    }

    @Override
    public boolean hasPrevious() {
        return currentPosition > 0;
    }

    @Override
    public void goFirst() {
        currentPosition = 0;
    }

    @Override
    public void goLast() {
        currentPosition = elements.size() - 1;
    }
}
