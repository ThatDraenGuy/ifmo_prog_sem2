package security;

import collection.classes.CollectibleFactory;
import collection.meta.CollectibleModel;
import collection.meta.FieldModel;
import commands.CommandAccessLevel;
import exceptions.IncorrectCollectibleTypeException;

public class AccountFactory implements CollectibleFactory<Account> {

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
}
