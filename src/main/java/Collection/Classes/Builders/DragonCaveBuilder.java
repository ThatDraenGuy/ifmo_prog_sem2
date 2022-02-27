package Collection.Classes.Builders;

import Collection.Classes.DragonCave;

import java.lang.reflect.Field;

public class DragonCaveBuilder implements Builder{
    private Integer depth;
    public DragonCaveBuilder() {
        clear();
    }
    public DragonCaveBuilder depth(int depth) {
        this.depth=depth;
        return this;
    }
    public Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field[] myFields = this.getClass().getDeclaredFields();
        for (Field myField : myFields) {
            if (field.getName().equals(myField.getName())) {
                myField.set(this, value);
                return this;
            }
        }
        throw new NoSuchFieldException("Couldn't find field "+field.getName());
    }
    public void clear() {
        depth= null;
    }
    public DragonCave build() {
        DragonCave cave;
        if (depth==null) {
            cave = null;
        } else {
            cave = new DragonCave(depth);
        }
        clear();
        return cave;
    }
}
