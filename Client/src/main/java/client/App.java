package client;

import annotations.CollectibleField;
import annotations.UserAccessible;
import collection.CollectionBuilder;
import collection.Validator;
import collection.classes.RawCollectible;
import commands.*;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;
import exceptions.CommandExecutionException;
import exceptions.CommandNonExistentException;
import exceptions.ValueNotValidException;
import message.CommandRequest;
import message.CommandResponse;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class App {
    final private ConnectionHandler connectionHandler;
    final private CommandsHandler clientCommandsHandler;
    final private CollectionBuilder<?> collectionBuilder;
    private HashMap<String, CommandData> clientCommands;
    private HashMap<String, CommandData> serverCommands;
    final private Class<?> targetClass;
    private ConsoleHandler consoleHandler;

    public App(ConnectionHandler connectionHandler, CommandsHandler clientCommandsHandler, CollectionBuilder<?> collectionBuilder, ConsoleHandler consoleHandler) {
        this.connectionHandler = connectionHandler;
        this.clientCommandsHandler = clientCommandsHandler;
        this.collectionBuilder = collectionBuilder;
        this.consoleHandler = consoleHandler;
        this.targetClass = connectionHandler.getServerData().getTargetClass();
    }

    /**
     * Starts console's loop
     */
    public void start() {
        this.clientCommands = clientCommandsHandler.getCommandsData();
        this.serverCommands = connectionHandler.getServerData().getServerCommands();
        loop();
    }

    /**
     * A main loop. Only returns if the "Exit" command is executed or a critical exception occurs.
     */
    private void loop() {
        while (true) {
            try {
                String input = consoleHandler.promptInput("");
                CommandRequest cmdRequest = parseInput(input);
                CommandResponse response = executeCommand(cmdRequest);
                boolean isExitQueried = handleResponse(response);
                if (isExitQueried) return;
            } catch (CommandNonExistentException | CommandArgsAmountException | CommandExecutionException e) {
                consoleHandler.errorMessage(e);
            } catch (NoSuchElementException e) {
                consoleHandler.errorMessage(new NoSuchElementException("InputStream ran out of lines while the program was working"));
                return;
            }
        }
    }

    private CommandRequest parseInput(String input) throws CommandNonExistentException, CommandArgsAmountException {
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

    private CommandRequest createRequest(CommandData commandData, CommandArgs arguments) throws CommandArgsAmountException {
        CommandType type = commandData.getCommandType();
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
                    RawCollectible<?> newArgs = promptComplexArgs();
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
                    RawCollectible<?> complexArgs = promptComplexArgs();
                    arguments = new CommandArgs(argumentString, complexArgs);
                }

        }
        return new CommandRequest(commandData, arguments);
    }

    /**
     * A method to get command from its name
     *
     * @param input a String consisting of a command name
     * @return The command with corresponding name
     * @throws CommandNonExistentException if inputted command cannot be found in the command list
     */
    private CommandData parseCommandData(String input) throws CommandNonExistentException {
        if (isInCommands(input)) {
            if (isClientCommand(input)) {
                return clientCommands.get(input);
            } else {
                return serverCommands.get(input);
            }
        } else {
            throw new CommandNonExistentException(input);
        }
    }

    private boolean isInCommands(String name) {
        return (serverCommands.containsKey(name) || clientCommands.containsKey(name));
        //TODO wtf?
    }

    private boolean isClientCommand(String name) {
        return clientCommands.containsKey(name);
    }

    private boolean isClientCommand(CommandRequest request) {
        return isClientCommand(request.getCommandData().getName());
        //TODO rework
    }

    /**
     * A method that gets a String and returns a CmdArgs object created with it
     */
    private CommandArgs parseArgs(String input) {
        return new CommandArgs(input);
    }

    /**
     * A method that gets a request and invokes ... method.
     *
     * @param request a request for a command execution
     * @return response gotten from command's execution
     * @throws CommandArgsAmountException if command requires both a simple and a complex argument and a simple one wasn't provided
     */
    private CommandResponse executeCommand(CommandRequest request) throws CommandArgsAmountException {
        if (isClientCommand(request)) {
            return clientCommandsHandler.executeCommand(request);
        }
        return connectionHandler.send(request);
    }

    private RawCollectible<?> promptComplexArgs() {
        Map<String, Object> map = promptComplexArgs(targetClass);
        try {
            return collectionBuilder.rawBuild(map);
        } catch (ValueNotValidException e) {
            consoleHandler.errorMessage(e);
            return null;
        }
    }

    private Map<String, Object> promptComplexArgs(Class<?> targetClass) {
        List<Field> fields = collectionBuilder.getClassFields(targetClass);
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            String key = field.getName();
            if (field.isAnnotationPresent(UserAccessible.class)) {
                if (!field.isAnnotationPresent(CollectibleField.class)) map.put(key, promptField(key, field));
                else map.put(key, promptComplexArgs(field.getType()));
            }
        }
        return map;
    }

    private Object promptField(String name, Field field) {
        Class<?> fieldType = field.getType();
        while (true) {
            String message;
            if (fieldType.isEnum()) message = generateEnumPrompt(field);
            else message = "Please enter " + name + ": ";
            String result = consoleHandler.promptInput(message);
            try {
                return Validator.validate(field, fieldType, result);
            } catch (ValueNotValidException e) {
                consoleHandler.errorMessage(e);
            }
        }
    }

    private String generateEnumPrompt(Field enumField) {
        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
        Field[] enums = enumField.getType().getFields();
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
     * @return a boolean value: if the response is from an "Exit" command it's true, otherwise it's false.
     * @throws CommandExecutionException if ActionResult isn't success
     */
    private boolean handleResponse(CommandResponse response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        boolean isSuccess = result.isSuccess();
        if (isSuccess) {
            String message = result.getMessage();
            consoleHandler.message(message);
            return result.isConsoleExitQueried();
        } else {
            throw new CommandExecutionException(result);
        }
    }

    public void useDifferentConsole(ConsoleHandler consoleHandler) {
        ConsoleHandler oldConsoleHandler = this.consoleHandler;
        this.consoleHandler = consoleHandler;
        loop();
        this.consoleHandler = oldConsoleHandler;
    }
}
