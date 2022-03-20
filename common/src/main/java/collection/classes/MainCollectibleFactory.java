package collection.classes;

public interface MainCollectibleFactory<T extends MainCollectible<T>> {
    T getObject(RawCollectible<T> rawCollectible);

    T getObject(RawCollectible<T> rawCollectible, long id);

    void setNextId(Long id);
}
