package planmysem.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.util.Pair;
import planmysem.commands.CommandP;
import planmysem.commands.CommandResultP;
import planmysem.data.Planner;
import planmysem.data.slot.ReadOnlySlot;
import planmysem.parser.ParserP;
import planmysem.storage.StorageFileP;

/**
 * Represents the main Logic of the Planner.
 */
public class LogicP {
    private StorageFileP storage;
    private Planner planner;

    /**
     * The list of Slots shown to the user most recently.
     */
    private List<Pair<LocalDate, ? extends ReadOnlySlot>> lastShownSlots;

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
    public List<Pair<LocalDate, ? extends ReadOnlySlot>> getLastShownSlots() {
        return lastShownSlots;
    }

    protected void setLastShownSlots(List<Pair<LocalDate, ? extends ReadOnlySlot>> slots) {
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
        command.setData(planner, lastShownSlots);
        CommandResultP result = command.execute();
        storage.save(planner);
        return result;
    }

    /**
     * Updates the {@link #lastShownSlots} if the result contains a list of Days.
     * TODO:
     */
    private void recordResult(CommandResultP result) {
        final Optional<List<Pair<LocalDate, ? extends ReadOnlySlot>>> slots = result.getRelevantSlots();
        if (slots.isPresent()) {
            lastShownSlots = slots.get();
        }
    }
}
