package planmysem.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import planmysem.common.Utils;
import planmysem.data.exception.IllegalValueException;
import planmysem.data.recurrence.Recurrence;
import planmysem.data.semester.Day;
import planmysem.data.semester.Semester;
import planmysem.data.slot.Description;
import planmysem.data.slot.Location;
import planmysem.data.slot.Name;
import planmysem.data.slot.Slot;

/**
 * Imports a .ics file into the Planner.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports a .ics file into the Planner."
            + "\n\tParameters: "
            + "\n\t\tFILENAME";

    public static final String MESSAGE_SUCCESS = "File imported.\n";
    public static final String MESSAGE_FILE_NOT_FOUND = "File not found.\n";
    public static final String MESSAGE_ERROR_IN_READING_FILE = "Error in reading file.\n";

    private final String fileName;
    private int failedImports = 0;

    public ImportCommand(String fileName) {
        this.fileName = fileName.replaceAll("\\s", "");
    }

    @Override
    public CommandResult execute() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(this.fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new CommandResult(MESSAGE_FILE_NOT_FOUND);
        }
        BufferedReader br = new BufferedReader(fileReader);


        try {
            String sCurrentLine = br.readLine();
            while (!(sCurrentLine.equals("END:VCALENDAR"))) {
                sCurrentLine = br.readLine();
                if (sCurrentLine.equals("BEGIN:VEVENT")) {
                    LocalDate date = null;
                    String name = null;
                    String location = null;
                    String description = null;
                    LocalTime startTime = null;
                    int duration = 0;
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

                    while (!(sCurrentLine.equals("END:VEVENT"))) {
                        sCurrentLine = br.readLine();
                        String[] sSplit = sCurrentLine.split(":");
                        switch (sSplit[0]) {
                        case "SUMMARY":
                            name = sSplit[1];
                            break;

                        case "DTSTART":
                            date = LocalDate.parse(sSplit[1], dateFormat);
                            startTime = LocalTime.parse(sSplit[1], dateFormat);
                            break;

                        case "DTEND":
                            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                            LocalDateTime endDateTime = LocalDateTime.parse(sSplit[1], dateFormat);
                            duration = (int) startDateTime.until(endDateTime, ChronoUnit.MINUTES);
                            break;

                        case "LOCATION":
                            location = sSplit[1];
                            break;

                        case "DESCRIPTION":
                            description = sSplit[1];
                            break;

                        default:
                            break;

                        }
                    }
                    try {
                        Slot slot = new Slot(new Name(name), new Location(location), new Description(description),
                                startTime, duration, Utils.parseTags(null));
                        Recurrence recurrence = new Recurrence(null, date);
                        Set<LocalDate> dates = recurrence.generateDates(planner.getSemester());
                        Map<LocalDate, Day> days = new TreeMap<>();

                        for (LocalDate singleDate : dates) {
                            try {
                                days.put(singleDate, planner.addSlot(date, slot));
                                System.out.println("pass");
                            } catch (Semester.DateNotFoundException dnfe) {
                                this.failedImports++;
                            }
                        }
                    } catch (IllegalValueException e) {
                        return new CommandResult(MESSAGE_ERROR_IN_READING_FILE);
                    }


                }
            }
        } catch (IOException e) {
            return new CommandResult(MESSAGE_ERROR_IN_READING_FILE);
        }
        if (this.failedImports == 0) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_SUCCESS + this.failedImports + " events failed to import.\n");
        }
    }
}


