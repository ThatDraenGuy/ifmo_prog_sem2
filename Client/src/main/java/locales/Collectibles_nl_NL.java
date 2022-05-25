package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Collectibles_nl_NL extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"empty", "-"},
            {"id", "id"},
            {"name", "naam"},
            {"coordinates", "coördinaten"},
            {"age", "leeftijd"},
            {"color", "kleur"},
            {"type", " type"},
            {"character", "karakter"},
            {"cave", "grot"},
            {"creationDate", "Aanmaakdatum"},
            {"owner", "baasje"},
            {"x", "x"},
            {"y", "y"},
            {"depth", "diepte"},
            {"Color.YELLOW", "GEEL"},
            {"Color.BROWN", "BRUIN"},
            {"Color.GREEN", "GROENTE"},
            {"DragonCharacter.CUNNING", "SLUW"},
            {"DragonCharacter.WISE", "VERSTANDIG"},
            {"DragonCharacter.GOOD", "GOED"},
            {"DragonType.WATER", "WATER"},
            {"DragonType.UNDERGROUND", "ONDERGRONDS"},
            {"DragonType.AIR", "LUCHT"},
            {"DragonType.FIRE", "VUUR"}
    };
}
