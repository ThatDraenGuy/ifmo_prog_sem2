package collection.builders;

import collection.classes.*;

import java.lang.reflect.Field;

public class DragonBuilder implements Builder {
    private Long nextId;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    private Color color; //Поле не может быть null
    private DragonType type; //Поле может быть null
    private DragonCharacter character; //Поле не может быть null
    private DragonCave cave; //Поле может быть null

    public DragonBuilder() {
        this.nextId = 1L;
        clear();
    }

    public DragonBuilder(Long nextId) {
        this.nextId = nextId;
        clear();
    }

    @Override
    public Builder put(Field field, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field[] myFields = this.getClass().getDeclaredFields();
        for (Field myField : myFields) {
            if (field.getName().equals(myField.getName())) {
                myField.set(this, value);
                return this;
            }
        }
        throw new NoSuchFieldException("Couldn't find field " + field.getName());
    }

    public Dragon build() {
        nextId++;
        creationDate = java.time.ZonedDateTime.now();
        Dragon newDragon = new Dragon(id, name, coordinates, creationDate, age, color, type, character, cave);
        clear();
        return newDragon;
    }

    private void clear() {
        id = nextId;
        name = null;
        coordinates = null;
        age = null;
        color = null;
        type = null;
        character = null;
        cave = null;
    }

}

