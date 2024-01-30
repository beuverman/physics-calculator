package physics;

import collections.BinaryTreeNode;

import java.util.List;
import java.util.Stack;
import static physics.TokenType.*;

/**
 * Computes a series of quantities, operators, and functions
 * Uses an expression tree representation
 */
public class Equation extends collections.LinkedBinaryTree<Token> {

    /**
     * Creates an equation from a list of tokens in prefix notation
     * @param equation The list of tokens to form the equation from
     */
    public Equation(List<Token> equation) {
        parseEquation(equation);
    }

    /**
     * Creates an equation with the given element and subtrees
     * @param element The operator that forms the root of the equation
     * @param leftSubtree The left argument of the operator
     * @param rightSubtree The right argument of the operator
     */
    private Equation(Token element, Equation leftSubtree, Equation rightSubtree) {
        root = new BinaryTreeNode<>(element, leftSubtree, rightSubtree);
    }

    /**
     * Creates an equation with a given existing root node
     * @param root The node to form the root of the equation
     */
    private Equation(BinaryTreeNode<Token> root) {
        this.root = root;
    }

    /**
     * Returns the left subtree of the equation
     * @return Returns an equation whose root is the left child of this equation's root
     */
    @Override
    public Equation getLeft() {
        return new Equation(root.getLeft());
    }

    /**
     * Returns the right subtree of the equation
     * @return Returns an equation whose root is the right child of this equation's root
     */
    @Override
    public Equation getRight() {
        return new Equation(root.getRight());
    }

    /**
     * Evaluates the equation
     * @return Returns a Quantity that represents the final evaluation of the equation
     */
    public Quantity evaluate() {
        return evaluateNode(root);
    }

    /**
     * Builds the equation from a list of tokens in prefix equation
     * @param tokens List of tokens to be parsed
     */
    private void parseEquation(List<Token> tokens) {
        TokenType type;
        Equation right;
        Stack<Equation> output = new Stack<>();
        Stack<Token> operators = new Stack<>();

        //Implementation of shunting yard algorithm
        for (Token token : tokens) {
            type = token.type;

            if (type == NUMBER || type == UNIT) {
                output.push(new Equation(token, null, null));
            }
            else if (type == OPERATOR) {
                while (!operators.isEmpty() && operators.peek().type != LBRACKET
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
                while (!operators.isEmpty() && operators.peek().type != LBRACKET) {
                    right = output.pop();
                    output.push(new Equation(operators.pop(), output.pop(), right));
                }
                operators.pop();

                if (!operators.isEmpty() && operators.peek().type == FUNCTION) {
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

    /**
     * Determines which operator has greater precedence
     * @param op1 The first operator to be considered
     * @param op2 The second operator to be considered
     * @return Returns a positive number if op1 has greater precedence, negative if op2 has greater precedence, and 0 if equal
     */
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

    /**
     * Recursively evalutes the subtree with the given root
     * @param root Root of the subtree to be evaluated
     * @return Returns a quantity that is the evaluation of the subtree
     */
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

    /**
     * Computes the result of a given function
     * @param function String representation of the function
     * @param x argument of the function
     * @return Returns f(x)
     */
    private static Quantity computeTerm(String function, Quantity x) {
        return switch (function) {
            case "sin" -> Quantity.sin(x);
            case "cos" -> Quantity.cos(x);
            case "tan" -> Quantity.tan(x);
            default -> throw new IllegalStateException("Unexpected value: " + function);
        };
    }

    /**
     * Computes the result of a given operation
     * @param operator The operation to perform
     * @param left The left operand
     * @param right The right operand
     * @return Returns the result of the operation
     */
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

    /**
     * Checks whether this equation has any subequations
     * @return Returns true if the root has no children, false otherwise
     */
    private boolean isAtom() {
        return getHeight() == 0;
    }

    /**
     * Returns a latex string representation of the equation
     * @return Returns a latex string representation of the equation
     */
    public String toLatexString() {
        if (isAtom()) return root.getElement().getValue().toLatexString();

        String left = getLeft().toLatexString();
        if (getRight().isEmpty()) {
            return root.getElement() + "\\left(" + left + "\\right)";
        }

        String right = getRight().toLatexString();

        return switch (root.getElement().getOperator()) {
            case Token.IMPLICIT_M -> left + right;
            case Token.IMPLICIT_D -> left + "/" + right;
            case '/' -> "\\frac{" + left + "}{" + right + "}";
            case '^' -> (getLeft().isAtom() ? left : ("\\left(" + left + "\\right)")) + "^{" + right + "}";
            default -> left + root.getElement().toString() + right;
        };
    }

    /**
     * Returns a string representation of the equation
     * @return Returns a string representation of the equation
     */
    public String toString() {
        if (isAtom()) return root.getElement().toString();

        String left = getLeft().toString();
        if (getRight().isEmpty()) return root.getElement() + "(" + left + ")";
        if (!getLeft().isAtom()) left = "(" + left + ")";
        String right = getRight().toString();
        if (!getRight().isAtom()) right = "(" + right + ")";

        return left + root.getElement() + right;
    }
}