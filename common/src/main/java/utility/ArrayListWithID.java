package utility;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ArrayListWithID<E> extends ArrayList<E> implements ListWithID<E> {
    @Getter
    @Setter
    long id;

    @Override
    public void incrementId() {
        id++;
    }

    public ArrayListWithID(long id) {
        super();
        this.id = id;
    }
}
