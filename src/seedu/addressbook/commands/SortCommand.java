package seedu.addressbook.commands;


/**
 * Lists all persons in the address book to the user.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Sort all persons in the address book in ascending order.\n\t"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Address book has been sorted by ascending order!";

    @Override
    public CommandResult execute() {
        addressBook.sort();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}