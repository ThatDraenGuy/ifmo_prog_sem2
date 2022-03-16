package commands;

public abstract class AbstractCommand implements Command {
    protected String name;
    protected String description;
    protected CommandType cmdType;

    public AbstractCommand(String n, String d, CommandType t) {
        this.name = n;
        this.description = d;
        this.cmdType = t;
    }

    public abstract ActionResult action(CommandArgs args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandType getCommandType() {
        return cmdType;
    }
}
