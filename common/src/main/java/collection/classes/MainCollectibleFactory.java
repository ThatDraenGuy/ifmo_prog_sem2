package collection.classes;

import java.time.ZonedDateTime;

public interface MainCollectibleFactory<T extends MainCollectible<T>> {
    T getObject(RawCollectible<T> rawCollectible);

    T getObject(RawCollectible<T> rawCollectible, long id);

    T getObject(RawCollectible<T> rawCollectible, long id, ZonedDateTime creationDate);

    void setNextId(Long id);
}
