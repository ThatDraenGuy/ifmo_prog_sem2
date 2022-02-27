package Collection.Classes.Builders;

import Collection.Classes.Coordinates;

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
        //TODO think?? wtf
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
    public CoordinatesBuilder x(int x) {
        this.x=x;
        return this;
    }
    public CoordinatesBuilder y(Long y) {
        this.y=y;
        return this;
    }
    @Override
    public Coordinates build() {
        Coordinates cords = new Coordinates(x,y);
        clear();
        return cords;
    }
}
