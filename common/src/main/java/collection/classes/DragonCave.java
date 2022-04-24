package collection.classes;

import annotations.NotNull;
import annotations.UserAccessible;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

public class DragonCave implements Collectible {
    @UserAccessible
    @NotNull
    @Getter
    private int depth;
    @NotNull
    private ZonedDateTime creationDate;

    public DragonCave(int depth, ZonedDateTime creationDate) {
        this.depth = depth;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "is " + this.depth + " meters deep";
    }

}
