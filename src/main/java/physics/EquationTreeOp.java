package physics;

public class EquationTreeOp {
    private boolean isOperator;
    private char operator;
    private Quantity value;

    public EquationTreeOp(Quantity value) {
        isOperator = false;
        this.value = value;
    }

    public EquationTreeOp(char operator) {
        isOperator = true;
        this.operator = operator;
    }

    public EquationTreeOp(String operator) {
        this(operator.charAt(0));
    }

    public boolean isOperator() {
        return isOperator;
    }

    public char getOperator() {
        return operator;
    }

    public Quantity getValue() {
        return value;
    }

    public String toString()
    {
        if (isOperator)
            return Character.toString(operator);
        else
            return value.toString();
    }
}
