package collection.classes;

public interface MainCollectible<T> extends Collectible, Comparable<T> {

    Long getId();

    Color getColor();

    DragonType getType();

    Long getAge();

    int compareTo(RawCollectible<T> rawCollectible);
}
