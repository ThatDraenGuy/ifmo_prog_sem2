package Collection.Classes.Builders;

import Collection.Classes.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


public class DragonBuilder implements Builder{
    private Long nextId;
    //TODO think
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
        this.nextId=1L;
        clear();
    }
    public DragonBuilder(Long nextId) {
        this.nextId=nextId;
        clear();
    }
    public DragonBuilder id(Long id) {
        this.id=id;
        return this;
    }
    public DragonBuilder name(String name) {
        this.name=name;
        return this;
    }
    public DragonBuilder coordinates(Coordinates cords) {
        this.coordinates=cords;
        return this;
    }
    public DragonBuilder age(Long age) {
        this.age=age;
        return this;
    }
    public DragonBuilder color(Color color) {
        this.color=color;
        return this;
    }
    public DragonBuilder type(DragonType type) {
        this.type=type;
        return this;
    }
    public DragonBuilder character(DragonCharacter character) {
        this.character=character;
        return this;
    }
    public DragonBuilder cave(DragonCave cave) {
        this.cave=cave;
        return this;
    }
    public Dragon build() {
        nextId++;
        creationDate=java.time.ZonedDateTime.now();
        Dragon newDragon = new Dragon(id,name,coordinates,creationDate,age,color,type,character,cave);
        clear();
        return newDragon;
        //TODO implement
    }
    private void clear() {
        id=nextId;
        name=null;
        coordinates=null;
        age=null;
        color=null;
        type=null;
        character=null;
        cave=null;
    }

}
