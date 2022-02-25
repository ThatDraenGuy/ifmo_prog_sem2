package Collection.Classes.Builders;

import Collection.Classes.DragonCave;

public class DragonCaveBuilder implements Builder{
    private Integer depth;
    public DragonCaveBuilder() {
        clear();
    }
    public DragonCaveBuilder depth(int depth) {
        this.depth=depth;
        return this;
    }
    public void clear() {
        depth= null;
    }
    public DragonCave build() {
        DragonCave cave;
        if (depth==null) {
            cave = null;
        } else {
            cave = new DragonCave(depth);
        }
        clear();
        return cave;
    }
}
