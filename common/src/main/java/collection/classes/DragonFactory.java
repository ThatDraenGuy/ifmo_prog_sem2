package collection.classes;

import collection.Validator;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldModel;
import collection.meta.InputtedValue;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.ValueNotValidException;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class DragonFactory implements MainCollectibleFactory<Dragon> {

    public Dragon getObject(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException {
        long id = getValue("id", collectibleModel, Long.class);
        return getObject(collectibleModel, id);
    }

    public Dragon getObject(CollectibleModel collectibleModel, long id) throws IncorrectCollectibleTypeException {
        String name = getValue("name", collectibleModel, String.class);
        Coordinates coordinates;
        if (collectibleModel.getValues().get("coordinates").getFieldData().isCollectible()) {
            coordinates = getCoordinates(collectibleModel.getValues().get("coordinates").getCollectibleModel());
        } else throw new IncorrectCollectibleTypeException();
        ZonedDateTime creationDate = getValue("creationDate", collectibleModel, ZonedDateTime.class);
        Long age = getValue("age", collectibleModel, Long.class);
        Color color = getValue("color", collectibleModel, Color.class);
        DragonType type = getValue("type", collectibleModel, DragonType.class);
        DragonCharacter character = getValue("character", collectibleModel, DragonCharacter.class);
        DragonCave cave;
        if (collectibleModel.getValues().get("cave").getFieldData().isCollectible()) {
            cave = getCave(collectibleModel.getValues().get("cave").getCollectibleModel());
        } else throw new IncorrectCollectibleTypeException();
        String owner = getValue("owner", collectibleModel, String.class);
        return new Dragon(id, name, coordinates, creationDate,
                age, color, type, character, cave, owner);
    }

    private <T> T getValue(String key, CollectibleModel collectibleModel, Class<T> target) throws IncorrectCollectibleTypeException {
        FieldModel fieldModel = collectibleModel.getValues().get(key);
        Object value = fieldModel.getValue();
        if (!fieldModel.getFieldData().getType().equals(target)) throw new IncorrectCollectibleTypeException();
        return (T) value;
    }

    private Coordinates getCoordinates(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException {
        int x = getValue("x", collectibleModel, int.class);
        long y = getValue("y", collectibleModel, Long.class);
        ZonedDateTime creationDate = getValue("creationDate", collectibleModel, ZonedDateTime.class);
        String owner = getValue("owner", collectibleModel, String.class);
        return new Coordinates(x, y, creationDate, owner);
    }

    private DragonCave getCave(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException {
        int depth = getValue("depth", collectibleModel, int.class);
        ZonedDateTime creationDate = getValue("creationDate", collectibleModel, ZonedDateTime.class);
        String owner = getValue("owner", collectibleModel, String.class);
        return new DragonCave(depth, creationDate, owner);
    }

    public CollectibleModel getModel(Dragon dragon) {
        CollectibleScheme scheme = new CollectibleScheme(dragon.getClass());
        Map<String, InputtedValue> map = new HashMap<>();
        map.put("id", new InputtedValue(dragon.getId()));
        map.put("name", new InputtedValue(dragon.getName()));
        Map<String, InputtedValue> coordsMap = new HashMap<>();
        Coordinates coordinates = dragon.getCoordinates();
        coordsMap.put("x", new InputtedValue(coordinates.getX()));
        coordsMap.put("y", new InputtedValue(coordinates.getY()));
        map.put("coordinates", new InputtedValue(coordsMap));
        map.put("creationDate", new InputtedValue(dragon.getCreationDate()));
        map.put("age", new InputtedValue(dragon.getAge()));
        map.put("color", new InputtedValue(dragon.getColor()));
        map.put("type", new InputtedValue(dragon.getType()));
        map.put("character", new InputtedValue(dragon.getCharacter()));
        Map<String, InputtedValue> caveMap = new HashMap<>();
        DragonCave cave = dragon.getCave();
        caveMap.put("depth", new InputtedValue(cave.getDepth()));
        map.put("cave", new InputtedValue(caveMap));
        map.put("owner", new InputtedValue(dragon.getOwner()));
        try {
            return new CollectibleModel(scheme, map);
        } catch (ValueNotValidException e) {
            e.printStackTrace();
            return null;
        }
    }
}
