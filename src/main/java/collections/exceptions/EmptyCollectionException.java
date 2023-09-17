/*
EmptyCollectionException.java

Brandon Euverman
T00741729
COMP 2231 Assignment 4

Represents the situation in which a collection is empty
Found in chapter 20 of Java Foundations
 */

package collections.exceptions;

/**
 * Represents the situation in which a collection is empty.
 *
 * @author Java Foundations
 * @version 4.0
 */
public class EmptyCollectionException extends RuntimeException {
    //Sets up this exception with an appropriate message
    public EmptyCollectionException (String collection) {
        super ("The " + collection + " is empty.");
    }
}
