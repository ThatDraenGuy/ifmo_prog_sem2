package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class GuiLabels_nl_NL extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"appName", "Dracoll"},
            {"connectButton", "Aansluiten"},
            {"connectPrompt", "Vul adres in:"},
            {"connectLabel", "Adres:"},
            {"loginButton", "Log in"},
            {"registerButton", "register"},
            {"loginPrompt", "Voer login in:"},
            {"passwordPrompt", "Voer wachtwoord in:"},
            {"loginLabel", "Login:"},
            {"passwordLabel", "Wachtwoord:"},
            {"addButton", "toevoegen"},
            {"editButton", "bewerk"},
            {"deleteButton", "verwijderen"},
            {"clearButton", "doorzichtig"},
            {"filterButton", "filter"},
            {"cancelButton", "annuleren"},
            {"logOutButton", "uitloggen"},
            {"changeServerButton", "verander server"},
            {"exitButton", "uitgang"},
            {"accountLabel", "Lopende rekening:"},
            {"serverLabel", "Huidige server:"},
            {"localeLabel", "Huidige taal:"},
            {"tableTab", "Tafel"},
            {"visualTab", "Beelden"},
            {"info", "info"},
            {"error", "fout"},
            {"ownedBy", "eigendom van: {0}"},
            {"emptyTable", "Geen geschikte objecten gevonden"}
    };
}
