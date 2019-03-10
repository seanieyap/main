package planmysem.data.recurrence;

import static planmysem.common.Utils.getNearestDayOfWeek;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import planmysem.data.semester.Semester;

/**
 * Represents a Recurrence Value of a slot in the Planner.
 */
public class Recurrence {
    private final boolean recess; // Represents recess week
    private final boolean reading; // Represents reading week
    private final boolean past; // Represents past academic weeks
    private final DayOfWeek day;
    private final LocalDate date;

    /**
     * Generate Recurrence values from a set that recurse over a day.
     */
    public Recurrence(Set<String> recurrences, int day) {
        this.day = DayOfWeek.of(day);
        this.date = getNearestDayOfWeek(LocalDate.now(), day);

        if (recurrences == null) {
            this.recess = false;
            this.reading = false;
            this.past = false;
            return;
        }
        this.recess = recurrences.contains("recess");
        this.reading = recurrences.contains("reading");
        this.past = recurrences.contains("past");
    }

    /**
     * Generate Recurrence values from a set that recurse over a date.
     */
    public Recurrence(Set<String> recurrences, LocalDate date) {
        this.day = date.getDayOfWeek();
        this.date = date;

        if (recurrences == null) {
            this.recess = false;
            this.reading = false;
            this.past = false;

            return;
        }
        this.recess = recurrences.contains("recess");
        this.reading = recurrences.contains("reading");
        this.past = recurrences.contains("past");
    }

    /**
     * Generate dates to place slots in the semester.
     */
    public Set<LocalDate> generateDates(Semester semester) {
        Set<LocalDate> result = new HashSet<>();

        LocalDate dateStart;
        if (past) {
            dateStart = semester.getStartDate();
        } else {
            dateStart = LocalDate.now();
        }

        if (!recess && !reading && !past) {
            result.add(date);
            return result;
        }

        // recurse over normal days
        result.addAll(getDates(semester.getNormalDays(), dateStart));

        // recurse over recess days
        result.addAll(getDates(semester.getRecessDays(), dateStart));

        // recurse over reading days
        result.addAll(getDates(semester.getReadingDays(), dateStart));

        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Recurrence // instanceof handles nulls
                && this.recess == ((Recurrence) other).recess
                && this.reading == ((Recurrence) other).reading
                && this.past == ((Recurrence) other).past); // state check
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
        if (past) {
            hashCode += 4; // 0100
        }
        return hashCode;
    }

    /**
     * get set of dates where it is a specific dayofweek and is after a start date.
     */
    private Set<LocalDate> getDates(Set<LocalDate> dates, LocalDate dateStart) {
        final Set<LocalDate> result = new HashSet<>();
        for (LocalDate d : dates) {
            if (d.getDayOfWeek() == day && (d.isAfter(dateStart) || d.isEqual(dateStart))) {
                result.add(date);
            }
        }

        return result;
    }
}
