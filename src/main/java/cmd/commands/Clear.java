package cmd.commands;

import collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

/**
 * A command for clearing the collection. Invokes {@link CollectionHandler#clear()}
 */
public class Clear extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Clear(CollectionHandler collectionHandler) {
        super("clear", "очистить коллекцию", CmdType.NO_ARGS);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        collectionHandler.clear();
        return new ActionResult(true, "Successfully cleared collection");
    }
}
