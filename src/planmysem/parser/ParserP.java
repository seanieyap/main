package planmysem.parser;

import static planmysem.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import planmysem.commands.AddCommandP;
import planmysem.commands.Command;
import planmysem.commands.CommandP;
import planmysem.commands.DeleteCommand;
import planmysem.commands.EditCommandP;
import planmysem.commands.ExitCommandP;
import planmysem.commands.FindCommand;
import planmysem.commands.HelpCommand;
import planmysem.commands.HelpCommandP;
import planmysem.commands.IncorrectCommand;
import planmysem.commands.IncorrectCommandP;
import planmysem.commands.ListCommandP;
import planmysem.commands.ViewAllCommand;
import planmysem.commands.ViewCommand;
import planmysem.common.Utils;
import planmysem.data.exception.IllegalValueException;

/**
 * Parses user input.
 */
public class ParserP {

    public static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    public static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final String PARAMETER_START_TIME = "st";


    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Extracts the new Slot's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings =
                Arrays.asList(
                        tagArguments.replaceFirst(" t/", "")
                                .split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Extracts the new Slot's recursive arguments from the add command's recurse arguments string.
     * Merges duplicate recursive strings.
     */
    private static Set<String> getRecurrencesFromArgs(String recursiveArguments) throws IllegalValueException {
        // no tags
        if (recursiveArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(recursiveArguments.replaceFirst(" r/", "").split(" r/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public CommandP parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {

        case AddCommandP.COMMAND_WORD:
            return prepareAdd(arguments);

        case EditCommandP.COMMAND_WORD:
            return prepareEdit(arguments);

        case ListCommandP.COMMAND_WORD:
            return prepareList(arguments);

        case ExitCommandP.COMMAND_WORD:
            return new ExitCommandP();

        case HelpCommandP.COMMAND_WORD: // Fallthrough

        default:
            return new HelpCommandP();
        }
    }

    /**
     * Parses arguments in the context of the add Slot command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private CommandP prepareAdd(String args) {
        HashMap<String, Set<String>> arguments = getSomething(args);
        String name;

        try {
            name = getFirstArgument(args);
        } catch (ParseException pe) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        if (arguments.isEmpty() || name.isEmpty() || arguments.get("d") == null
                || arguments.get("st") == null || arguments.get("et") == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Either date or day must be present
        String dateOrDay = getFirstInSet(arguments.get("d"));
        int day = Utils.getDay(dateOrDay);
        LocalDate date = null;
        if (day == 0) {
            date = Utils.parseDate(dateOrDay);
        }
        if (day == 0 && date == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Start time is mandatory
        String stringStartTime = getFirstInSet(arguments.get("et"));
        LocalTime startTime = Utils.parseTime(stringStartTime);
        if (startTime == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // determine if "end time" is a duration or time
        String stringEndTime = getFirstInSet(arguments.get("st"));
        int duration;
        try {
            duration = Integer.parseInt(stringEndTime);
        } catch (NumberFormatException nfe) {
            duration = -1;
        }

        LocalTime endTime = null;
        if (duration == -1) {
            endTime = Utils.parseTime(stringEndTime);
        }

        if (duration == -1 && endTime == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Description is not mandatory and can be null
        String description = getFirstInSet(arguments.get("des"));

        // Location is not mandatory and can be null
        String location = getFirstInSet(arguments.get("l"));

        // Tags is not mandatory and can be null
        Set<String> tags = arguments.get("t");

        // Recurrences is not mandatory and can be null
        Set<String> recurrences = arguments.get("r");

        try {
            if (duration != -1) {
                // parse duration string into int
                if (day != -1) {
                    return new AddCommandP(
                            day,
                            name,
                            location,
                            description,
                            startTime,
                            duration,
                            tags,
                            recurrences
                    );
                } else {
                    return new AddCommandP(
                            date,
                            name,
                            location,
                            description,
                            startTime,
                            duration,
                            tags,
                            recurrences
                    );
                }
            } else {
                if (day != -1) {
                    return new AddCommandP(
                            day,
                            name,
                            location,
                            description,
                            startTime,
                            endTime,
                            tags,
                            recurrences
                    );
                } else {
                    return new AddCommandP(
                            date,
                            name,
                            location,
                            description,
                            startTime,
                            endTime,
                            tags,
                            recurrences
                    );
                }
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommandP(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the add Slot command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private CommandP prepareEdit(String args) {
        HashMap<String, Set<String>> arguments = getSomething(args);
        String name;

        try {
            name = getFirstArgument(args);
        } catch (ParseException pe) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        if (arguments.isEmpty() || name.isEmpty() || arguments.get("d") == null
                || arguments.get("st") == null || arguments.get("et") == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Either date or day must be present
        String dateOrDay = getFirstInSet(arguments.get("d"));
        int day = Utils.getDay(dateOrDay);
        LocalDate date = null;
        if (day == 0) {
            date = Utils.parseDate(dateOrDay);
        }
        if (day == 0 && date == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Start time is mandatory
        String stringStartTime = getFirstInSet(arguments.get("et"));
        LocalTime startTime = Utils.parseTime(stringStartTime);
        if (startTime == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // determine if "end time" is a duration or time
        String stringEndTime = getFirstInSet(arguments.get("st"));
        int duration;
        try {
            duration = Integer.parseInt(stringEndTime);
        } catch (NumberFormatException nfe) {
            duration = -1;
        }

        LocalTime endTime = null;
        if (duration == -1) {
            endTime = Utils.parseTime(stringEndTime);
        }

        if (duration == -1 && endTime == null) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandP.MESSAGE_USAGE));
        }

        // Description is not mandatory and can be null
        String description = getFirstInSet(arguments.get("des"));

        // Location is not mandatory and can be null
        String location = getFirstInSet(arguments.get("l"));

        // Tags is not mandatory and can be null
        Set<String> tags = arguments.get("t");

        // Recurrences is not mandatory and can be null
        Set<String> recurrences = arguments.get("r");

        try {
            if (duration != -1) {
                // parse duration string into int
                if (day != -1) {
                    return new AddCommandP(
                            day,
                            name,
                            location,
                            description,
                            startTime,
                            duration,
                            tags,
                            recurrences
                    );
                } else {
                    return new AddCommandP(
                            date,
                            name,
                            location,
                            description,
                            startTime,
                            duration,
                            tags,
                            recurrences
                    );
                }
            } else {
                if (day != -1) {
                    return new AddCommandP(
                            day,
                            name,
                            location,
                            description,
                            startTime,
                            endTime,
                            tags,
                            recurrences
                    );
                } else {
                    return new AddCommandP(
                            date,
                            name,
                            location,
                            description,
                            startTime,
                            endTime,
                            tags,
                            recurrences
                    );
                }
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommandP(ive.getMessage());
        }
    }


    /**
     * Parses arguments in the context of the add Slot command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private CommandP prepareList(String args) {
        HashMap<String, Set<String>> arguments = getSomething(args);


        // Validate arg string format
        if (args.isEmpty() || arguments.isEmpty()) {
            return new IncorrectCommandP(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommandP.MESSAGE_USAGE));
        }

        // TODO: prepare list
        //        try {
        // TODO: add exception handling
        return new ListCommandP(arguments.get(PARAMETER_START_TIME));
        //        } catch (IllegalValueException ive) {
        //            return new IncorrectCommandP(ive.getMessage());
        //        }
    }


    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {
        try {
            final int targetIndex = parseArgsAsDisplayedIndex(args);
            return new DeleteCommand(targetIndex);
        } catch (ParseException | NumberFormatException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments in the context of the view command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareView(String args) {

        try {
            final int targetIndex = parseArgsAsDisplayedIndex(args);
            return new ViewCommand(targetIndex);
        } catch (ParseException | NumberFormatException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses arguments in the context of the view all command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareViewAll(String args) {

        try {
            final int targetIndex = parseArgsAsDisplayedIndex(args);
            return new ViewAllCommand(targetIndex);
        } catch (ParseException | NumberFormatException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewAllCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses the given arguments string as a single index number.
     *
     * @param args arguments string to parse as index number
     * @return the parsed index number
     * @throws ParseException        if no region of the args string could be found for the index
     * @throws NumberFormatException the args string region is not a valid number
     */
    private int parseArgsAsDisplayedIndex(String args) throws ParseException, NumberFormatException {
        final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException("Could not find index number to parse");
        }
        return Integer.parseInt(matcher.group("targetIndex"));
    }

    /**
     * Parses arguments in the context of the find person command.
     *
     * @param args partial args string
     * @return the arguments sorted by its relevant options
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

    /**
     * Parses arguments in the context of the add slots command.
     *
     * @return the prepared command
     */
    private static HashMap<String, Set<String>> getSomething(String args) {
        String parameters = args.trim();
        String parameter;
        String option;
        int t;
        HashMap<String, Set<String>> result = new HashMap<>();
        while (true) {
            t = parameters.indexOf('/');
            if (t == -1) {
                break;
            }

            option = parameters.substring(0, t).trim();
            if (option.contains(" ")) {
                parameters = parameters.substring(option.lastIndexOf(" "));
                continue;
            }

            parameters = parameters.substring(t + 1).trim();

            if (parameters.indexOf('/') != -1) {
                parameter = parameters.substring(0, parameters.indexOf('/'));
                parameter = parameter.substring(0, parameter.lastIndexOf(" "));
            } else {
                parameter = parameters;
            }

            if (result.get(option) == null) {
                result.put(option, new HashSet<>(Collections.singletonList(parameter.trim())));
            } else {
                Set<String> ss = result.get(option);
                ss.add(parameter.trim());
                result.replace(option, ss);
            }

            if (parameters.length() == 0) {
                break;
            }
            parameters = parameters.substring(parameter.length());
        }

        return result;
    }

    /**
     * Parses arguments in the context of the find person command.
     *
     * @return the a string that is trimmed
     */
    private String getFirstArgument(String args) throws ParseException {
        String result = args;

        // test if firstArgument is present
        if (result.trim().length() == 0 || result.substring(result.indexOf('/')).indexOf(' ') == -1) {
            throw new ParseException("");
        } else if (result.indexOf('/') != -1) {
            result = result.substring(0, result.indexOf('/'));
            return result.substring(0, result.lastIndexOf(" ")).trim();
        } else {
            return result.trim();
        }
    }

    private String getFirstInSet(Set<String> set) {
        if (set == null || set.size() == 0) {
            return null;
        }
        return set.stream().findFirst().get();
    }

    /**
     * Signals that the user input could not be parsed.
     */
    public static class ParseException extends Exception {
        ParseException(String message) {
            super(message);
        }
    }
}
