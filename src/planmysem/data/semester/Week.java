package planmysem.data.semester;

import java.util.List;

/**
 * Represents a Week in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Week {
    private String name;
    private List<Day> days;

    /**
     * Assumption: Every field must be present and not null.
     */
    public Week(String name, List<Day> days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public List<Day> getDays() {
        return days;
    }

}
