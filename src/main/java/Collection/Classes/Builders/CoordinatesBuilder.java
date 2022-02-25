package Collection.Classes.Builders;

import Collection.Classes.Coordinates;

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
