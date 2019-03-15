package planmysem.commands;

/**
 * Clears the planner.
 */
public class ClearCommandP extends CommandP {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the planner permanently."
            + "\n\tExample: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "The Planner has been cleared!";

    @Override
    public CommandResultP execute() {
        planner.clearSlots();
        return new CommandResultP(MESSAGE_SUCCESS);
    }
}
