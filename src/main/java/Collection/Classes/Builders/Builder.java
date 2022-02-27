package Collection.Classes.Builders;

import Collection.Classes.Collectible;

import java.lang.reflect.Field;

public interface Builder {
    public <T extends Collectible> T build();
    //TODO think about moving below to abstract class maybe??
    public default Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field[] myFields = this.getClass().getDeclaredFields();
        for (Field myField : myFields) {
            if (field.getName().equals(myField.getName()) && field.getType().equals(myField.getType())) {
                myField.set(this, value);
            } else {
                throw new NoSuchFieldException("Couldn't find field"+field.getName());
            }
        }
        return this;
    }
}
