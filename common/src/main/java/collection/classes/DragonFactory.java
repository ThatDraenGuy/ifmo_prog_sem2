package collection.classes;

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
        nextId++;
        return newDragon;
    }

    public Dragon getObject(RawCollectible<Dragon> rawCollectible, long id) {
        RawDragon rawDragon = (RawDragon) rawCollectible;
        return new Dragon(id, rawDragon.getName(), rawDragon.getCoordinates(), ZonedDateTime.now(),
                rawDragon.getAge(), rawDragon.getColor(), rawDragon.getType(), rawDragon.getCharacter(), rawDragon.getCave());
    }
}
