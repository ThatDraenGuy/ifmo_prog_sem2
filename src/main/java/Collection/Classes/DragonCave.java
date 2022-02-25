package Collection.Classes;

import Annotations.NotNull;
import Annotations.UserAccessibleField;
import Collection.Classes.Builders.Builder;
import Collection.Classes.Builders.DragonCaveBuilder;

public class DragonCave implements Collectible{
    @UserAccessibleField
    @NotNull
    private int depth;
    private static Builder builder = new DragonCaveBuilder();

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

    public static Builder getBuilder() {
        return builder;
    }
}
