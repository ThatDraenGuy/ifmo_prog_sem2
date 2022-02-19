package cmd.commands;

import Collection.CollectionHandler;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

public class Save extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public Save(CollectionHandler collectionHandler) {
       super("save", "сохранить коллекцию в файл", CmdType.NO_ARGS);
       this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        collectionHandler.save();
        return new ActionResult(true, "Successfully saved collection");
        //TODO a false result?
    }
}
