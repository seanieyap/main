package planmysem.commands;


/**
 * Represents an incorrect command. Upon execution, produces some feedback to the user.
 */
public class IncorrectCommandP extends CommandP {

    public final String feedbackToUser;

    public IncorrectCommandP(String feedbackToUser) {
        this.feedbackToUser = feedbackToUser;
    }

    @Override
    public CommandResultP execute() {
        return new CommandResultP(feedbackToUser);
    }

}
