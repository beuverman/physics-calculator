/*
ElementNotFoundException.java

Brandon Euverman
T00741729
COMP 2231 Assignment 4

Represents the situation in which a target element is not present in a collection
Found in chapter 20 of Java Foundations
 */

package collections.exceptions;

public class ElementNotFoundException extends RuntimeException {
    //Sets up this exception with an appropriate message
    public ElementNotFoundException (String collection) {
        super ("The target element is not in this " + collection);
    }
}
