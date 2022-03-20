package collection.classes;

import annotations.*;
import lombok.Builder;
import lombok.Getter;

@Builder
public class RawDragon implements RawCollectible<Dragon> {
    @NotNull
    @UserAccessibleField
    @Getter
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @UserAccessibleObject
    @Getter
    private Coordinates coordinates; //Поле не может быть null
    @UserAccessibleField
    @LowerBounded
    @Getter
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    @NotNull
    @UserAccessibleEnum
    @Getter
    private Color color; //Поле не может быть null
    @UserAccessibleEnum
    @Getter
    private DragonType type; //Поле может быть null
    @NotNull
    @UserAccessibleEnum
    @Getter
    private DragonCharacter character; //Поле не может быть null
    @UserAccessibleObject
    @Getter
    private DragonCave cave; //Поле может быть null
}
