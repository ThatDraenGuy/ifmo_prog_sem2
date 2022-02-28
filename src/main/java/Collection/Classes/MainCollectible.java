package Collection.Classes;

public interface MainCollectible<T> extends Collectible, Comparable<T> {

    Long getId();

    Color getColor();

    DragonType getType();

    Long getAge();

}
