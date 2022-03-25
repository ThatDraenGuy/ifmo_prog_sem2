package commands;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import collection.classes.RawDragon;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A class that represents arguments command needs to perform its action. As of now it uses separated fields for
 * simple args and complex args but in near future it might change to a single String[] field
 */
public class CommandArgs<T extends MainCollectible<T>> implements Serializable {
    @Getter
    private final String args;
    @Getter
    private RawCollectible<T> rawObject;

    public CommandArgs(String args) {
        this.args = args;
    }


    public CommandArgs(String args, RawCollectible<T> object) {
        this.rawObject = object;
        this.args = args;
    }


}