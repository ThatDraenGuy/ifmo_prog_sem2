package commands;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import collection.meta.CollectibleModel;
import lombok.Getter;
import utility.QueueWithID;

import java.io.Serializable;
import java.util.Queue;

/**
 * A class that represents arguments command needs to perform its action. As of now it uses separated fields for
 * simple args and complex args but in near future it might change to a single String[] field
 */
public class CommandArgs implements Serializable {
    @Getter
    private final CommandArgsType argsType;
    @Getter
    private String args;
    @Getter
    private String[] longArgs;
    @Getter
    private CollectibleModel collectibleModel;

    public CommandArgs() {
        this.argsType = CommandArgsType.NO_ARGS;
    }

    public CommandArgs(String args) {
        this.args = args;
        this.argsType = CommandArgsType.SIMPLE_ARG;
    }

    public CommandArgs(String[] longArgs) {
        this.args = longArgs[0];
        this.longArgs = longArgs;
        this.argsType = CommandArgsType.LONG_ARG;
    }

    public CommandArgs(CollectibleModel object) {
        this.collectibleModel = object;
        this.argsType = CommandArgsType.COMPLEX_ARG;
    }

    public CommandArgs(String args, CollectibleModel object) {
        this.collectibleModel = object;
        this.args = args;
        this.argsType = CommandArgsType.BOTH_ARG;
    }
}
