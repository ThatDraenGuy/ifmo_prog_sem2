package collection;

import collection.classes.*;
import exceptions.InvalidCollectionException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {
    public static void main(String... args) throws InvalidCollectionException {
        RawDragon.RawDragonBuilder builder = RawDragon.builder();
        RawDragon rawDragon = builder.cave(DragonCave.builder().depth(10).build()).coordinates(Coordinates.builder().x(1).y(2L).build()).age(10L).character(
                DragonCharacter.GOOD
        ).name("Alduin").color(Color.BROWN).type(DragonType.AIR).build();
        JsonHandler jsonHandler = new JsonHandler();
        DragonFactory factory = new DragonFactory();
        Dragon dragon = factory.getObject(rawDragon);
        JSONObject res = jsonHandler.serializeCollectible(dragon);
        System.out.println(res);
        Map<String, Object> dragon1 = jsonHandler.parseCollectible(res);
        Collection<Map<String, Object>> collection = new ArrayList<>();
        collection.add(dragon1);
        System.out.println(dragon1);
        DragonCollectionBuilder builder1 = new DragonCollectionBuilder(factory);
        Collection<Dragon> dragonCollection = builder1.build(collection);
        System.out.println(dragonCollection);
    }
}