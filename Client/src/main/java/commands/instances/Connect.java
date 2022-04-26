package commands.instances;

import web.ConnectionHandler;
import threads.ThreadHandler;
import commands.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class Connect extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final ThreadHandler threadHandler;
    private final ExecutionController executionController;

    public Connect(ConnectionHandler connectionHandler, ExecutionController executionController, ThreadHandler threadHandler) {
        super("connect", "подключиться к серверу", CommandArgsType.SIMPLE_ARG, CommandAccessLevel.DISCONNECTED);
        this.connectionHandler = connectionHandler;
        this.executionController = executionController;
        this.threadHandler = threadHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        try {
            String address = args.getArgs();
            String[] splitStr = address.split(":");
            if (splitStr.length != 2)
                return new ActionResult(false, "Please input address in the form of host:port (e.g. 127.0.0.1:25565)");
            String host = splitStr[0];
            int port = Integer.parseInt(splitStr[1]);
            connectionHandler.connect(host, port);
            threadHandler.reloadMessageReader();
            executionController.setUserAccessLevel(CommandAccessLevel.GUEST);
            //TODO normal level changing
            return new ActionResult(true, "You have been successfully connected");
        } catch (NumberFormatException e) {
            return new ActionResult(false, "Couldn't parse port");
        } catch (UnknownHostException e) {
            return new ActionResult(false, "Couldn't find host \"" + args.getArgs() + "\"");
        } catch (IOException e) {
            return new ActionResult(false, e.getMessage());
        }
    }
}
