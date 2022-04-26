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
    private final int depth;
    @NotNull
    private final ZonedDateTime creationDate;
    @NotNull
    private final String owner;

    public DragonCave(int depth, ZonedDateTime creationDate, String owner) {
        this.depth = depth;
        this.creationDate = creationDate;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "is " + this.depth + " meters deep";
    }

}
