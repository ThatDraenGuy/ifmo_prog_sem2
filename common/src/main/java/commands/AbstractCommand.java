package commands;

import lombok.Getter;

public abstract class AbstractCommand implements Command {
    @Getter
    private CommandData data;

    public AbstractCommand(String name, String desc, CommandType type) {
        this.data = new CommandData(name, desc, type);
    }

    public abstract ActionResult action(CommandArgs args);


}
