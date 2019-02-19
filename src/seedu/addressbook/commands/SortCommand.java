package seedu.addressbook.commands;


/**
 * Sorts all people in the address book by name in ascneding/descending lexicographical order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Sort all persons in the address book in lexicographical order.\n\t"
            + "Parameters: sort [ascending | asc] | [descending | desc] \n\t"
            + "Example: " + COMMAND_WORD + "desc";

    public static final String MESSAGE_SUCCESS_ASCENDING = "Address book has been sorted in lexicographical order!";
    public static final String MESSAGE_SUCCESS_DESCENDING = "Address book has been sorted in colexicographical order!";
    private boolean ascending = true;

    public SortCommand(){
        super();
    }
    public SortCommand(boolean ascending){
        super();
        this.ascending = ascending;
    }

    @Override
    public CommandResult execute() {
        if (ascending){
            addressBook.sort();
            return new CommandResult(MESSAGE_SUCCESS_ASCENDING);
        }
        addressBook.sortReverse();
        return new CommandResult(MESSAGE_SUCCESS_DESCENDING);
    }
}