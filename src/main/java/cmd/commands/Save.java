package cmd.commands;

import Collection.CollectionHandler;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Save extends AbstractCommand {
    private CollectionHandler collectionHandler;
    public Save(CollectionHandler collectionHandler) {
       super("save", "сохранить коллекцию в файл", CmdType.NO_ARGS);
       this.collectionHandler=collectionHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        try {
            collectionHandler.save();
            return new ActionResult(true, "Successfully saved collection");
        } catch (IOException | InvocationTargetException |NoSuchMethodException |IllegalAccessException e) {
            return new ActionResult(false, "An exception occurred: "+e.getMessage());
        }
    }
}
