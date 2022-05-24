package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Collectibles_hu_HU extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"empty", "-"},
            {"id", "id"},
            {"name", "név"},
            {"coordinates", "koordináták"},
            {"age", "kor"},
            {"color", "szín"},
            {"type", "típus"},
            {"character", "karakter"},
            {"cave", "barlang"},
            {"creationDate", "készítés ideje"},
            {"owner", "tulajdonos"},
            {"x", "x"},
            {"y", "y"},
            {"depth", "mélység"},
            {"Color.YELLOW", "sárga"},
            {"Color.BROWN", "barna"},
            {"Color.GREEN", "zöld"},
            {"DragonCharacter.CUNNING", "ravasz"},
            {"DragonCharacter.WISE", "bölcs"},
            {"DragonCharacter.GOOD", "jó"},
            {"DragonType.WATER", "VÍZ"},
            {"DragonType.UNDERGROUND", "FÖLD ALATT"},
            {"DragonType.AIR", "LEVEGŐ"},
            {"DragonType.FIRE", "TŰZ"}
    };
}
