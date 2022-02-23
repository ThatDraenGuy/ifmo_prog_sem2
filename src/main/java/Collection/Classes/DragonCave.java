package Collection.Classes;

import Annotations.UserAccessibleField;

public class DragonCave {
    @UserAccessibleField
    private int depth;
    public DragonCave(int depth) {
        this.depth=depth;
    }

    public int getDepth() {
        return depth;
    }
    @Override
    public String toString() {
        return "is "+this.depth+" meters deep";
    }
}
