package cmd;

public abstract class Command {
    protected String name;
    protected String description;
    protected CmdType cmdType;
    public Command(String n, String d) {
        this.name=n;
        this.description=d;
    }
    public abstract ActionResult action(CmdArgs args);
    public String getName() {
        return name;
    }
    public String getDescription() {
        return  description;
    }

    public CmdType getCmdType() {
        return cmdType;
    }
}
