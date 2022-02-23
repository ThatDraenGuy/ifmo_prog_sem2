package cmd.commands;

import Collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

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
        //TODO a "are you sure" prompt
    }
}
