package commands.instances;

import commands.*;

public class Disconnect extends AbstractCommand {
    public Disconnect() {
        super("disconnect", "завершить программу (без сохранения в файл)", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL, true);
    }

    @Override
    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, "disconnected");
    }
}
