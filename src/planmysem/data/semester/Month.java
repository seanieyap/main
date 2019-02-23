package planmysem.data.semester;

import java.util.List;

/**
 * Represents a Month in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Month {
    private String name;
    private List<Week> weeks;

    /**
     * Assumption: Every field must be present and not null.
     */
    public Month(String name, List<Week> weeks) {
        this.name = name;
        this.weeks = weeks;
    }

    public String getName() {
        return name;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

}
