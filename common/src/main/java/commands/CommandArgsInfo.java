package commands;

import collection.classes.Collectible;
import collection.meta.CollectibleScheme;
import lombok.Getter;

import java.io.Serializable;

public class CommandArgsInfo implements Serializable {
    @Getter
    private final CommandArgsType type;
    @Getter
    private CollectibleScheme targetScheme;

    public CommandArgsInfo(CommandArgsType type) {
        this.type = type;
    }

    public CommandArgsInfo(CommandArgsType type, Class<? extends Collectible> target) {
        this.type = type;
        this.targetScheme = new CollectibleScheme(target);
    }

    public CommandArgsInfo(CommandArgsType type, CollectibleScheme scheme) {
        this.type = type;
        this.targetScheme = scheme;
    }
}
