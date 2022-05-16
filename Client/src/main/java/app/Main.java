package app;

import collection.CollectionClassesHandler;
import commands.ClientCommandsHandler;
import commands.CommandAccessLevel;
import commands.Requester;
import message.Response;
import security.Account;
import security.CurrentAccount;
import threads.ConsoleInteractionController;
import threads.MessageReader;
import threads.MessageSender;
import web.ConnectionHandler;
import threads.ThreadHandler;
import commands.ExecutionController;
import commands.instances.*;
import console.ConsoleHandler;

import java.io.*;
import java.util.concurrent.Exchanger;

/**
 * An app.Main class, only consists of main() method.
 */
public class Main {
    /**
     *
     */
    public static void main(String... args) {
        try {
            ConsoleHandler consoleHandler = new ConsoleHandler(System.in, System.out, System.err);
            CurrentAccount.setAccount(new Account("guest", "guest", CommandAccessLevel.GUEST));
            ConnectionHandler connectionHandler = new ConnectionHandler(consoleHandler);
            ClientCommandsHandler clientCommandsHandler = new ClientCommandsHandler();
            CollectionClassesHandler collectionClassesHandler = new CollectionClassesHandler(consoleHandler);
            Exchanger<Response> responseExchanger = new Exchanger<>();
            MessageSender messageSender = new MessageSender(connectionHandler, responseExchanger, consoleHandler);
            ExecutionController executionController = new ExecutionController(clientCommandsHandler, consoleHandler, collectionClassesHandler, messageSender);
            MessageReader messageReader = new MessageReader(responseExchanger, connectionHandler, executionController, consoleHandler);
            ConsoleInteractionController app = new ConsoleInteractionController(executionController, consoleHandler);
            ThreadHandler threadHandler = new ThreadHandler(connectionHandler, messageReader, app, consoleHandler);
            clientCommandsHandler.addCommands(
                    new Exit(consoleHandler, threadHandler),
                    new History(executionController),
                    new Help(executionController),
                    new ExecuteScript(app),
                    new FinishScript(app),
                    new Disconnect(connectionHandler, executionController),
                    new Connect(connectionHandler, executionController, threadHandler),
                    new ApplyCollectionChange(collectionClassesHandler),
                    new ApplyFullCollection(collectionClassesHandler),
                    new SetAccount(executionController),
                    new Show(collectionClassesHandler),
                    new ShowMy(collectionClassesHandler),
                    new Info(collectionClassesHandler),
                    new CountByColor(collectionClassesHandler),
                    new FilterByType(collectionClassesHandler),
                    new FilterGreaterThanAge(collectionClassesHandler));
            executionController.initialize();
            Controllers.setThreadHandler(threadHandler);
            Controllers.setRequester(new Requester(executionController));
            threadHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
