package Collection.Classes;

public class Dragon implements Comparable<Dragon>{
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

    public String getName() {
        return name;
    }

    public Long getAge() {
        return age;
    }

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

    public DragonType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }
    public java.time.ZonedDateTime getCreationDate() {
        return  creationDate;
    }

    public int compareTo(Dragon dragon) {
        return dragon.getName().length()-this.name.length();
    }
    @Override
    public String toString() {
        return this.id+": "+this.name+", a "+this.age+" years old "+this.color+" "+this.type+" "+this.character+" dragon. He was created at "+this.creationDate.toString()+" and is currently at "+this.coordinates.toString()+". His cave is "+this.cave.toString();
    }
}
