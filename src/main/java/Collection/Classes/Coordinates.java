package Collection.Classes;

import Annotations.LowerBounded;
import Annotations.NotNull;
import Annotations.UserAccessibleField;
import Collection.Classes.Builders.Builder;
import Collection.Classes.Builders.CoordinatesBuilder;

public class Coordinates implements Collectible{
    @UserAccessibleField
    @NotNull
    private int x;
    @UserAccessibleField
    @NotNull
    @LowerBounded(value = -255)
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null
    private static Builder builder = new CoordinatesBuilder();
    public Coordinates (int x, Long y) {
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
    public static Builder getBuilder() {
        return builder;
    }
}
