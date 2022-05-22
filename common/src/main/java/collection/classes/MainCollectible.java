package collection.classes;

import collection.meta.CollectibleModel;

public interface MainCollectible<T> extends Collectible, Comparable<T> {

    Long getId();

    String getName();

    Color getColor();

    DragonType getType();

    Long getAge();

    String getOwner();

    Object[] getDescriptionArgs();

    int compareTo(CollectibleModel collectibleModel);

    CollectibleModel toModel();
}
