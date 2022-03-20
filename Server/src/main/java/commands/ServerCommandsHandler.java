package commands;

import collection.DragonCollectionHandler;

/**
 * A class needed to handle all the commands. Stores all commands and provides access to them. Stores command history.
 * Accepts ... after performing command's action.
 * Also works as a link between commands and {@link DragonCollectionHandler};
 */
public class ServerCommandsHandler extends CommandsHandler {
    private final DragonCollectionHandler collectionHandler;

    public ServerCommandsHandler(DragonCollectionHandler collectionHandler) {
        super();
        this.collectionHandler = collectionHandler;
    }


    public DragonCollectionHandler getCollectionHandler() {
        return collectionHandler;
    }

}
