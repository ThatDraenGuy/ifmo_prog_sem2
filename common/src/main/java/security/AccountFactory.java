package security;

import collection.classes.CollectibleFactory;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldModel;
import collection.meta.InputtedValue;
import commands.CommandAccessLevel;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.ValueNotValidException;

import java.util.HashMap;
import java.util.Map;

public class AccountFactory implements CollectibleFactory<Account> {
    private final CollectibleScheme accountScheme;

    public AccountFactory() {
        this.accountScheme = new CollectibleScheme(Account.class);
    }

    @Override
    public Account getObject(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException {
        String username = getValue("name", collectibleModel, String.class);
        String password = getValue("password", collectibleModel, String.class);
        CommandAccessLevel accessLevel = getValue("accessLevel", collectibleModel, CommandAccessLevel.class);
        if (accessLevel == null) accessLevel = CommandAccessLevel.GUEST;
        return new Account(username, password, accessLevel);
    }


    private <T> T getValue(String key, CollectibleModel collectibleModel, Class<T> target) throws IncorrectCollectibleTypeException {
        FieldModel fieldModel = collectibleModel.getValues().get(key);
        Object value = fieldModel.getValue();
        if (!fieldModel.getFieldData().getType().equals(target)) throw new IncorrectCollectibleTypeException();
        return (T) value;
    }

    public CollectibleModel getModel(Account account) {
        Map<String, InputtedValue> accountMap = new HashMap<>();
        accountMap.put("name", new InputtedValue(account.getName()));
        accountMap.put("password", new InputtedValue(account.getPassword()));
        try {
            return new CollectibleModel(accountScheme, accountMap);
        } catch (ValueNotValidException e) {
            e.printStackTrace();
            return null;
        }
    }
}
