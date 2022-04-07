package web;

import collection.ServerCollectionHandler;
import lombok.Setter;
import message.UserData;

public class UserDataHandler {
    @Setter
    private static ServerCollectionHandler<?> collectionHandler;

    public static void handleUserData(UserHandler userHandler, UserData userData) {
        if (userData == null) {
            userHandler.getLogger().debug("NULL-userData!!");
            return; //TODO
        }
        userHandler.getLogger().debug(userData.toString());
        long clientCollectionId = userData.getClientCollectionId();
        if (collectionHandler.isIdBehind(clientCollectionId)) {
            collectionHandler.getHistoryHandler().handleBehindId(userHandler, clientCollectionId);
            userHandler.getLogger().debug("sent changes?");
        }
    }
}
