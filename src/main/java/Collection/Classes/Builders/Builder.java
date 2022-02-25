package Collection.Classes.Builders;

import Collection.Classes.Collectible;

public interface Builder {
    public <T extends Collectible> T build();
}
