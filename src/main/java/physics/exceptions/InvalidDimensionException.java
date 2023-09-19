package physics.exceptions;

public class InvalidDimensionException extends RuntimeException {
    public InvalidDimensionException() {
        super("Invalid exponent");
    }
}