package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Interactions_nl_NL extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"storageException", "A storage exception has occurred"},
            {"addSuccess", "Successfully added object to collection"},
            {"incorrectCollectibleException", "Object of incorrect type was offered"},
            {"clearSuccess", "All your objects were successfully deleted"},
            {"disconnectSuccess", "You were successfully disconnected"},
            {"disconnectFailure", "An exception occurred while trying to disconnect form the server"},
            {"loginSuccess", "You've successfully logged in"},
            {"loginFailure", "An exception occurred while trying to log into account"},
            {"registerSuccess", "You've been successfully registered"},
            {"accountAccessLevelException", "Your actual access level is lower than the one you requested"},
            {"usernameTakenException", "This username is already taken"},
            {"wrongAccountDataException", "Incorrect username/password"},
            {"unknownAccountException", "Account with this username doesn't exist"},
            {"elementIdException", "There is no object with id {0} in your collectibles!"},
            {"removeSuccess", "Object(s)'ve been successfully deleted"},
            {"emptyCollectionException", "There are to elements in your collection!"},
            {"updateSuccess", "Collectible have been successfully updated"},
            {"connectSuccess", "You have been successfully connected to the server"},
            {"connectFailure", "Couldn't connect to the server"},
            {"invalidAddressException", "Invalid address"},
            {"invalidPortException", "Invalid port"},
            {"unknownHostException", "Couldn't find host \"{0}\""},
            {"commandNonExistentException", "Command \"{0}\" does not exist!"}
    };
}
