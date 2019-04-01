package planmysem.model.Semester;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import planmysem.model.semester.Day;
import planmysem.model.slot.Slot;
import planmysem.testutil.SlotBuilder;


public class DayTest {

    @Test
    public void contains() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");
        Slot slot = new SlotBuilder().slotOne();

        assertFalse(day.contains(slot));

        day.addSlot(slot);
        assertTrue(day.contains(slot));
    }

    @Test
    public void getDayOfWeek() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");

        assertTrue(day.getDayOfWeek() == DayOfWeek.of(1));
        assertFalse(day.getDayOfWeek() == DayOfWeek.of(2));
    }

    @Test
    public void getDay() {
        DayOfWeek dayOfWeek = DayOfWeek.of(1);
        Day day = new Day(dayOfWeek, "Week 1");

        assertTrue(day.getDay() == dayOfWeek.toString());

        DayOfWeek dayOfWeek2 = DayOfWeek.of(2);
        assertFalse(day.getDay() == dayOfWeek2.toString());
    }

    @Test
    public void getType() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");

        assertTrue(day.getType() == "Week 1");
        assertFalse(day.getType() == "Week 2");
    }

    @Test
    public void getSlots() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");

        assertTrue(day.getSlots().equals(new ArrayList<>()));

        Slot slot = new SlotBuilder().slotOne();
        day.addSlot(slot);
        assertTrue(day.getSlots().equals(Arrays.asList(slot)));
    }

    @Test
    public void equals() {
        assertTrue(new Day(DayOfWeek.of(1), "Week 1")
                .equals(new Day(DayOfWeek.of(1), "Week 1")));
        assertTrue(new Day(DayOfWeek.of(1), "Week 2")
                .equals(new Day(DayOfWeek.of(1), "Week 2")));

        assertFalse(new Day(DayOfWeek.of(1), "Week 1")
                .equals(new Day(DayOfWeek.of(1), "Week 2")));
        assertFalse(new Day(DayOfWeek.of(1), "Week 1")
                .equals(new Day(DayOfWeek.of(2), "Week 1")));
    }
}
