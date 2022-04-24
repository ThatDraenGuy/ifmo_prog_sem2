package collection.classes;

import annotations.*;
import annotations.CollectibleField;
import collection.meta.CollectibleModel;
import lombok.Getter;
import lombok.NonNull;


import java.time.ZonedDateTime;

@Getter
public class Dragon implements MainCollectible<Dragon> {
    @NotNull
    @LowerBounded
    private final Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NotNull
    @UserAccessible
    private final String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @UserAccessible
    @CollectibleField
    private final Coordinates coordinates; //Поле не может быть null
    @UserAccessible
    @LowerBounded
    private final Long age; //Значение поля должно быть больше 0, Поле может быть null
    @NotNull
    @UserAccessible
    private final Color color; //Поле не может быть null
    @UserAccessible
    private final DragonType type; //Поле может быть null
    @NotNull
    @UserAccessible
    private final DragonCharacter character; //Поле не может быть null
    @CollectibleField
    @UserAccessible
    private final DragonCave cave; //Поле может быть null
    @NotNull
    private final ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически


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


    public int compareTo(CollectibleModel collectibleModel) {
        String modelName = collectibleModel.getValues().get(name).getValue().toString();
        return this.name.compareTo(modelName);
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
