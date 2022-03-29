package commands.instances;

import commands.*;

public class FetchServerData extends AbstractCommand {

    public FetchServerData() {
        super("fetch_server_data", "Internal command for fetching serverData", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
    }

    @Override
    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, "Successfully fetched serverData");
    }
}
