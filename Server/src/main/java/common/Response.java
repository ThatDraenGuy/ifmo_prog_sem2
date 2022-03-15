package common;

import cmd.ActionResult;

/**
 * A response form a command's execution. Consists of command's ActionResult. Handled by a {@link console.ConsoleHandler}
 */
public interface Response {
    ActionResult getActionResult();
}
