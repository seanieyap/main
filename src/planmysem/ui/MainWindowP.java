package planmysem.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import planmysem.commands.CommandResultP;
import planmysem.commands.ExitCommand;
import planmysem.common.Messages;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.logic.LogicP;

/**
 * Main Window of the GUI.
 */
public class MainWindowP {

    private LogicP logic;
    private Stoppable mainApp;
    @FXML
    private TextArea outputConsole;
    @FXML
    private TextField commandInput;

    public MainWindowP() {
    }

    public void setLogic(LogicP logic) {
        this.logic = logic;
    }

    public void setMainApp(Stoppable mainApp) {
        this.mainApp = mainApp;
    }


    /**
     * TODO: Add Javadoc comment.
     */
    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();
            CommandResultP result = logic.execute(userCommandText);
            if (isExitCommand(result)) {
                exitApp();
                return;
            }
            displayResult(result);
            clearCommandInput();
        } catch (Exception e) {
            display(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void exitApp() throws Exception {
        mainApp.stop();
    }

    /**
     * Returns true of the result given is the result of an exit command
     */
    private boolean isExitCommand(CommandResultP result) {
        return result.feedbackToUser.equals(ExitCommand.MESSAGE_EXIT_ACKNOWEDGEMENT);
    }

    /**
     * Clears the command input box
     */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /**
     * Clears the output display area
     */
    public void clearOutputConsole() {
        outputConsole.clear();
    }

    /**
     * Displays the result of a command execution to the user.
     */
    public void displayResult(CommandResultP result) {
        clearOutputConsole();
        final Optional<HashMap<LocalDate, ? extends ReadOnlyDay>> resultDays = result.getRelevantDays();
        if (resultDays.isPresent()) {
            display(resultDays.get());
        }
        display(result.feedbackToUser);
    }

    /**
     * TODO: Add Javadoc comment.
     */
    public void displayWelcomeMessage(String version, String storageFilePath) {
        String storageFileInfo = String.format(Messages.MESSAGE_USING_STORAGE_FILE, storageFilePath);
        display(Messages.MESSAGE_WELCOME, version, Messages.MESSAGE_PROGRAM_LAUNCH_ARGS_USAGE, storageFileInfo);
    }

    /**
     * Displays the list of persons in the output display area, formatted as an indexed list.
     * Private contact details are hidden.
     */
    private void display(HashMap<LocalDate, ? extends ReadOnlyDay> days) {
        display(new Formatter().format(days));
    }

    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    private void display(String... messages) {
        outputConsole.setText(outputConsole.getText() + new Formatter().format(messages));
    }

}
