package planmysem.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import planmysem.Main;
import planmysem.logic.Logic;

/**
 * The GUI of the App
 */
public class Gui {

    /**
     * Offset required to convert between 1-indexing and 0-indexing.
     */
    public static final int DISPLAYED_INDEX_OFFSET = 1;

    public static final int INITIAL_WINDOW_WIDTH = 800;
    public static final int INITIAL_WINDOW_HEIGHT = 600;
    private final Logic logic;

    private MainWindow mainWindow;
    private String version;

    public Gui(Logic logic, String version) {
        this.logic = logic;
        this.version = version;
    }

    /**
     * TODO: Add Javadoc comment.
     */
    public void start(Stage stage, Stoppable mainApp) throws IOException {
        mainWindow = createMainWindowP(stage, mainApp);
        mainWindow.displayWelcomeMessage(version, logic.getStorageFilePath());
    }

    /**
     * TODO: Add Javadoc comment.
     */
    private MainWindow createMainWindowP(Stage stage, Stoppable mainApp) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("ui/mainwindow.fxml"));

        stage.setTitle(version);
        stage.setScene(new Scene(loader.load(), INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT));
        stage.show();
        mainWindow = loader.getController();
        mainWindow.setLogic(logic);
        mainWindow.setMainApp(mainApp);
        return mainWindow;
    }
}