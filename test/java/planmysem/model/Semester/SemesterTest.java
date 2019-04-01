package planmysem.model.Semester;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;

import org.junit.Test;
import planmysem.common.Clock;
import planmysem.model.semester.Semester;


public class SemesterTest {

    @Test
    public void equals() {
        assertTrue(Semester.generateSemester(LocalDate.now(Clock.get()))
            .equals(Semester.generateSemester(LocalDate.now(Clock.get()))));

        assertFalse(Semester.generateSemester(LocalDate.now(Clock.get()))
                .equals(Semester.generateSemester(LocalDate.of(1999, 1, 1))));

    }
}
