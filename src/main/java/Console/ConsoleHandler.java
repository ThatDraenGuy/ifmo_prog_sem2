package Console;

import Annotations.NotNull;
import Collection.Classes.MainCollectible;
import Exceptions.CmdArgsAmountException;
import Exceptions.CommandExecutionException;
import Exceptions.CommandNonExistentException;
import Exceptions.ValueNotValidException;
import Annotations.UserAccessibleEnum;
import Annotations.UserAccessibleField;
import Annotations.UserAccessibleObject;
import cmd.*;
import common.CmdRequest;
import common.Request;
import common.Response;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleHandler {
    final private CmdHandler cmdHandler;
    final private Class<? extends MainCollectible> targetClass;
    final private Scanner inputScanner;
    final private PrintStream out;
    final private PrintStream err;
    public ConsoleHandler(CmdHandler cmdHandler, InputStream in, PrintStream out, PrintStream err) {
        this.cmdHandler = cmdHandler;
        this.targetClass=cmdHandler.getCollectionHandler().getTargetClass();
        inputScanner = new Scanner(in);
        this.out=out;
        this.err=err;
    }
    public void start() {
        loop();
    }
    private void loop() {
        while (true) {
            try {
                String input = promptInput("");
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                boolean isCmdExit = handleResponse(response);
                if (isCmdExit) {
                    if (promptAgreement("Are you sure you want to exit? Any unsaved changes will be gone!")) return;
                }
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
                errorMessage(e);
            } catch (NoSuchElementException e) {
                errorMessage(new NoSuchElementException("InputStream ran out of lines while the program was working"));
                return;
            }

        }
    }
    private String promptInput(String message) {
        if (message.equals("")) {
            out.println("Awaiting your input: ");
        } else {
            out.println(message);
        }
        return inputScanner.nextLine();

    }

    private Request parseInput(String input) throws CommandNonExistentException, CmdArgsAmountException {
        input = input.strip();
        Command cmd;
        CmdArgs cmdArgs = new CmdArgs("");
        if (input.contains(" ")) {
            String[] str = input.split(" ");
            input = str[0];
            cmdArgs = parseArgs(str[1]);
            if (str.length>2) {
                throw new CmdArgsAmountException();
            }
        }
        cmd = parseCommand(input);
        return (new CmdRequest(cmd, cmdArgs));
    }
    private Command parseCommand(String input) throws CommandNonExistentException {
        if (cmdHandler.isInCmds(input)) {
            return cmdHandler.getCmds().get(input);
        } else {
            throw new CommandNonExistentException(input);
        }
    }
    private CmdArgs parseArgs(String input) {
        return new CmdArgs(input);
    }
    private Response executeCmd(Request request) throws CmdArgsAmountException {
        CmdType type = request.getCmd().getCmdType();
        CmdArgs args = request.getCmdArgs();
        if (type==CmdType.COMPLEX_ARG) {
            HashMap<Field, Object> newArgs = promptComplexArgs(targetClass);
            request.setCmdArgs(new CmdArgs(newArgs));
        } else if (type==CmdType.BOTH_ARG) {
            if (args.getArgs().equals("")) {
                throw new CmdArgsAmountException("This command needs an in-line argument!");
            }
            HashMap<Field, Object> complexArgs = promptComplexArgs(targetClass);
            args.setDeconstructedObject(complexArgs);
        }
        return cmdHandler.executeCmd(request);
    }
    private HashMap<Field, Object> promptComplexArgs(Class<?> targetClass) {
        HashMap<Field, Object> map = new HashMap<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(UserAccessibleField.class)) {
                Object obj = promptField(field);
                map.put(field, obj);
            }
            if (field.isAnnotationPresent(UserAccessibleEnum.class)) {
                Object obj = promptEnum(field);
                map.put(field, obj);
            }
            if (field.isAnnotationPresent(UserAccessibleObject.class)) {
                if (!field.isAnnotationPresent(NotNull.class)) {
                    boolean answer = promptAgreement("Object \""+ field.getName()+"\" can be null. Are you gonna input it?");
                    if (answer) {
                        Object obj = promptComplexArgs(field.getType());
                        map.put(field, obj);
                    }
                } else {
                    Object obj = promptComplexArgs(field.getType());
                    map.put(field, obj);
                }

            }
        }
        return map;
    }
    private Object promptWithValidation(Field field, String message) {
        while (true) {
            String result = promptInput(message);
            try {
                return cmdHandler.getCollectionHandler().validate(field, result);
            } catch (ValueNotValidException e) {
                errorMessage(e);
            }
        }
    }
    private Object promptField(Field field) {
        return promptWithValidation(field, "Please enter " + field.getName() + ": ");

    }
    private Object promptEnum(Field enumField) {
        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
        Field[] enums = enumField.getType().getFields();
        for (Field field : enums) {
            message.append(field.getName()).append(";");
        }
        message.deleteCharAt(message.length()-1);
        message.append("): ");
        return promptWithValidation(enumField, message.toString());
    }
    private boolean promptAgreement(String message) {
        while (true) {
            String answer = promptInput(message+" (Y/N)");
            switch (answer) {
                case "Y": return true;
                case "N": return false;
                default: out.println("Please input \"Y\" or \"N\"");
            }
        }
    }
    private boolean handleResponse(Response response) throws CommandExecutionException {
        ActionResult result = response.getActionResult();
        boolean isSuccess = result.isSuccess();
        if (isSuccess) {
            String message = result.getMessage();
            if (message.equals("exit")) {
                return true;
            }else {
                out.println(result.getMessage());
                return false;
            }
        } else {
            throw new CommandExecutionException(result);
        }
    }
    private void errorMessage(Exception e) {
        err.println(e.getMessage());
    }
}
