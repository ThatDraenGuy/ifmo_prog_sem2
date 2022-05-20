package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Collectibles_ru_RU extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"id", "id"},
            {"name", "имя"},
            {"coordinates", "координаты"},
            {"age", "возраст"},
            {"color", "цвет"},
            {"type", " тип"},
            {"character", "характер"},
            {"cave", "пещера"},
            {"creationDate", "дата создания"},
            {"owner", "владелец"},
            {"x", "x"},
            {"y", "y"},
            {"depth", "глубина"}
    };
}
