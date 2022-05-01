package utility;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Just an object to store list & id. Name is self-explanatory
 */
public class ListAndId<E> implements Serializable {
    @Getter
    @Setter
    private volatile long id;

    private final List<E> list;

    public ListAndId() {
        list = new ArrayList<>();
    }

    public ListAndId(long id, List<E> list) {
        this.id = id;
        this.list = list;
    }

    public synchronized List<E> getList() {
        //yes, calling sort everytime you try to get a list isn't good, but it was the easiest and the least messy way.
        list.sort(null);
        return list;
    }
}
