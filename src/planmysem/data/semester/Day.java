package planmysem.data.semester;

import java.time.DayOfWeek;
import java.util.ArrayList;

import planmysem.data.slot.Slot;

/**
 * Represents a Day in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Day implements ReadOnlyDay {
    private final DayOfWeek dayOfWeek;
    private final String weekType;
    private final ArrayList<Slot> slots = new ArrayList<>();

    /**
     * Assumption: Every field must be present and not null.
     * TODO: Ding Heng, please init weekType
     */
    public Day(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.weekType = null;
    }

    public Day(DayOfWeek dayOfWeek, String weekType, ArrayList<Slot> slots) {
        this.dayOfWeek = dayOfWeek;
        this.weekType = null;

        for (Slot slot : slots) {
            this.slots.add(slot);
        }
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public void clear() {
        slots.clear();
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
    public String getWeekType() {
        return weekType;
    }

    @Override
    public ArrayList<Slot> getSlots() {
        return slots;
    }
}
