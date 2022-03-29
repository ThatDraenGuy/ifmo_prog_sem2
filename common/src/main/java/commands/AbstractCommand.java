package commands;

import lombok.Getter;

public abstract class AbstractCommand implements Command {
    @Getter
    private final CommandData data;

    public AbstractCommand(String name, String desc, CommandArgsType type) {
        this.data = new CommandData(name, desc, type);
    }

    public AbstractCommand(String name, String desc, CommandArgsType type, boolean disconnectionFlag) {
        this.data = new CommandData(name, desc, type, disconnectionFlag);
    }

    public AbstractCommand(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel) {
        this.data = new CommandData(name, desc, type, accessLevel);
    }

    public AbstractCommand(String name, String desc, CommandArgsType type, CommandAccessLevel accessLevel, boolean disconnectionFlag) {
        this.data = new CommandData(name, desc, type, accessLevel, disconnectionFlag);
    }

    public abstract ActionResult action(CommandArgs args);


}
