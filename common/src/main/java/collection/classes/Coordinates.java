package collection.classes;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessible;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class Coordinates implements Collectible {
    @UserAccessible
    @NotNull
    @Getter
    private int x;
    @UserAccessible
    @NotNull
    @NonNull
    @LowerBounded(value = -255)
    @Getter
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null



    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }

}
