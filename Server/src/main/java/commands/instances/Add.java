package commands.instances;

import collection.CollectionBridge;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandArgsType;
import exceptions.IncorrectCollectibleTypeException;

/**
 * A command for adding new elements to collection. Receives a deconstructed object on the form of a complex arg and calls
 */
public class Add extends AbstractCommand {
    CollectionBridge<?> collectionBridge;

    public Add(CollectionBridge<?> collectionBridge) {
        super("add", "добавить новый элемент в коллекцию", CommandArgsType.COMPLEX_ARG);
        this.collectionBridge = collectionBridge;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            collectionBridge.add(args.getRawObject());
            return new ActionResult(true, "Successfully added new dragon to collection");
        } catch (IncorrectCollectibleTypeException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
