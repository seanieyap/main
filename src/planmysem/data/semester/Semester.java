package planmysem.data.semester;

import java.util.List;

/**
 * Represents a Semester in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Semester {
    private String name;
    private String acadamicYear;
    private List<Month> months;

    /**
     * Assumption: Every field must be present and not null.
     */
    public Semester(String name, String acadamicYear, List<Month> months) {
        this.name = name;
        this.acadamicYear = acadamicYear;
        this.months = months;
    }

    public String getName() {
        return name;
    }

    public String getAcademicYear() {
        return acadamicYear;
    }

    public List<Month> getMonths() {
        return months;
    }

}
