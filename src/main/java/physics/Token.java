package physics;

import static physics.TokenType.*;

/**
 * Represents a token in an equation
 */
public class Token {
    /**
     * Type associated with the token
     */
    public final TokenType type;
    private String token;
    private Quantity value;

    /**
     * Creates a token from the given string representation
     * @param token The string to be made into a token
     * @param type The type of the token
     */
    public Token(String token, TokenType type) {
        this.type = type;

        if (type == NUMBER || type == UNIT)
            value = new Quantity(token);
        else
            this.token = token;
    }

    /**
     * Creates a token that represents the given quantity
     * @param token Quantity to be made into a token
     */
    public Token(Quantity token) {
        type = NUMBER;
        value = token;
    }

    /**
     * Creates a token that represent the given operation
     * @param token Operation to be made into a token
     */
    public Token(char token) {
        this(String.valueOf(token), OPERATOR);
    }

    /**
     * Checks whether this token represents an operator
     * @return Returns true if token is an operator, false otherwise
     */
    public boolean isOperator() {
        return type == OPERATOR;
    }

    /**
     * Checks whether this token represents a function
     * @return Returns true if token is a function, false otherwise
     */
    public boolean isFunction() {
        return type == FUNCTION;
    }

    /**
     * Checks whether this token represents a variable
     * @return Returns true if token is a variable, false otherwise
     */
    public boolean isVariable() {
        return type == VARIABLE;
    }

    /**
     * Gets the operator associated with this token
     * @return Returns the character representation of the operator
     */
    public char getOperator() {
        if (!isOperator())
            throw new RuntimeException("Token not of type operator");

        return token.charAt(0);
    }

    /**
     * Gets the function associated with this token
     * @return Returns the string representation of the function
     */
    public String getFunction() {
        if (!isFunction())
            throw new RuntimeException("Token not of type function");

        return token;
    }

    /**
     * Gets the variable associated with this token
     * @return Returns string representation of the variable
     */
    public String getVariable() {
        if (!isVariable())
            throw new RuntimeException("Token not of type variable");

        return token;
    }

    /**
     * Gets the Quantity associated with this token
     * @return Returns the Quantity held in this token
     */
    public Quantity getValue() {
        if (value == null)
            throw new RuntimeException("Token not of type number");

        return value;
    }


    /**
     * Returns a string representation of the Token
     * @return Returns the toString function of the element held by the token
     */
    public String toString()
    {
        if (type == NUMBER || type == UNIT)
            return value.toString();
        else
            return token;
    }
}