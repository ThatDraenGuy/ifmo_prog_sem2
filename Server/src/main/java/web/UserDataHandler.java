package web;

import collection.ServerCollectionHandler;
import lombok.Setter;
import message.UserData;

public class UserDataHandler {
    @Setter
    private static ServerCollectionHandler<?> collectionHandler;

    public static void handleUserData(UserHandler userHandler, UserData userData) {
        if (userData == null) {
            return;
        }
        long clientCollectionId = userData.getClientCollectionId();
        if (collectionHandler.isIdBehind(clientCollectionId)) {
            collectionHandler.getHistoryHandler().handleBehindId(userHandler, clientCollectionId);
        }
    }
}
