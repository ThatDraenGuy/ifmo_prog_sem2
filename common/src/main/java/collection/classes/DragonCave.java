package collection.classes;

import annotations.NotNull;
import annotations.UserAccessibleField;
import collection.builders.DragonCaveBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
public class DragonCave implements Collectible {
    @UserAccessibleField
    @NotNull
    @Getter
    private int depth;


    @Override
    public String toString() {
        return "is " + this.depth + " meters deep";
    }

}
