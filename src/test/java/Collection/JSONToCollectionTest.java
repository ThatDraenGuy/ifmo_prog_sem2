package Collection;

import Collection.Classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

public class JSONToCollectionTest {
    @Test
    public void correctString() {
        String testString = "{\n" +
                "  \"dragonCollection\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Alduin\",\n" +
                "      \"coordinates\": {\n" +
                "        \"x\": 73,\n" +
                "        \"y\": 256\n" +
                "      },\n" +
                "      \"creationDate\": \"17.02.2022\",\n" +
                "      \"age\": 7800,\n" +
                "      \"color\": \"YELLOW\",\n" +
                "      \"type\": \"AIR\",\n" +
                "      \"character\": \"CUNNING\",\n" +
                "      \"cave\": {\n" +
                "        \"depth\": 150\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Alduin\",\n" +
                "      \"coordinates\" {\n" +
                "        \"x\": 73,\n" +
                "        \"y\": 256\n" +
                "      },\n" +
                "      \"creationDate\": \"17.02.2022\",\n" +
                "      \"age\": 7800,\n" +
                "      \"color\": \"YELLOW\",\n" +
                "      \"type\": \"AIR\",\n" +
                "      \"character\": \"CUNNING\",\n" +
                "      \"cave\": {\n" +
                "        \"depth\": 150\n" +
                "      }\n" +
                "    },\n" +
                "    ]\n" +
                "}";
        java.util.PriorityQueue<Dragon> dragonCollection = JSONToCollection.parseString(testString);
        Coordinates coords = new Coordinates(73, 256);
        DragonCave cave = new DragonCave(150);
        Dragon dragon1 = new Dragon(1L, "Alduin", coords, null, 7800L, Color.YELLOW, DragonType.AIR, DragonCharacter.CUNNING, cave);
        Dragon dragon2 = new Dragon(1L, "Alduin", coords, null, 7800L, Color.YELLOW, DragonType.AIR, DragonCharacter.CUNNING, cave);
        java.util.PriorityQueue<Dragon> testCollection = new PriorityQueue<>();
        testCollection.add(dragon1);
        testCollection.add(dragon2);
        Assertions.assertEquals(dragonCollection, testCollection);
    }
}