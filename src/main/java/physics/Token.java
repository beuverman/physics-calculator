package physics;

import static physics.TokenType.*;

public class Token {
    public static final char IMPLICIT_M = 9994;
    public static final char IMPLICIT_D = 9995;
    public static final String[] OPERATORS = {"+-", "*/", new String(new char[]{IMPLICIT_M, IMPLICIT_D}), "^"};

    private TokenType type;
    private String token;
    private Quantity value;

    public Token(String token) {
        type = identifyToken(token);

        if (type == NUMBER || type == UNIT)
            value = new Quantity(token);
        else
            this.token = token;
    }

    public Token(Quantity token) {
        type = NUMBER;
        value = token;
    }

    public Token(char token) {
        this(String.valueOf(token));
    }

    public TokenType getType() {
        return type;
    }

    public boolean isOperator() {
        return type == OPERATOR;
    }

    public boolean isFunction() {
        return type == FUNCTION;
    }

    public char getOperator() {
        return token.charAt(0);
    }

    public String getFunction() {
        return token;
    }

    public Quantity getValue() {
        return value;
    }

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

    public String toString()
    {
        if (type == NUMBER || type == UNIT)
            return value.toString();
        else
            return token;
    }
}