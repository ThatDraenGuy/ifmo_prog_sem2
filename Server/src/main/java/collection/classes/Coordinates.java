package collection.classes;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessibleField;
import collection.builders.Builder;
import collection.builders.CoordinatesBuilder;

public class Coordinates implements Collectible {
    @UserAccessibleField
    @NotNull
    private int x;
    @UserAccessibleField
    @NotNull
    @LowerBounded(value = -255)
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null
    private static Builder builder = new CoordinatesBuilder();

    public Coordinates(int x, Long y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }

    public static Builder getBuilder() {
        return builder;
    }
}
