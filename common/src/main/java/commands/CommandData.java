package commands;

import lombok.Getter;

import java.io.Serializable;

public class CommandData implements Serializable {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final CommandArgsInfo commandArgsInfo;
    @Getter
    private final CommandAccessLevel accessLevel;

    public CommandData(String name, String desc, CommandArgsInfo type) {
        this.name = name;
        this.description = desc;
        this.commandArgsInfo = type;
        this.accessLevel = CommandAccessLevel.USER;
    }

    public CommandData(String name, String desc, CommandArgsInfo type, CommandAccessLevel accessLevel) {
        this.name = name;
        this.description = desc;
        this.commandArgsInfo = type;
        this.accessLevel = accessLevel;
    }

}
