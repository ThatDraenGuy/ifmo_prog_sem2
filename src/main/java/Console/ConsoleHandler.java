package Console;

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
import java.util.Scanner;

public class ConsoleHandler {
    private CmdHandler cmdHandler;
    private Class<?> targetClass;
    //TODO think about above
    private Scanner inputScanner;
    private PrintStream out;
    private PrintStream err;
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
    public void loop() {
        while (true) {
            try {
                String input = promptInput("");
                Request cmdRequest = parseInput(input);
                Response response = executeCmd(cmdRequest);
                boolean isCmdExit = handleResponse(response);
                if (isCmdExit) {
                    return;
                }
            } catch (CommandNonExistentException | CmdArgsAmountException | CommandExecutionException e) {
                errorMessage(e);
            } catch (ClassNotFoundException e) {
                errorMessage(new Exception("A critical exception!"));
                //TODO ???
            }

        }
    }
    public String promptInput(String message) {
        if (message.equals("")) {
            out.println("Awaiting your input: ");
        } else {
            out.println(message);
        }
        return inputScanner.nextLine();
    }

    public Request parseInput(String input) throws CommandNonExistentException, CmdArgsAmountException {
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
    public Command parseCommand(String input) throws CommandNonExistentException {
        if (cmdHandler.isInCmds(input)) {
            return cmdHandler.getCmds().get(input);
        } else {
            throw new CommandNonExistentException(input);
        }
    }
    public CmdArgs parseArgs(String input) {
        return new CmdArgs(input);
        // TODO think about this
    }
    public Response executeCmd(Request request) throws CmdArgsAmountException, ClassNotFoundException {
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
    public HashMap<Field, Object> promptComplexArgs(Class<?> targetClass) {
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
                Object obj = promptComplexArgs(field.getType());
                map.put(field, obj);
            }
        }
        return map;
    }
    public String promptWithValidation(Field field, String message) {
        while (true) {
            String result = promptInput(message);
            try {
                cmdHandler.getCollectionHandler().validate(field, result);
                return result;
            } catch (ValueNotValidException e) {
                errorMessage(e);
            }
        }
    }
    public String promptField(Field field) {
        return promptWithValidation(field, "Please enter " + field.getName() + ": ");

    }
    public String promptEnum(Field enumField) {
        StringBuilder message = new StringBuilder("Please enter " + enumField.getName() + " (");
        Field[] enums = enumField.getType().getFields();
        for (Field field : enums) {
            message.append(field.getName()).append(";");
        }
        message.deleteCharAt(message.length()-1);
        message.append("): ");
        return promptWithValidation(enumField, message.toString());
    }
    public boolean handleResponse(Response response) throws CommandExecutionException {
        //TODO think about exit
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
    public void errorMessage(Exception e) {
        err.println(e.getMessage());
    }
}
