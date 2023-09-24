package physics;

import collections.BinaryTreeNode;

import java.util.Stack;
import static physics.TokenType.*;

public class Equation extends collections.LinkedBinaryTree<Token> {
    //low to high precedence

    public Equation() {
        super();
    }

    public Equation(Token element, Equation leftSubtree, Equation rightSubtree) {
        root = new BinaryTreeNode<>(element, leftSubtree, rightSubtree);
    }

    public Equation(Token[] equation) {
        parseEquation(equation);
    }

    public Quantity evaluate() {
        return evaluateNode(root);
    }

    //Implementation of shunting yard algorithm
    private void parseEquation(Token[] tokens) {
        TokenType type;
        Equation right;
        Stack<Equation> output = new Stack<>();
        Stack<Token> operators = new Stack<>();

        for (Token token : tokens) {
            type = token.getType();

            if (type == NUMBER || type == UNIT) {
                output.push(new Equation(token, null, null));
            }
            else if (type == OPERATOR) {
                while (!operators.isEmpty() && operators.peek().getType() != LBRACKET
                        && precedence(operators.peek().getOperator(), token.getOperator()) >= 0) {
                    right = output.pop();
                    output.push(new Equation(operators.pop(), output.pop(), right));
                }
                operators.push(token);
            }
            else if (type == FUNCTION) {
                operators.push(token);
            }
            else if (type == LBRACKET) {
                operators.push(token);
            }
            else if (type == RBRACKET) {
                while (!operators.isEmpty() && operators.peek().getType() != LBRACKET) {
                    right = output.pop();
                    output.push(new Equation(operators.pop(), output.pop(), right));
                }
                operators.pop();

                if (!operators.isEmpty() && operators.peek().getType() == FUNCTION) {
                    output.push(new Equation(operators.pop(), output.pop(), null));
                }
            }
        }

        while (!operators.isEmpty()) {
            right = output.pop();
            output.push(new Equation(operators.pop(), output.pop(), right));
        }

        root = output.pop().root;
    }

    private static int precedence(char op1, char op2) {
        int a = 0, b = 0;

        for (int i = 0; i < Token.OPERATORS.length; i++) {
            if (Token.OPERATORS[i].indexOf(op1) != -1)
                a = i;
            if (Token.OPERATORS[i].indexOf(op2) != -1)
                b = i;
        }

        return a - b;
    }

    public Quantity evaluateNode(BinaryTreeNode<Token> root) {
        Quantity ret, left, right;
        Token temp;

        temp = root.getElement();

        if (temp.isOperator()) {
            left = evaluateNode(root.getLeft());
            right = evaluateNode(root.getRight());
            ret = computeTerm(temp.getOperator(), left, right);
        }
        else if (temp.isFunction()) {
            ret = computeTerm(temp.getFunction(), evaluateNode(root.getLeft()));
        }
        else
            ret = temp.getValue();

        return ret;
    }

    private static Quantity computeTerm(String function, Quantity x) {
        if (function.equals("sin")) return Quantity.sin(x);
        if (function.equals("cos")) return Quantity.cos(x);
        if (function.equals("tan")) return Quantity.tan(x);

        throw new IllegalStateException("Unexpected value: " + function);
    }

    private static Quantity computeTerm(char operator, Quantity left, Quantity right) {
        return switch (operator) {
            case '+' -> left.add(right);
            case '-' -> left.subtract(right);
            case '*', Token.IMPLICIT_M -> left.multiply(right);
            case '/', Token.IMPLICIT_D -> left.divide(right);
            case '^' -> left.pow(right);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    public String toString() {
        if (root.getLeft() == null && root.getRight() == null)
            return root.getElement().toString();
        if (root.getRight() == null)
            return root.getElement().toString() + "(" + root.getLeft().getElement().toString() + ")";
        return "(" + root.getLeft().toString() + root + root.getRight().toString() + ")";
    }
}