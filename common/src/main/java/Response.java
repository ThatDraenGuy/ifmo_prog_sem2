import cmd.ActionResult;

/**
 * A response form a command's execution. Consists of command's ActionResult.
 */
public interface Response {
    ActionResult getActionResult();
}
