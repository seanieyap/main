package planmysem.commands;


/**
 * Shows help instructions.
 */
public class HelpCommandP extends CommandP {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Shows program usage instructions.\n\t"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_ALL_USAGES = AddCommandP.MESSAGE_USAGE
            + "\n\n" + EditCommandP.MESSAGE_USAGE
            //            + "\n\n" + DeleteCommand.MESSAGE_USAGE
            + "\n\n" + ClearCommandP.MESSAGE_USAGE
            + "\n\n" + FindCommand.MESSAGE_USAGE
            + "\n\n" + ListCommandP.MESSAGE_USAGE
            //            + "\n\n" + ViewCommand.MESSAGE_USAGE
            //            + "\n\n" + ViewAllCommand.MESSAGE_USAGE
            + "\n\n" + HelpCommandP.MESSAGE_USAGE
            + "\n\n" + ExitCommandP.MESSAGE_USAGE;

    @Override
    public CommandResultP execute() {
        return new CommandResultP(MESSAGE_ALL_USAGES);
    }
}
