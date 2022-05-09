package utility;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Just an object to store list & id. Name is self-explanatory
 */
public class ListAndId<E> implements Serializable {
    @Getter
    @Setter
    private AtomicLong id;

    private final List<E> list;
    @Getter
    private final Class<E> targetClass;

    public ListAndId(Class<E> targetClass) {
        list = new ArrayList<>();
        this.targetClass = targetClass;
    }

    public ListAndId(long id, List<E> list, Class<E> targetClass) {
        this.id = new AtomicLong(id);
        this.list = list;
        this.targetClass = targetClass;
    }

    public synchronized List<E> getList() {
        //yes, calling sort everytime you try to get a list isn't good, but it was the easiest and the least messy way.
        list.sort(null);
        return list;
    }
}
