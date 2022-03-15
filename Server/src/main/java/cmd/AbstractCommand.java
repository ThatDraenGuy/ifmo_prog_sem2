package cmd;

public abstract class AbstractCommand implements Command {
    protected String name;
    protected String description;
    protected CmdType cmdType;

    public AbstractCommand(String n, String d, CmdType t) {
        this.name = n;
        this.description = d;
        this.cmdType = t;
    }

    public abstract ActionResult action(CmdArgs args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CmdType getCmdType() {
        return cmdType;
    }
}
