package planmysem.model.Semester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;

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

        assertEquals(day.getDayOfWeek(), DayOfWeek.of(1));
        assertNotEquals(day.getDayOfWeek(), DayOfWeek.of(2));
    }

    @Test
    public void getDay() {
        DayOfWeek dayOfWeek = DayOfWeek.of(1);
        Day day = new Day(dayOfWeek, "Week 1");

        assertEquals(day.getDay(), dayOfWeek.toString());

        DayOfWeek dayOfWeek2 = DayOfWeek.of(2);
        assertNotEquals(day.getDay(), dayOfWeek2.toString());
    }

    @Test
    public void getType() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");

        assertEquals(day.getType(), "Week 1");
        assertNotEquals(day.getType(), "Week 2");
    }

    @Test
    public void getSlots() {
        Day day = new Day(DayOfWeek.of(1), "Week 1");

        assertEquals(day.getSlots(), new ArrayList<>());

        Slot slot = new SlotBuilder().slotOne();
        day.addSlot(slot);
        assertEquals(day.getSlots(), Collections.singletonList(slot));
    }

    @Test
    public void equals() {
        assertEquals(new Day(DayOfWeek.of(1), "Week 1"),
                new Day(DayOfWeek.of(1), "Week 1"));
        assertEquals(new Day(DayOfWeek.of(1), "Week 2"),
                new Day(DayOfWeek.of(1), "Week 2"));
        assertEquals(new Day(DayOfWeek.of(2), "Week 2"),
                new Day(DayOfWeek.of(2), "Week 2"));

        assertNotEquals(new Day(DayOfWeek.of(2), "Week 2"),
                new Day(DayOfWeek.of(3), "Week 2"));
        assertNotEquals(new Day(DayOfWeek.of(2), "Week 2"),
                new Day(DayOfWeek.of(2), "Week 3"));
    }
}
