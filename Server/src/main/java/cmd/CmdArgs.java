package cmd;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A class that represents arguments command needs to perform its action. As of now it uses separated fields for
 * simple args and complex args but in near future it might change to a single String[] field
 */
public class CmdArgs {
    private String args;
    private HashMap<Field, Object> deconstructedObject;

    public CmdArgs(String args) {
        this.args = args;
    }

    public CmdArgs(HashMap<Field, Object> object) {
        this.deconstructedObject = object;
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

    public void setDeconstructedObject(HashMap<Field, Object> deconstructedObject) {
        this.deconstructedObject = deconstructedObject;
    }
}
