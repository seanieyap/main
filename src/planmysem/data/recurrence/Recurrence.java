package planmysem.data.recurrence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import planmysem.data.semester.Semester;

/**
 * Represents a Recurrence Value of a slot in the Planner.
 */
public class Recurrence {
    private final boolean recess; // Represents recess week
    private final boolean reading; // Represents reading week
    private final boolean normal; // Represents normal academic weeks
    private final int day;
    private final LocalDate date;

    /**
     * Generate Recurrence values from a set that recurse over a day.
     */
    public Recurrence(Set<String> recurrences, int day) {
        this.day = day;
        this.date = null;

        if (recurrences == null) {
            this.recess = false;
            this.reading = false;
            this.normal = false;
            return;
        }
        this.recess = recurrences.contains("recess");
        this.reading = recurrences.contains("reading");
        this.normal = recurrences.contains("normal");
    }

    /**
     * Generate Recurrence values from a set that recurse over a date.
     */
    public Recurrence(Set<String> recurrences, LocalDate date) {
        this.day = 0;
        this.date = date;

        if (recurrences == null) {
            this.recess = false;
            this.reading = false;
            this.normal = false;

            return;
        }
        this.recess = recurrences.contains("recess");
        this.reading = recurrences.contains("reading");
        this.normal = recurrences.contains("normal");
    }

    /**
     * Generate dates to place slots in the semester.
     */
    public List<LocalDate> generateDates(Semester semester) {
        List<LocalDate> dates = new ArrayList<>();

        //TODO: generate dates

        return dates;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Recurrence // instanceof handles nulls
                && this.recess == ((Recurrence) other).recess
                && this.reading == ((Recurrence) other).reading
                && this.normal == ((Recurrence) other).normal); // state check
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (recess) {
            hashCode += 1; // 0001
        }
        if (reading) {
            hashCode += 2; // 0010
        }
        if (normal) {
            hashCode += 4; // 0100
        }
        return hashCode;
    }
}
