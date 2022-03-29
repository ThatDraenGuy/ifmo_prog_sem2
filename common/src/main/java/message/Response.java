package message;

import commands.ActionResult;

import java.io.Serializable;

/**
 * A response form a command's execution. Consists of command's ActionResult.
 */
public interface Response extends Serializable {
    ActionResult getActionResult();

    ServerData getServerData();

}
