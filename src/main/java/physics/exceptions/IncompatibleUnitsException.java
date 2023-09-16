package physics.exceptions;

public class IncompatibleUnitsException extends RuntimeException {
    public IncompatibleUnitsException(String units1, String units2) {
        super(units1 + " and " + units2 + " are incompatible units");
    }
}
