package collection.classes;

import collection.Validator;
import lombok.Setter;

import java.time.ZonedDateTime;

public class DragonFactory implements MainCollectibleFactory<Dragon> {
    @Setter
    private Long nextId;

    public DragonFactory() {
        nextId = 1L;
    }

    public DragonFactory(long nextId) {
        this.nextId = nextId;
    }

    public Dragon getObject(RawCollectible<Dragon> rawCollectible) {
        RawDragon rawDragon = (RawDragon) rawCollectible;
        Dragon newDragon = new Dragon(nextId, rawDragon.getName(), rawDragon.getCoordinates(), ZonedDateTime.now(),
                rawDragon.getAge(), rawDragon.getColor(), rawDragon.getType(), rawDragon.getCharacter(), rawDragon.getCave());
        return handle(newDragon);
    }

    public Dragon getObject(RawCollectible<Dragon> rawCollectible, long id) {
        RawDragon rawDragon = (RawDragon) rawCollectible;
        Dragon newDragon = new Dragon(id, rawDragon.getName(), rawDragon.getCoordinates(), ZonedDateTime.now(),
                rawDragon.getAge(), rawDragon.getColor(), rawDragon.getType(), rawDragon.getCharacter(), rawDragon.getCave());
        return handle(newDragon);
    }

    public Dragon getObject(RawCollectible<Dragon> rawCollectible, long id, ZonedDateTime creationDate) {
        RawDragon rawDragon = (RawDragon) rawCollectible;
        Dragon newDragon = new Dragon(id, rawDragon.getName(), rawDragon.getCoordinates(), creationDate,
                rawDragon.getAge(), rawDragon.getColor(), rawDragon.getType(), rawDragon.getCharacter(), rawDragon.getCave());
        return handle(newDragon);
    }

    private Dragon handle(Dragon dragon) {
        nextId++;
        return dragon;
    }
}
