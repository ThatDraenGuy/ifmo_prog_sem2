package utility;

import lombok.Getter;
import lombok.Setter;

import java.util.PriorityQueue;
import java.util.function.Predicate;

public class PriorityQueueWithID<E> extends PriorityQueue<E> implements QueueWithID<E> {
    @Getter
    @Setter
    private long id;

    public void incrementId() {
        id++;
    }
//    @Override
//    public boolean add(E object) {
//        id++;
//        return super.add(object);
//    }
//    @Override
//    public boolean removeIf(Predicate<? super E> filter) {
//        boolean res = super.removeIf(filter);
//        if (res) id++;
//        return res;
//    }
}
