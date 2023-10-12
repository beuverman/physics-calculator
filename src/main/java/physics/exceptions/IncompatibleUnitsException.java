package physics.exceptions;

/**
 * Exception thrown when an operation is called on Quantities whose units are incompatible
 */
public class IncompatibleUnitsException extends RuntimeException {
    /**
     * Throws a RunTimeException when the units are incompatible
     * @param units1 String representation of the units of the first operand
     * @param units2 String representation of the units of the second operand
     */
    public IncompatibleUnitsException(String units1, String units2) {
        super(units1 + " and " + units2 + " are incompatible units");
    }
}
