package planmysem.logic.Commands;

import static planmysem.logic.Commands.CommandTestUtil.assertCommandFailure;
import static planmysem.logic.Commands.CommandTestUtil.assertCommandSuccess;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import planmysem.logic.CommandHistory;
import planmysem.logic.commands.ImportCommand;
import planmysem.model.Model;
import planmysem.model.ModelManager;
import planmysem.testutil.SlotBuilder;

public class ImportCommandTest {
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    private SlotBuilder slotBuilder = new SlotBuilder();


    @Before
    public void setup() throws Exception {

        // Create typical planner
        model = new ModelManager();

        expectedModel = new ModelManager();
        expectedModel.addSlot(LocalDate.of(2019, 02, 01), slotBuilder.generateSlot(1));
        expectedModel.addSlot(LocalDate.of(2019, 02, 02), slotBuilder.generateSlot(2));
        expectedModel.addSlot(LocalDate.of(2019, 02, 03), slotBuilder.generateSlot(3));
        expectedModel.addSlot(LocalDate.of(2019, 02, 04), slotBuilder.generateSlot(3));
        expectedModel.setLastShownList(model.getLastShownList());

    }

    @Test
    public void execute_import_success() {
        String expectedMessage = ImportCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(new ImportCommand("test/data/ImportTest/ImportTest.ics"), model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_import_invalidEvent() {
        String expectedMessage = ImportCommand.MESSAGE_SUCCESS + "1 event(s) failed to import.\n";
        assertCommandSuccess(new ImportCommand("test/data/ImportTest/ImportTest2.ics"), model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_import_fail() {
        String expectedMessage = ImportCommand.MESSAGE_ERROR_IN_READING_FILE;
        assertCommandFailure(new ImportCommand("test/data/ImportTest/FileDoesNotExist.ics"), model,
                commandHistory, expectedMessage);
    }
}
