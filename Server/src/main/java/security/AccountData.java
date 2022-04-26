package security;

import commands.CommandAccessLevel;
import lombok.Getter;

public class AccountData {
    @Getter
    private final String passwordHash;
    @Getter
    private final String salt;
    @Getter
    private final CommandAccessLevel accessLevel;

    public AccountData(String passwordHash, String salt, CommandAccessLevel accessLevel) {
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.accessLevel = accessLevel;
    }
}
