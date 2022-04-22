package threads;

import annotations.CollectibleField;
import annotations.UserAccessible;
import collection.Validator;
import collection.classes.RawCollectible;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.InputtedValue;
import commands.*;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;
import exceptions.CommandExecutionException;
import exceptions.CommandNonExistentException;
import exceptions.ValueNotValidException;
import lombok.Setter;
import message.Request;
import message.Response;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ClientInteractionController extends Thread {
    private final ExecutionController commandsExecutor;
    private ConsoleHandler consoleHandler;
    @Setter
    private boolean exitQueried;

    public ClientInteractionController(ExecutionController commandsExecutor, ConsoleHandler consoleHandler) {
        this.commandsExecutor = commandsExecutor;
        this.consoleHandler = consoleHandler;
    }


    /**
     * A main loop. Only returns if the "Exit" command is executed or a critical exception occurs.
     */
    public void run() {
        consoleHandler.debugMessage("interactionController started");
        commandsExecutor.initialize();
        while (!exitQueried) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            try {
                String input = consoleHandler.promptInput("");
                Request cmdRequest = parseInput(input);
                Response response = commandsExecutor.executeCommand(cmdRequest);
                handleResponse(response);
            } catch (CommandNonExistentException | CommandArgsAmountException | CommandExecutionException e) {
                consoleHandler.errorMessage(e);
            } catch (NoSuchElementException e) {
                consoleHandler.errorMessage(new NoSuchElementException("InputStream ran out of lines while the program was working"));
                return;
            }
        }
    }

    private Request parseInput(String input) throws CommandNonExistentException, CommandArgsAmountException {
        input = input.strip();
        CommandData commandData;
        CommandArgs cmdArgs = new CommandArgs("");
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length > 2) {
                throw new CommandArgsAmountException();
            }
        }
        commandData = parseCommandData(input);
        return (createRequest(commandData, cmdArgs));
    }

    private Request createRequest(CommandData commandData, CommandArgs arguments) throws CommandArgsAmountException {
        CommandArgsType type = commandData.getCommandArgsType();
        String argumentString = arguments.getArgs();
        boolean isEmpty = argumentString.equals("");
        switch (type) {
            case NO_ARGS:
                if (!isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" does not need arguments!");
                }
                break;
            case COMPLEX_ARG:
                if (!isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" needs a complex argument, not a simple one!");
                } else {
                    CollectibleModel newArgs = promptComplexArgs();
                    arguments = new CommandArgs("", newArgs);
                }
                break;
            case SIMPLE_ARG:
                if (isEmpty) {
                    throw new CommandArgsAmountException(commandData.getName() + " needs an argument!");
                }
                break;
            case BOTH_ARG:
                if (isEmpty) {
                    throw new CommandArgsAmountException("Command \"" + commandData.getName() + "\" needs an in-line (simple) argument!");
                } else {
                    CollectibleModel complexArgs = promptComplexArgs();
                    arguments = new CommandArgs(argumentString, complexArgs);
                }

        }
        return commandsExecutor.createRequest(commandData, arguments);
    }

    /**
     * A method to get command from its name
     *
     * @param input a String consisting of a command name
     * @return The command with corresponding name
     * @throws CommandNonExistentException if inputted command cannot be found in the command list
     */
    private CommandData parseCommandData(String input) throws CommandNonExistentException {
        if (commandsExecutor.isAccessibleCommand(input)) {
            if (commandsExecutor.isClientCommand(input)) {
                return commandsExecutor.getAccessibleClientCommands().get(input);
            } else {
                return commandsExecutor.getAccessibleServerCommands().get(input);
            }
        } else {
            throw new CommandNonExistentException(input);
        }
    }


    /**
     * A method that gets a String and returns a CmdArgs object created with it
     */
    private CommandArgs parseArgs(String input) {
        return new CommandArgs(input);
    }


    private CollectibleModel promptComplexArgs() {
        CollectibleScheme collectibleScheme = commandsExecutor.getTargetClassHandler().getCurrentCollectibleScheme();
        Map<String, InputtedValue> map = promptComplexArgsMap(collectibleScheme);
        try {
            return new CollectibleModel(collectibleScheme, map);
//            return commandsExecutor.getTargetClassHandler().getCurrentCollectionBuilder().rawBuild(map);
        } catch (ValueNotValidException e) {
            consoleHandler.errorMessage(e);
            return null;
        }
    }

    private Map<String, InputtedValue> promptComplexArgsMap(CollectibleScheme collectibleScheme) {
//        List<Field> fields = commandsExecutor.getTargetClassHandler().getCurrentCollectionBuilder().getClassFields(targetClass);
        Map<String, InputtedValue> map = new HashMap<>();
        for (String key : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(key);
            if (fieldData.isUserAccessible()) {
                if (!fieldData.isCollectible()) map.put(key, new InputtedValue(promptField(key, fieldData)));
                else map.put(key, new InputtedValue(promptComplexArgsMap(fieldData.getCollectibleScheme())));
            }
        }
        return map;
    }

    private Object promptField(String name, FieldData fieldData) {
        Class<?> fieldType = fieldData.getType();
        while (true) {
            String message;
            if (fieldType.isEnum()) message = generateEnumPrompt(fieldData);
            else message = "Please enter " + name + ": ";
            String result = consoleHandler.promptInput(message);
            try {
                return Validator.convertAndValidate(fieldData, fieldType, result);
            } catch (ValueNotValidException e) {
                consoleHandler.errorMessage(e);
            }
        }
    }

    private String generateEnumPrompt(FieldData enumFieldData) {
        StringBuilder message = new StringBuilder("Please enter " + enumFieldData.getSimpleName() + " (");
        Field[] enums = enumFieldData.getType().getFields();
        for (Field field : enums) {
            message.append(field.getName()).append(";");
        }
        message.deleteCharAt(message.length() - 1);
        message.append("): ");
        return message.toString();
    }

    /**
     * A method to handle command's execution's response. Gets ActionResult from it and displays it.
     *
     * @param response a response gotten from ...
     * @throws CommandExecutionException if ActionResult isn't success
     */
    private void handleResponse(Response response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        if (!result.isSuccess()) throw new CommandExecutionException(result);
        String message = result.getMessage();
        consoleHandler.message(message);
    }


    public void useDifferentSettings(ConsoleHandler consoleHandler, CommandAccessLevel accessLevel) {
        ConsoleHandler oldConsoleHandler = this.consoleHandler;
        CommandAccessLevel oldAccessLevel = this.commandsExecutor.getUserAccessLevel();
        this.consoleHandler = consoleHandler;
        this.commandsExecutor.setUserAccessLevel(accessLevel);
        run();
        this.commandsExecutor.setUserAccessLevel(oldAccessLevel);
        this.consoleHandler = oldConsoleHandler;
    }

}
