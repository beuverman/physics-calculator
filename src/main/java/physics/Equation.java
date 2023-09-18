package physics;

import collections.BinaryTreeNode;
import java.util.Stack;
import static physics.TokenType.*;

public class Equation extends collections.LinkedBinaryTree<EquationTreeOp> {
    //low to high precedence
    private static final String[] OPERATORS = {"+-", "*/", "^"};

    public Equation() {
        super();
    }

    public Equation(EquationTreeOp element, Equation leftSubtree, Equation rightSubtree) {
        root = new BinaryTreeNode<>(element, leftSubtree, rightSubtree);
    }

    public Equation(String equation) {
        parseEquation(equation);
    }

    public Quantity evaluate() {
        return evaluateNode(root);
    }

    //Implementation of shunting yard algorithm
    private void parseEquation(String equation) {
        String[] tokens = equation.split(" ");
        TokenType type;
        Equation right;
        Stack<Equation> output = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            type = identifyToken(token);

            if (type == NUMBER) {
                output.push(new Equation(new EquationTreeOp(new Quantity(token)), null, null));
            }
            else if (type == OPERATOR) {
                while (!operators.isEmpty() && identifyToken(operators.peek()) != LBRACKET && precedence(operators.peek(), token) >= 0) {
                    right = output.pop();
                    output.push(new Equation(new EquationTreeOp(operators.pop()), output.pop(), right));
                }
                operators.push(token);
            }
            else if (type == FUNCTION) {

            }
            else if (type == LBRACKET) {
                operators.push(token);
            }
            else if (type == RBRACKET) {
                while (!operators.isEmpty() && identifyToken(operators.peek()) != LBRACKET) {
                    right = output.pop();
                    output.push(new Equation(new EquationTreeOp(operators.pop()), output.pop(), right));
                }
                operators.pop();

                if (!operators.isEmpty() && identifyToken(operators.peek()) == FUNCTION) {
                    right = output.pop();
                    output.push(new Equation(new EquationTreeOp(operators.pop()), output.pop(), right));
                }
            }
        }

        while (operators.size() > 1) {
            right = output.pop();
            output.push(new Equation(new EquationTreeOp(operators.pop()), output.pop(), right));
        }

        right = output.pop();
        root = new BinaryTreeNode<>(new EquationTreeOp(operators.pop()), output.pop(), right);

    }

    private static TokenType identifyToken(String token) {
        if ("(".equals(token))
            return LBRACKET;
        if (")".equals(token))
            return RBRACKET;
        if (token.length() == 1)
            for (String operatorGroup : OPERATORS)
                if (operatorGroup.contains(token))
                    return OPERATOR;

        return NUMBER;
    }

    private static int precedence(String op1, String op2) {
        int a = 0, b = 0;

        for (int i = 0; i < 2; i++) {
            if (OPERATORS[i].contains(op1))
                a = i;
            if (OPERATORS[i].contains(op2))
                b = i;
        }

        return a - b;
    }

    public Quantity evaluateNode(BinaryTreeNode<EquationTreeOp> root) {
        Quantity ret, left, right;
        EquationTreeOp temp;

        temp = root.getElement();

        if (temp.isOperator()) {
            left = evaluateNode(root.getLeft());
            right = evaluateNode(root.getRight());
            ret = computeTerm(temp.getOperator(), left, right);
        }
        else
            ret = temp.getValue();

        return ret;
    }

    private static Quantity computeTerm(char operator, Quantity left, Quantity right) {
        return switch (operator) {
            case '+' -> left.add(right);
            case '-' -> left.subtract(right);
            case '*' -> left.multiply(right);
            case '/' -> left.divide(right);
            case '^' -> left.pow(right);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    public String toString() {
        return "(" + root.getLeft().toString() + root + root.getRight().toString() + ")";
    }
}