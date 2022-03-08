package cmd.commands;

import collection.CollectionHandler;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

import java.util.NoSuchElementException;

public class RemoveFirst extends AbstractCommand {
    CollectionHandler collectionHandler;
    public  RemoveFirst(CollectionHandler collectionHandler) {
        super("remove_first", "удалить первый элемент из коллекции", CmdType.NO_ARGS);
        this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.removeFirst();
            return new ActionResult(true, "Successfully deleted first element");
        } catch (NoSuchElementException e) {
            return new ActionResult(false, "There are no elements in the collection!");
        }
    }
}
