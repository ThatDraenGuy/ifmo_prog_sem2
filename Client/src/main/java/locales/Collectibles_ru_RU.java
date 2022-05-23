package locales;

import collection.classes.DragonCharacter;
import lombok.Getter;

import java.util.ListResourceBundle;

public class Collectibles_ru_RU extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"empty", "-"},
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
            {"depth", "глубина"},
            {"Color.YELLOW", "ЖЁЛТЫЙ"},
            {"Color.BROWN", "КОРИЧНЕВЫЙ"},
            {"Color.GREEN", "ЗЕЛЁНЫЙ"},
            {"DragonCharacter.CUNNING", "ХИТРЫЙ"},
            {"DragonCharacter.WISE", "МУДРЫЙ"},
            {"DragonCharacter.GOOD", "ДОБРЫЙ"},
            {"DragonType.WATER", "ВОДЯНОЙ"},
            {"DragonType.UNDERGROUND", "ПОДЗЕМНЫЙ"},
            {"DragonType.AIR", "ВОЗДУШНЫЙ"},
            {"DragonType.FIRE", "ОГНЕННЫЙ"}
    };
}
