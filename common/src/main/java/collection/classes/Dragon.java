package collection.classes;

import annotations.*;
import annotations.CollectibleField;
import lombok.Getter;
import lombok.NonNull;


import java.time.ZonedDateTime;

@Getter
public class Dragon implements MainCollectible<Dragon> {
    @NotNull
    @NonNull
    @LowerBounded
    private final Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NonNull
    @NotNull
    @UserAccessible
    private final String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @NonNull
    @UserAccessible
    @CollectibleField
    private final Coordinates coordinates; //Поле не может быть null
    @NotNull
    @NonNull
    private final ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @UserAccessible
    @LowerBounded
    private final Long age; //Значение поля должно быть больше 0, Поле может быть null
    @NotNull
    @NonNull
    @UserAccessible
    private final Color color; //Поле не может быть null
    @UserAccessible
    private final DragonType type; //Поле может быть null
    @NotNull
    @NonNull
    @UserAccessible
    private final DragonCharacter character; //Поле не может быть null
    @CollectibleField
    @UserAccessible
    private final DragonCave cave; //Поле может быть null

    public Dragon(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.cave = cave;

    }


    public int compareTo(Dragon dragon) {
        return this.name.compareTo(dragon.getName());
    }

    public int compareTo(RawCollectible<Dragon> rawDragon) {
        return this.name.compareTo(rawDragon.getName());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(id).append(": ").append(name).append(", a ");
        if (age != null) str.append(age).append(" years old ");
        str.append(color).append(" ");
        if (type != null) str.append(type).append(" ");
        str.append(character).append(" dragon. He was created at ").append(creationDate).append(" and is currently at ").append(coordinates);
        if (cave != null) str.append(". His cave ").append(cave);
        return str.toString();
    }

}
