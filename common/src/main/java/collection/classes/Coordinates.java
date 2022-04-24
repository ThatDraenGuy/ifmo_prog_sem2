package collection.classes;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessible;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

public class Coordinates implements Collectible {
    @UserAccessible
    @NotNull
    @Getter
    private int x;
    @UserAccessible
    @NotNull
    @LowerBounded(value = -255)
    @Getter
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null
    @NotNull
    private ZonedDateTime creationDate;

    public Coordinates(int x, long y, ZonedDateTime creationDate) {
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }

}
