package physics;

import static physics.TokenType.*;

/**
 * Represents a token in an equation
 */
public class Token {
    public static final char IMPLICIT_M = 9994;
    public static final char IMPLICIT_D = 9995;
    public static final String[] OPERATORS = {"+-", "*/", new String(new char[]{IMPLICIT_M, IMPLICIT_D}), "^"};

    /**
     * Type associated with the token
     */
    public final TokenType type;
    private String token;
    private Quantity value;

    /**
     * Creates a token from the given string representation
     * @param token The string to be made into a token
     */
    public Token(String token) {
        type = identifyToken(token);

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
        this(String.valueOf(token));
    }

    /**
     * Checks whether this token represents an operator
     * @return Returns true of token is an operator, false otherwise
     */
    public boolean isOperator() {
        return type == OPERATOR;
    }

    /**
     * Checks whether this token represents a function
     * @return Returns true of token is a function, false otherwise
     */
    public boolean isFunction() {
        return type == FUNCTION;
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
     * Gets the Quantity associated with this token
     * @return Returns the Quantity held in this token
     */
    public Quantity getValue() {
        if (value == null)
            throw new RuntimeException("Token not of type number");

        return value;
    }

    /**
     * Identifies what type of token the given string is
     * @param token String to be identified
     * @return Returns the TokenType of the argument
     */
    public static TokenType identifyToken(String token) {
        if (token.equals("("))
            return LBRACKET;
        if (token.equals(")"))
            return RBRACKET;
        if (token.equals("sin") || token.equals("cos") || token.equals("tan"))
            return FUNCTION;
        if (token.length() == 1)
            for (String operatorGroup : OPERATORS)
                if (operatorGroup.contains(token))
                    return OPERATOR;
        if (Character.isAlphabetic(token.charAt(0)))
            return UNIT;

        return NUMBER;
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