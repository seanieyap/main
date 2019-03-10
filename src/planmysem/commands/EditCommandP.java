package planmysem.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import planmysem.data.exception.IllegalValueException;
import planmysem.data.recurrence.Recurrence;
import planmysem.data.semester.Semester;
import planmysem.data.slot.Description;
import planmysem.data.slot.Location;
import planmysem.data.slot.Name;
import planmysem.data.slot.Slot;
import planmysem.data.tag.TagP;

/**
 * Adds a person to the address book.
 */
public class EditCommandP extends CommandP {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Adds a single or multiple slot to the Planner."
            + "\n\tParameters: NAME st/START_TIME et/END_TIME|DURATION [date/DATE|DAY_OF_WEEK] "
            + "[l/LOCATION] [d/DESCRIPTION] [r/recess|norecess] [r/reading|noreading] [r/normal|nonormal] [t/TAG].."
            + "\n\tExample: " + COMMAND_WORD
            + " CS2113T Tutorial st/0800 et/0900 date/tuesday l/COM2 r/norecess r/noreading "
            + " t/tutorial t/cs2113t t/module";

    public static final String MESSAGE_SUCCESS_NO_CHANGE = "No slots were added.";
    public static final String MESSAGE_SUCCESS = "Slots added: %1$s";
    public static final String MESSAGE_FAIL_OUT_OF_BOUNDS = "Date specified is out of bounds.";

    private final Slot slot;
    private final Recurrence recurrence;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommandP(LocalDate date, String name, String location, String description, LocalTime startTime,
                        int duration, Set<String> tags, Set<String> recurrences) throws IllegalValueException {
        slot = new Slot(new Name(name), new Location(location), new Description(description),
                startTime, duration, parseTags(tags));
        recurrence = new Recurrence(recurrences, date);
    }

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommandP(int day, String name, String location, String description, LocalTime startTime,
                        int duration, Set<String> tags, Set<String> recurrences) throws IllegalValueException {
        slot = new Slot(new Name(name), new Location(location), new Description(description),
                startTime, duration, parseTags(tags));
        recurrence = new Recurrence(recurrences, day);
    }

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommandP(LocalDate date, String name, String location, String description, LocalTime startTime,
                        LocalTime endTime, Set<String> tags, Set<String> recurrences) throws IllegalValueException {
        slot = new Slot(new Name(name), new Location(location), new Description(description),
                startTime, endTime, parseTags(tags));
        recurrence = new Recurrence(recurrences, date);
    }

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommandP(int day, String name, String location, String description, LocalTime startTime,
                        LocalTime endTime, Set<String> tags, Set<String> recurrences) throws IllegalValueException {
        slot = new Slot(new Name(name), new Location(location), new Description(description),
                startTime, endTime, parseTags(tags));
        recurrence = new Recurrence(recurrences, day);
    }


    @Override
    public CommandResultP execute() {
        try {
            List<LocalDate> dates = recurrence.generateDates(planner.getSemester());

            for (LocalDate date : dates) {
                planner.addSlot(date, slot);
            }

            if (dates.size() == 0) {
                return new CommandResultP(MESSAGE_SUCCESS_NO_CHANGE);
            } else {
                return new CommandResultP(String.format(MESSAGE_SUCCESS, dates));
            }
        } catch (Semester.DateNotFoundException dnfe) {
            return new CommandResultP(MESSAGE_FAIL_OUT_OF_BOUNDS);
        }
    }

    /**
     * Convert set of strings into set of tags.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    private Set<TagP> parseTags(Set<String> tags) throws IllegalValueException {
        Set<TagP> tagset = new HashSet<>();
        for (String tag : tags) {
            tagset.add(new TagP(tag));
        }
        return tagset;
    }
}
