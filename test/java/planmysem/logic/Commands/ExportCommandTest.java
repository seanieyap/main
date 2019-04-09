package planmysem.logic.Commands;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.TemporaryFolder;
import planmysem.common.Clock;
import planmysem.logic.CommandHistory;
import planmysem.logic.commands.ExportCommand;
import planmysem.logic.commands.exceptions.CommandException;
import planmysem.model.Model;
import planmysem.model.ModelManager;

import static planmysem.logic.Commands.CommandTestUtil.assertCommandFailure;
import static planmysem.logic.Commands.CommandTestUtil.assertCommandSuccess;

public class ExportCommandTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setup() {
        Clock.set("2019-01-14T10:00:00Z");
    }

    @Test
    public void execute_export_success() throws IOException, CommandException {
        File testFolder = tempFolder.newFolder("test");
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        new ExportCommand(testFolder.getPath() + "\\ExportTest").execute(model, commandHistory);
        assertCommandSuccess(new ExportCommand(testFolder.getPath() + "\\ExportTest"), model,
                commandHistory, ExportCommand.MESSAGE_SUCCESS, expectedModel);

        String expectedIcs = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nEND:VCALENDAR\r\n";
        String actualIcs = new String(Files.readAllBytes(Paths.get(testFolder.getPath() + "\\ExportTest.ics")));

        Assert.assertEquals(actualIcs, expectedIcs);
    }

    @Test
    public void execute_export_fail() {
        Model model = new ModelManager();
        assertCommandFailure(new ExportCommand("?"), model, commandHistory, ExportCommand.MESSAGE_FAILED);
    }
}
