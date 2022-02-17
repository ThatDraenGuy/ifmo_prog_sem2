package Collection.Classes;

public class Dragon {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    private Color color; //Поле не может быть null
    private DragonType type; //Поле может быть null
    private DragonCharacter character; //Поле не может быть null
    private DragonCave cave; //Поле может быть null
    public Dragon(Long id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, Long age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
        this.id=id;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.age=age;
        this.color=color;
        this.type=type;
        this.character=character;
        this.cave=cave;
    }
}
