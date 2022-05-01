package commands.instances;

import collection.ServerCollectionHandler;
import commands.*;

import java.io.IOException;

/**
 * A command for saving collection.
 */
public class Save extends AbstractCommand {
    private final ServerCollectionHandler<?> collectionHandler;

    public Save(ServerCollectionHandler<?> collectionHandler) {
        super("save", "сохранить коллекцию в файл", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.DEV);
        this.collectionHandler = collectionHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        try {
            collectionHandler.save();
            return new ActionResult(true, "Successfully saved collection");
        } catch (IOException e) {
            return new ActionResult(false, "An exception occurred: " + e.getMessage());
        }
    }
}
