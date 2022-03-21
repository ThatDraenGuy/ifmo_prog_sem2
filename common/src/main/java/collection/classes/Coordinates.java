package collection.classes;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessibleField;
import lombok.Builder;
import lombok.Getter;

@Builder
public class Coordinates implements Collectible {
    @UserAccessibleField
    @NotNull
    @Getter
    private int x;
    @UserAccessibleField
    @NotNull
    @LowerBounded(value = -255)
    @Getter
    private Long y; //Значение поля должно быть больше -255, Поле не может быть null



    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }

}
