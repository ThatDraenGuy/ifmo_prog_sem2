package utility;

import java.io.Serializable;
import java.util.*;

public class LimitedCollection<T> extends AbstractCollection<T> implements Serializable {
    private final int size;

    private final Deque<T> deque;

    public LimitedCollection(int size) {
        super();
        this.size = size;
        deque = new ArrayDeque<>(size);
    }

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean add(T e) {
        if (deque.size() == size) {
            deque.pollFirst();
        }
        return deque.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return deque.remove(o);
    }

}
