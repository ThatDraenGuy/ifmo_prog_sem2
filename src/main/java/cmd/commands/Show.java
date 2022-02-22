package cmd.commands;

import Collection.CollectionHandler;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

public class Show extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public Show(CollectionHandler collectionHandler) {
        super("show", "вывести все элементы коллекции в строковом представлении", CmdType.NO_ARGS);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        String str = collectionHandler.toString();
        if (str!="") {
            return new ActionResult(true, collectionHandler.toString());
        } else {
            return new ActionResult(true, "There are no elements in the collection");
        }
    }
}