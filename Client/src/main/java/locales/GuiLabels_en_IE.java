package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class GuiLabels_en_IE extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"connectButton", "Connect"},
            {"connectPrompt", "Enter address:"},
            {"connectLabel", "Address:"},
            {"loginButton", "Log in"},
            {"registerButton", "register"},
            {"loginPrompt", "Enter login:"},
            {"passwordPrompt", "Enter password:"},
            {"loginLabel", "Login:"},
            {"passwordLabel", "Password:"},
            {"addButton", "add"},
            {"editButton", "edit"},
            {"deleteButton", "delete"},
            {"cancelButton", "cancel"},
            {"logOutButton", "log out"},
            {"changeServerButton", "change server"},
            {"exitButton", "exit"},
            {"accountLabel", "Current account:"},
            {"serverLabel", "Current server:"},
            {"localeLabel", "Current language:"},
            {"tableTab", "Table"},
            {"visualTab", "Visuals"},
            {"info", "info"}
    };
}
