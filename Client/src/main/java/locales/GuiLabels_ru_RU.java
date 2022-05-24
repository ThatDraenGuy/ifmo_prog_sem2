package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class GuiLabels_ru_RU extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"appName", "Драколл"},
            {"connectButton", "Подключиться"},
            {"connectPrompt", "Введите адрес:"},
            {"connectLabel", "Адрес:"},
            {"loginButton", "Войти"},
            {"registerButton", "зарегистрироваться"},
            {"loginPrompt", "Введите логин:"},
            {"passwordPrompt", "Введите пароль:"},
            {"loginLabel", "Логин:"},
            {"passwordLabel", "Пароль:"},
            {"addButton", "добавить"},
            {"editButton", "редактировать"},
            {"deleteButton", "удалить"},
            {"clearButton", "очистить"},
            {"cancelButton", "отмена"},
            {"logOutButton", "сменить аккаунт"},
            {"changeServerButton", "сменить сервер"},
            {"exitButton", "выйти"},
            {"accountLabel", "Текущий аккаунт:"},
            {"serverLabel", "Текущий сервер:"},
            {"localeLabel", "Текущий язык:"},
            {"tableTab", "Таблица"},
            {"visualTab", "Визуал"},
            {"info", "информация"},
            {"error", "ошибка"},
            {"ownedBy", "Владелец: {0}"}
    };
}
