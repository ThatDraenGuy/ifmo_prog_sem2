package cmd.commands;

import Collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

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
