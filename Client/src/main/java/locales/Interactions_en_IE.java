package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Interactions_en_IE extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"storageException", "Произошла проблема с сохранением объекта в коллекцию"},
            {"addSuccess", "Объект был успешно добавлен в коллекцию"},
            {"incorrectCollectibleException", "Вами был предосталвен объект неправильного типа"},
            {"clearSuccess", "Все ваши объекты были успешно удалены"},
            {"disconnectSuccess", "Вы были успешно отключены от сервера"},
            {"disconnectFailure", "Произошла ошибка при попытке отключиться от сервера"},
            {"loginSuccess", "Вы успешно вошли в свой аккаунт"},
            {"loginFailure", "Произошла ошибка при попытке войти в аккаунт"},
            {"registerSuccess", "Вы были успешно зарегистрированы"},
            {"accountAccessLevelException", "Уровень доступа вашего аккаунта ниже, чем запрошенный вами"},
            {"usernameTakenException", "Этот логин уже занят"},
            {"wrongAccountDataException", "Неправильный логин/пароль"},
            {"unknownAccountException", "Аккаунта с таким именем не существует"},
            {"elementIdException", "Среди ваших объектов нет объекта с id {0}!"},
            {"removeSuccess", "Объект(ы) был(и) успешно удален(ы)"},
            {"emptyCollectionException", "В коллекции нет элементов!"},
            {"updateSuccess", "Информация об объекте была успешно обновлена"},
            {"connectSuccess", "Вы были успешно подключены к серверу"},
            {"connectFailure", "Не удалось подключиться к серверу"},
            {"invalidAddressException", "Введён невалидный адрес"},
            {"invalidPortException", "Введён невалидный порт"},
            {"unknownHostException", "Не удалось найти хост \"{0}\""}
    };
}
