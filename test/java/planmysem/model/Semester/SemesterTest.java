package planmysem.model.Semester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;

import org.junit.Test;
import planmysem.common.Clock;
import planmysem.model.semester.Semester;


public class SemesterTest {

    @Test
    public void equals() {
        assertEquals(Semester.generateSemester(LocalDate.now(Clock.get())),
                Semester.generateSemester(LocalDate.now(Clock.get())));
        assertNotEquals(Semester.generateSemester(LocalDate.now(Clock.get())),
                Semester.generateSemester(LocalDate.of(1999, 1, 1)));

    }
}
