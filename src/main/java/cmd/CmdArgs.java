package cmd;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CmdArgs {
    private String args;
    private HashMap<Field, Object> deconstructedObject;
    public CmdArgs(String args) {
        this.args=args;
    }
    public CmdArgs(HashMap<Field,Object> object) {
        this.deconstructedObject=object;
    }

    public HashMap<Field, Object> getDeconstructedObject() {
        return deconstructedObject;
    }

    public String getArgs() {
        return args;
    }
    public void setArgs(String args) {
        this.args=args;
    }
}
