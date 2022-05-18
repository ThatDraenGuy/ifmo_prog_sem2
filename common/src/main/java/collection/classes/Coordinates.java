package collection.classes;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserWritable;
import lombok.Getter;

import java.time.ZonedDateTime;

public class Coordinates implements Collectible {
    @UserWritable
    @NotNull
    @Getter
    private final int x;
    @UserWritable
    @NotNull
    @LowerBounded(value = -255)
    @Getter
    private final Long y; //Значение поля должно быть больше -255, Поле не может быть null
    @NotNull
    private final ZonedDateTime creationDate;
    @NotNull
    private final String owner;

    public Coordinates(int x, long y, ZonedDateTime creationDate, String owner) {
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }

}
