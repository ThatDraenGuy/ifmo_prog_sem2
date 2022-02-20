package cmd.commands;

import Collection.CollectionHandler;
import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

public class Add extends AbstractCommand {
    CollectionHandler collectionHandler;
    public Add(CollectionHandler collectionHandler) {
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        collectionHandler.add(collectionHandler.convertArgs(args));
        return new ActionResult(true, "Successfully added new dragon to collection");
        //TODO exceptions and stuff
    }
}
