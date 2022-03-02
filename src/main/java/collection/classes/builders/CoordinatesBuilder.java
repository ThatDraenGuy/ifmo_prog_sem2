package collection.classes.builders;

import collection.classes.Coordinates;

import java.lang.reflect.Field;


public class CoordinatesBuilder implements Builder{
    private Integer x;
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null
    public CoordinatesBuilder() {
        clear();
    }
    private void clear() {
        x= null;
        y= null;
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
        throw new NoSuchFieldException("Couldn't find field "+field.getName());
    }

    @Override
    public Coordinates build() {
        Coordinates cords = new Coordinates(x,y);
        clear();
        return cords;
    }
}
