package utility;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public List<E> getList() {
        list.sort(null);
        return list;
    }
}
