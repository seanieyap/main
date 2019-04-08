//package planmysem.logic.Commands;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import planmysem.common.Clock;
//import planmysem.logic.CommandHistory;
//import planmysem.logic.commands.CommandResult;
//import planmysem.logic.commands.ExportCommand;
//import planmysem.logic.commands.FindCommand;
//import planmysem.model.Model;
//import planmysem.model.ModelManager;
//import planmysem.model.semester.IcsSemester;
//
//public class ExportCommandTest {
//    private Model model = new ModelManager();
//    private CommandHistory commandHistory = new CommandHistory();
//
//    @Before
//    public void setup() {
//        Clock.set("2019-01-14T10:00:00Z");
//    }
//
//    @Test
//    public void execute_export_success() throws IOException {
//        IcsSemester semester = new IcsSemester(model.getPlanner().getSemester());
//        CommandResult commandResult = new ExportCommand("C:\Users\sly_1\Documents\PlanMySem\test\data\ImportExportTest\ExportTest").execute(model, commandHistory);
//
//        String expectedIcs = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nEND:VCALENDAR\r\n";
//        String actualIcs = semester.toString();
//        Assert.assertEquals(actualIcs, expectedIcs);
//    }
//
//    static String readFile(String path) throws IOException {
//        byte[] encoded = Files.readAllBytes(Paths.get(path));
//        return new String(encoded);
//    }
//}
