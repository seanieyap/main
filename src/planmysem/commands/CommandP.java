package planmysem.commands;

import java.time.LocalDate;
import java.util.HashMap;

import planmysem.data.Planner;
import planmysem.data.semester.ReadOnlyDay;

/**
 * Represents an executable command.
 */
public abstract class CommandP {
    protected Planner planner;
    private HashMap<LocalDate, ? extends ReadOnlyDay> relevantDays;
    private LocalDate targetDate = null;

    /**
     * @param targetDate last visible listing index of the target person
     */
    public CommandP(LocalDate targetDate) {
        this.setTargetDate(targetDate);
    }

    protected CommandP() {
    }

    /**
     * Executes the command and returns the result.
     */
    public CommandResultP execute() {
        throw new UnsupportedOperationException("This method should be implement in child classes");
    }

    //Note: it is better to make the execute() method abstract, by replacing the above method with the line below:
    //public abstract CommandResult execute();

    /**
     * Supplies the data the command will operate on.
     */
    public void setData(Planner planner, HashMap<LocalDate, ? extends ReadOnlyDay> relevantDays) {
        this.planner = planner;
        this.relevantDays = relevantDays;
    }

    /**
     * Extracts the the target Day in the last shown list from the given arguments.
     *
     * @throws IndexOutOfBoundsException if the target index is out of bounds of the last viewed listing
     */
    protected ReadOnlyDay getTargetDay() throws IndexOutOfBoundsException {
        return relevantDays.get(getTargetDate());
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
}
