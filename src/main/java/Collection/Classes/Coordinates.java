package Collection.Classes;

import Annotations.LowerBounded;
import Annotations.NotNull;
import Annotations.UserAccessibleField;

public class Coordinates {
    @UserAccessibleField
    private int x;
    @UserAccessibleField
    @NotNull
    @LowerBounded(value = -255)
    private long y; //Значение поля должно быть больше -255, Поле не может быть null
    public Coordinates (int x, long y) {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }
    @Override
    public String toString() {
        return "{"+this.x+", "+this.y+"}";
    }
}
