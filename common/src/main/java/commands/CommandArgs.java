package commands;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A class that represents arguments command needs to perform its action. As of now it uses separated fields for
 * simple args and complex args but in near future it might change to a single String[] field
 */
public class CommandArgs implements Serializable {
    private String args;
    private HashMap<Field, Object> deconstructedObject;

    public CommandArgs(String args) {
        this.args = args;
    }

    public CommandArgs(HashMap<Field, Object> object) {
        this.deconstructedObject = object;
    }

    public CommandArgs(HashMap<Field, Object> object, String args) {
        this.deconstructedObject = object;
        this.args = args;
    }


    public HashMap<Field, Object> getDeconstructedObject() {
        return deconstructedObject;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

}
