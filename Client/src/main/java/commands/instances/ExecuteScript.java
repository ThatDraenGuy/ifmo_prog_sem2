package commands.instances;

import client.ConnectionHandler;
import collection.DragonCollectionBuilder;
import commands.*;
import console.ConsoleHandler;

import java.io.*;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A command for executing scripts.
 */
public class ExecuteScript extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final CommandsHandler clientCommandsHandler;
    private final DragonCollectionBuilder collectionBuilder;
    private final HashSet<String> scriptHistory;

    public ExecuteScript(ConnectionHandler connectionHandler, CommandsHandler clientCommandsHandler, DragonCollectionBuilder collectionBuilder) {
        super("execute_script", "считать и исполнить скрипт из указанного файла", CommandType.SIMPLE_ARG);
        this.connectionHandler = connectionHandler;
        this.clientCommandsHandler = clientCommandsHandler;
        this.collectionBuilder = collectionBuilder;
        this.scriptHistory = new HashSet<>();
    }

    @Override
    public ActionResult action(CommandArgs args) {
        //TODO normal log
        try {
            File scriptFile = new File(args.getArgs());
            String path = scriptFile.getAbsolutePath();
            Scanner fileScanner = new Scanner(scriptFile);
            if (!scriptHistory.add(path)) {
                return finishScript(false, "Script recursion detected!", path);
            }
            StringBuilder script = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                script.append(fileScanner.nextLine()).append("\n");
            }
            script.append("finish_script");
            InputStream in = new ByteArrayInputStream(script.toString().getBytes());
            OutputStream out = new ByteArrayOutputStream();
            PrintStream outPrint = new PrintStream(out);
            OutputStream err = new ByteArrayOutputStream();
            PrintStream errPrint = new PrintStream(err);
            ConsoleHandler consoleHandler = new ConsoleHandler(connectionHandler, clientCommandsHandler, collectionBuilder, in, outPrint, errPrint);
            try {
                consoleHandler.start();
            } catch (NoSuchElementException e) {
                return finishScript(false, "Reached end of the script in the process of executing command!", path);
            }
            String outRes = out.toString();
            String errRes = err.toString();
            if (errRes.equals("")) {
                return finishScript(true, "Successfully executed script.", path);
            } else {
                return finishScript(false, "An exception(s) occurred while trying to execute the script. Here's a full exception log: \n" + errRes, path);
            }
        } catch (FileNotFoundException e) {
            return new ActionResult(false, "Cannot find file \"" + args.getArgs() + "\"");
        }
    }

    private ActionResult finishScript(boolean res, String message, String path) {
        scriptHistory.remove(path);
        return new ActionResult(res, message);
    }
}