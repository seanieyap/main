package planmysem.data.semester;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import planmysem.data.slot.ReadOnlySlot;
import planmysem.data.slot.Slot;

/**
 * Represents a Day in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Day implements ReadOnlyDay {
    private final DayOfWeek dayOfWeek;
    private final String type;
    private final ArrayList<Slot> slots = new ArrayList<>();

    /**
     * Assumption: Every field must be present and not null.
     */
    public Day(DayOfWeek dayOfWeek, String weekType) {
        this.dayOfWeek = dayOfWeek;
        this.type = weekType;
    }

    public Day(DayOfWeek dayOfWeek, String weekType, ArrayList<Slot> slots) {
        this.dayOfWeek = dayOfWeek;
        this.type = weekType;

        for (Slot slot : slots) {
            this.slots.add(slot);
        }
    }

    /**
     * Add a slot to the day.
     */
    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    /**
     * Edit a slot in the day.
     */
    public void editSlot(ReadOnlySlot targetSlot, LocalTime startTime, int duration,
                         String name, String location, String description) {
        for (Slot slot : slots) {
            if (slot.equals(targetSlot)) {
                if (startTime != null) {
                    slot.setStartTime(startTime);
                }
                if (duration != -1) {
                    slot.setDuration(duration);
                }

                slot.setName(name);

                slot.setLocation(location);

                slot.setDescription(description);
            }
        }
    }

    /**
     * Removes the equivalent slot from the day.
     */
    public void removeSlot(ReadOnlySlot slot) {
        slots.remove(slot);
    }

    /**
     * Removes all slots from the day.
     */
    public void clear() {
        slots.clear();
    }

    public boolean contains(ReadOnlySlot slot) {
        return slots.contains(slot);
    }

    @Override
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String getDay() {
        return dayOfWeek.toString();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public ArrayList<Slot> getSlots() {
        return slots;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyDay // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyDay) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(dayOfWeek, type, slots);
    }

    /**
     * Signals that an operation targeting a specified slot in the list would fail because
     * there is no such matching slot in the list.
     */
    public static class SlotNotFoundException extends Exception {
    }
}
