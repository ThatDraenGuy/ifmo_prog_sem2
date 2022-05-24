package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class GuiLabels_hu_HU extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"appName", "Dracoll"},
            {"connectButton", "Csatlakozás"},
            {"connectPrompt", "Írja be a címet:"},
            {"connectLabel", "Cím:"},
            {"loginButton", "Belépés"},
            {"registerButton", "Regisztráció"},
            {"loginPrompt", "Adja meg a bejelentkezést:"},
            {"passwordPrompt", "Írd be a jelszót:"},
            {"loginLabel", "Belépés:"},
            {"passwordLabel", "Jelszó:"},
            {"addButton", "add"},
            {"editButton", "szerkeszteni"},
            {"deleteButton", "töröl"},
            {"clearButton", "egyértelmű"},
            {"filterButton", "szűrő"},
            {"cancelButton", "megszünteti"},
            {"logOutButton", "Kijelentkezés"},
            {"changeServerButton", "szervert váltani"},
            {"exitButton", "kijárat"},
            {"accountLabel", "Jelenlegi fiók:"},
            {"serverLabel", "Jelenlegi szerver:"},
            {"localeLabel", "Jelenlegi nyelv:"},
            {"tableTab", "asztal"},
            {"visualTab", "Vizuális"},
            {"info", "info"},
            {"error", "hiba"},
            {"ownedBy", "tulajdonában lévő: {0}"},
            {"emptyTable", "Nem található megfelelő objektum"}
    };
}
