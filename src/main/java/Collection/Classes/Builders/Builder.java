package Collection.Classes.Builders;

import Collection.Classes.Collectible;

import java.lang.reflect.Field;

public interface Builder {
    public <T extends Collectible> T build();
    //TODO think about moving below to abstract class maybe??
    public Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException;
}
