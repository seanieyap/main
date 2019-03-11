package planmysem.data.tag;

import planmysem.data.exception.IllegalValueException;

/**
 * Represents a Tag in the Planner.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class TagP {

    private static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric";
    private static final String VALIDATION_REGEX = ".+";

    public final String value;

    /**
     * Validates given value.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public TagP(String name) throws IllegalValueException {
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
        this.value = name;
    }

    /**
     * Returns true if a given value is a valid.
     */
    private static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagP // instanceof handles nulls
                && this.value.equals(((TagP) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return '[' + value + ']';
    }

}
