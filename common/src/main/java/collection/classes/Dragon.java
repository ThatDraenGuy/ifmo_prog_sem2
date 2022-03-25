package collection.classes;

import annotations.*;
import annotations.CollectibleField;
import lombok.NonNull;


import java.time.ZonedDateTime;

public class Dragon implements MainCollectible<Dragon> {
    @NotNull
    @NonNull
    @LowerBounded
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NonNull
    @NotNull
    @UserAccessible
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @NonNull
    @UserAccessible
    @CollectibleField
    private Coordinates coordinates; //Поле не может быть null
    @NotNull
    @NonNull
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @UserAccessible
    @LowerBounded
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    @NotNull
    @NonNull
    @UserAccessible
    private Color color; //Поле не может быть null
    @UserAccessible
    private DragonType type; //Поле может быть null
    @NotNull
    @NonNull
    @UserAccessible
    private DragonCharacter character; //Поле не может быть null
    @CollectibleField
    @UserAccessible
    private DragonCave cave; //Поле может быть null

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

    public String getName() {
        return name;
    }

    @Override
    public Long getAge() {
        return age;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public DragonCave getCave() {
        return cave;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    @Override
    public DragonType getType() {
        return type;
    }

    @Override
    public Long getId() {
        return id;
    }

    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public int compareTo(Dragon dragon) {
        return this.name.length() - dragon.name.length();
    }

    public int compareTo(RawCollectible rawDragon) {
        return this.name.length() - rawDragon.getName().length();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(id).append(": ").append(name).append(", a ");
        if (age != null) str.append(age).append(" years old ");
        str.append(color).append(" ");
        if (type != null) str.append(type).append(" ");
        str.append(character).append(" dragon. He was created at ").append(creationDate).append(" and is currently at ").append(coordinates);
        if (cave != null) str.append(". His cave is ").append(cave);
        return str.toString();
    }

}