package collection.classes;

import collection.meta.CollectibleModel;

public interface MainCollectible<T> extends Collectible, Comparable<T> {

    Long getId();

    Color getColor();

    DragonType getType();

    Long getAge();

    String getOwner();


    int compareTo(CollectibleModel collectibleModel);

    CollectibleModel toModel();
}
