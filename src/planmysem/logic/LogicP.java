package planmysem.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import planmysem.commands.CommandP;
import planmysem.commands.CommandResultP;
import planmysem.data.Planner;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.data.slot.Slot;
import planmysem.parser.ParserP;
import planmysem.storage.StorageFileP;

/**
 * Represents the main Logic of the Planner.
 */
public class LogicP {
    private StorageFileP storage;
    private Planner planner;

    /**
     * The list of person shown to the user most recently.
     */
    private HashMap<LocalDate, ? extends ReadOnlyDay> lastShownDays = null;
    private ArrayList<Slot> lastShownSlots = null;

    public LogicP() throws Exception {
        setStorage(initializeStorage());
        setPlanner(storage.load());
    }

    public LogicP(StorageFileP storageFile, Planner planner) {
        setStorage(storageFile);
        setPlanner(planner);
    }

    public void setStorage(StorageFileP storage) {
        this.storage = storage;
    }

    public void setPlanner(Planner planner) {
        this.planner = planner;
    }

    /**
     * Creates the StorageFile object based on the user specified path (if any) or the default storage path.
     *
     * @throws StorageFileP.InvalidStorageFilePathException if the target file path is incorrect.
     */
    private StorageFileP initializeStorage() throws StorageFileP.InvalidStorageFilePathException {
        return new StorageFileP();
    }

    public String getStorageFilePath() {
        return storage.getPath();
    }

    /**
     * Unmodifiable view of the current last shown list.
     */
    public HashMap<LocalDate, ? extends ReadOnlyDay> getLastShownDays() {
        return lastShownDays;
    }

    protected void setLastShownDays(HashMap<LocalDate, ? extends ReadOnlyDay> days) {
        lastShownDays = days;
    }

    protected void setLastShownSlots(ArrayList<Slot> slots) {
        lastShownSlots = slots;
    }

    /**
     * Parses the user command, executes it, and returns the result.
     *
     * @throws Exception if there was any problem during command execution.
     */
    public CommandResultP execute(String userCommandText) throws Exception {
        CommandP command = new ParserP().parseCommand(userCommandText);
        CommandResultP result = execute(command);
        recordResult(result);
        return result;
    }

    /**
     * Executes the command, updates storage, and returns the result.
     *
     * @param command user command
     * @return result of the command
     * @throws Exception if there was any problem during command execution.
     */
    private CommandResultP execute(CommandP command) throws Exception {
        command.setData(planner, lastShownDays);
        CommandResultP result = command.execute();
        storage.save(planner);
        return result;
    }

    /**
     * Updates the {@link #lastShownDays} if the result contains a list of Days.
     */
    private void recordResult(CommandResultP result) {
        final Optional<HashMap<LocalDate, ? extends ReadOnlyDay>> days = result.getRelevantDays();
        if (days.isPresent()) {
            lastShownDays = days.get();
        }
    }
}
