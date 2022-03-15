package collection.classes.builders;

import collection.classes.Collectible;

import java.lang.reflect.Field;

public interface Builder {
    public <T extends Collectible> T build();

    public Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException;
}
