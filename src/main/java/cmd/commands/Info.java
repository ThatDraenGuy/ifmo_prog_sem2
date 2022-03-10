package cmd.commands;

import collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

/**
 * A command for displaying information about collection. Invokes {@link CollectionHandler#info()}
 */
public class Info extends AbstractCommand {
    private CollectionHandler collectionHandler;

    public Info(CollectionHandler collectionHandler) {
        super("info", "вывести информацию о коллекции", CmdType.NO_ARGS);

        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        return new ActionResult(true, collectionHandler.info());
    }
}
