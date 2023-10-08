package physics.exceptions;

/**
 * Exception thrown when an operation is called on Quantities whose units are incompatible
 */
public class InvalidDimensionException extends RuntimeException {
    /**
     * Throws a RuntimeException when the dimensions are incompatible with the operation
     */
    public InvalidDimensionException() {
        super("Invalid dimension");
    }
}