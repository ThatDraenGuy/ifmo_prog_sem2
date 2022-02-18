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
                "      \"creationDate\": \"2022-02-18T15:41:24.111121122+03:00[Europe/Moscow]\",\n" +
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
                "      \"name\": \"Alduinishe\",\n" +
                "      \"coordinates\": {\n" +
                "        \"x\": 73,\n" +
                "        \"y\": 256\n" +
                "      },\n" +
                "      \"creationDate\": \"2022-02-18T15:41:24.111121122+03:00[Europe/Moscow]\",\n" +
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
        System.out.println(dragonCollection.peek().getName());
        String str = JSONToCollection.saveDragons(dragonCollection);
        System.out.println(str);
    }
    @Test
    public void incorrectString() {
        String incorrect = "{\n" +
                "  \"dragonCollection\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Alduin\",\n" +
                "      \"coordinates\": {\n" +
                "        \"x\": 73,\n" +
                "        \"y\": 256\n" +
                "      },\n" +
                "      \"creationDate\": \"2022-02-18T15:41:24.111121122+03:00[Europe/Moscow]\",\n" +
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
                "      \"name\": \"Alduinishe\",\n" +
                "      \"coordinates\" {\n" +
                "        \"x\": 73,\n" +
                "        \"y\": 256\n" +
                "      },\n" +
                "      \"creationDate\": \"2022-02-18T15:41:24.111121122+03:00[Europe/Moscow]\",\n" +
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
        JSONToCollection.parseString(incorrect);
    }
}