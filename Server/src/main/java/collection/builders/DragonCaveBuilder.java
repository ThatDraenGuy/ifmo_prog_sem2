package collection.builders;

import collection.classes.DragonCave;

import java.lang.reflect.Field;


public class DragonCaveBuilder implements Builder {
    private Integer depth;

    public DragonCaveBuilder() {
        clear();
    }

    @Override
    public Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field[] myFields = this.getClass().getDeclaredFields();
        for (Field myField : myFields) {
            if (field.getName().equals(myField.getName())) {
                myField.set(this, value);
                return this;
            }
        }
        throw new NoSuchFieldException("Couldn't find field " + field.getName());
    }

    public void clear() {
        depth = null;
    }

    public DragonCave build() {
        DragonCave cave = new DragonCave(depth);
        clear();
        return cave;
    }
}
