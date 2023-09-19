package physics;

import static physics.TokenType.*;

public class EquationTreeOp {
    private TokenType type;
    private String operator;
    private Quantity value;

    public EquationTreeOp(Quantity value) {
        type = NUMBER;
        this.value = value;
    }

    public EquationTreeOp(char operator) {
        type = OPERATOR;
        this.operator = String.valueOf(operator);
    }

    public EquationTreeOp(String operator) {
        if (operator.length() == 1)
            type = OPERATOR;
        else
            type = FUNCTION;

        this.operator = operator;
    }

    public boolean isOperator() {
        return type == OPERATOR;
    }

    public boolean isFunction() {
        return type == FUNCTION;
    }

    public char getOperator() {
        return operator.charAt(0);
    }

    public String getFunction() {
        return operator;
    }

    public Quantity getValue() {
        return value;
    }

    public String toString()
    {
        if (type == NUMBER)
            return value.toString();
        else
            return operator;
    }
}