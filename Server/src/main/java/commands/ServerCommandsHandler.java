package commands;

import collection.CollectionHandler;

/**
 * A class needed to handle all the commands. Stores all commands and provides access to them. Stores command history.
 * Accepts ... after performing command's action.
 */
public class ServerCommandsHandler extends CommandsHandler {
    private final CollectionHandler<?> collectionHandler;

    public ServerCommandsHandler(CollectionHandler<?> collectionHandler) {
        super();
        this.collectionHandler = collectionHandler;
    }


    public CollectionHandler<?> getCollectionHandler() {
        return collectionHandler;
    }

}
