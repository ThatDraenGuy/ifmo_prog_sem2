package Collection.Classes;

public class Coordinates {
    private int x;
    private long y; //Значение поля должно быть больше -255, Поле не может быть null
    public Coordinates (int x, long y) {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }
}
