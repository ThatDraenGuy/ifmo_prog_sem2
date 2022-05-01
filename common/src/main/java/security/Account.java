package security;

import annotations.UserAccessible;
import collection.classes.Collectible;
import commands.CommandAccessLevel;
import lombok.Getter;


public class Account implements Collectible {
    @Getter
    @UserAccessible
    private final String password;
    @Getter
    @UserAccessible
    private final String name;
    @Getter
    private final CommandAccessLevel accessLevel;

    public Account(String name, String password, CommandAccessLevel accessLevel) {
        this.name = name;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + name + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
