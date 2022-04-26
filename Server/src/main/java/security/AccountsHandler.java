package security;

import collection.storage.database.DatabaseHandler;
import commands.CommandAccessLevel;
import exceptions.IncorrectAccountDataException;
import exceptions.InvalidAccessLevelException;
import exceptions.StorageException;
import exceptions.UnknownAccountException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class AccountsHandler {
    private final DatabaseHandler databaseHandler;
    private final MessageDigest messageDigest;
    private final String pepper;

    public AccountsHandler(DatabaseHandler databaseHandler) throws NoSuchAlgorithmException {
        this.databaseHandler = databaseHandler;
        this.messageDigest = MessageDigest.getInstance("SHA-512");
        pepper = "AB0b4Su2Syb4Ka<T>";
    }

    public void validate(Account account) throws IncorrectAccountDataException, UnknownAccountException, StorageException, InvalidAccessLevelException {
        Account validated = validate(account.getUsername(), account.getPassword());
        if (!validated.getAccessLevel().equals(account.getAccessLevel()))
            throw new InvalidAccessLevelException("Your account's access level and the one presented by you don't match. Either something went wrong or you're a cheeky little bastard");
    }

    public Account validate(String username, String password) throws UnknownAccountException, StorageException, IncorrectAccountDataException {
        AccountData accountData = databaseHandler.getAccountData(username);
        String inputtedPassword = hash(password, accountData.getSalt());
        System.out.println(username + ": " + inputtedPassword);
        //TODO remove above
        if (inputtedPassword.equals(accountData.getPasswordHash())) {
            //TODO
            return new Account(username, password, accountData.getAccessLevel());
        }
        throw new IncorrectAccountDataException("Wrong username/password");
    }


    private String hash(String password, String salt) {
        String toBeHashed = pepper + password + salt;
        byte[] encoded = messageDigest.digest(toBeHashed.getBytes(StandardCharsets.UTF_8));
        BigInteger bi = new BigInteger(1, encoded);
        return bi.toString(16);
    }

    public boolean checkIfExists(String username) throws StorageException {
        try {
            databaseHandler.getAccountData(username);
            return true;
        } catch (UnknownAccountException e) {
            return false;
        }
    }

    public void register(String username, String password) throws StorageException, IncorrectAccountDataException {
        if (checkIfExists(username)) throw new IncorrectAccountDataException("This username is already taken");
        String salt = generateSalt();
        String hashedPassword = hash(password, salt);
        System.out.println(username + ": " + password + " " + salt + " " + hashedPassword);
        //TODO remove above
        AccountData accountData = new AccountData(hashedPassword, salt, CommandAccessLevel.USER);
        databaseHandler.addAccount(username, accountData);
    }

    private String generateSalt() {
        Random random = new Random();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
