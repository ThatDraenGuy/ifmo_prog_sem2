package gui;

import app.Controllers;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.InputtedValue;
import commands.ActionResult;
import commands.CommandAccessLevel;
import commands.CommandArgs;
import exceptions.ValueNotValidException;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import security.Account;
import security.AccountFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommandService {
    private static final AccountFactory accountFactory = new AccountFactory();

    public static Service<Void> getNoArgs(String command, Consumer<ActionResult> handler) {
        return getService(command, handler, CommandArgs::new);
    }

    public static Service<Void> getSimpleArgs(String command, Consumer<ActionResult> handler, Supplier<String> simpleArgSupplier) {
        return getService(command, handler, () -> new CommandArgs(simpleArgSupplier.get()));
    }

    public static Service<Void> getComplexArgs(String command, Consumer<ActionResult> handler, Supplier<Map<String, String>> mapSupplier) {
        return getService(command, handler, () -> {
            try {
                return new CommandArgs(build(mapSupplier.get()));
            } catch (ValueNotValidException e) {
                return new CommandArgs();
            }
        });
    }

    public static Service<Void> getBothArgs(String command, Consumer<ActionResult> handler, Supplier<String> simpleArgSupplier, Supplier<Map<String, String>> mapSupplier) {
        return getService(command, handler, () -> {
            try {
                return new CommandArgs(simpleArgSupplier.get(), build(mapSupplier.get()));
            } catch (ValueNotValidException e) {
                return new CommandArgs(simpleArgSupplier.get());
            }
        });
    }

    public static Service<Void> getAccountArgs(String command, Consumer<ActionResult> handler, Supplier<String> username, Supplier<String> password) {
        return getService(command, handler, () -> new CommandArgs(accountFactory.getModel(new Account(username.get(), password.get(), CommandAccessLevel.GUEST))));
    }

    private static Service<Void> getService(String command, Consumer<ActionResult> handler, Supplier<CommandArgs> argsSupplier) {
        return new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        updateProgress(0.1, 1.0);
                        ActionResult result = Controllers.getRequester().request(command, argsSupplier.get());
                        Platform.runLater(() -> handler.accept(result));
                        return null;
                    }
                };
            }
        };
    }

    private static CollectibleModel build(Map<String, String> map) throws ValueNotValidException {
        CollectibleScheme scheme = Controllers.getRequester().getExecutionController().getTargetClassHandler().getCurrentCollectionHandler().getCollectibleScheme();
        //TODO fix above
        return new CollectibleModel(scheme, getMap(scheme, map));
    }

    private static Map<String, InputtedValue> getMap(CollectibleScheme collectibleScheme, Map<String, String> stringMap) {
        Map<String, InputtedValue> map = new HashMap<>();
        for (String key : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(key);
            if (fieldData.isUserWritable()) {
                if (!fieldData.isCollectible()) map.put(key, new InputtedValue(stringMap.get(key)));
                else map.put(key, new InputtedValue(getMap(fieldData.getCollectibleScheme(), stringMap)));
            }
        }
        return map;
    }
}
