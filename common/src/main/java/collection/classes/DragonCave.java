package collection.classes;

import annotations.NotNull;
import annotations.UserAccessible;
import lombok.Builder;
import lombok.Getter;

@Builder
public class DragonCave implements Collectible {
    @UserAccessible
    @NotNull
    @Getter
    private int depth;


    @Override
    public String toString() {
        return "is " + this.depth + " meters deep";
    }

}
