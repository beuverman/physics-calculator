package physics;

import collections.BinaryTreeNode;

import java.util.*;
import java.util.function.Function;

import static physics.TokenType.*;

/**
 * Computes a series of quantities, operators, and functions
 * Uses an expression tree representation
 */
public class Equation extends collections.LinkedBinaryTree<Token> {
    private final Function<String, Quantity> variables;

    public HashSet<String> variableUsage;
    public String variable;

    /**
     * Creates an equation from a list of tokens in prefix notation
     * @param equation The list of tokens to form the equation from
     */
    public Equation(List<Token> equation, Function<String, Quantity> variables) {
        this.variables = variables;
        variableUsage = new HashSet<>();
        variable = null;
        parseEquation(equation);
    }

    /**
     * Creates an equation with the given element and subtrees
     * @param element The operator that forms the root of the equation
     * @param leftSubtree The left argument of the operator
     * @param rightSubtree The right argument of the operator
     */
    private Equation(Token element, Equation leftSubtree, Equation rightSubtree, Function<String, Quantity> variables) {
        root = new BinaryTreeNode<>(element, leftSubtree, rightSubtree);
        this.variables = variables;
        variable = null;

        variableUsage = new HashSet<>();
        if (leftSubtree != null)
            variableUsage.addAll(leftSubtree.variableUsage);
        if (rightSubtree != null)
            variableUsage.addAll(rightSubtree.variableUsage);
    }

    /**
     * Creates an equation with a given existing root node
     * @param root The node to form the root of the equation
     */
    private Equation(BinaryTreeNode<Token> root, Function<String, Quantity> variables) {
        this.root = root;
        this.variables = variables;
        variable = null;
    }

    /**
     * Returns the left subtree of the equation
     * @return Returns an equation whose root is the left child of this equation's root
     */
    @Override
    public Equation getLeft() {
        return new Equation(root.getLeft(), this.variables);
    }

    /**
     * Returns the right subtree of the equation
     * @return Returns an equation whose root is the right child of this equation's root
     */
    @Override
    public Equation getRight() {
        return new Equation(root.getRight(), this.variables);
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

        if (tokens.size() > 1 && tokens.get(0).type == VARIABLE
                && tokens.get(1).isOperator() && tokens.get(1).getOperator() == '=') {
            variable = tokens.get(0).getVariable();
            tokens.remove(0);
            tokens.remove(0);
        }

        //Implementation of shunting yard algorithm
        for (Token token : tokens) {
            type = token.type;

            if (type == NUMBER || type == UNIT) {
                output.push(new Equation(token, null, null, this.variables));
            }
            else if (type == VARIABLE) {
                output.push(new Equation(token, null, null, this.variables));
                variableUsage.add(token.getVariable());
            }
            else if (type == OPERATOR) {
                if (token.getOperator() == '=') {
                    throw new RuntimeException("Found \"=\" operating in unexpected place.");
                }

                while (!operators.isEmpty() && operators.peek().type != LBRACKET
                        && precedence(operators.peek().getOperator(), token.getOperator()) >= 0) {
                    right = output.pop();
                    output.push(new Equation(operators.pop(), output.pop(), right, this.variables));
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
                    output.push(new Equation(operators.pop(), output.pop(), right, this.variables));
                }
                operators.pop();

                if (!operators.isEmpty() && operators.peek().type == FUNCTION) {
                    output.push(new Equation(operators.pop(), output.pop(), null, this.variables));
                }
            }
        }

        while (!operators.isEmpty()) {
            right = output.pop();
            output.push(new Equation(operators.pop(), output.pop(), right, this.variables));
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

        for (int i = 0; i < Parsing.OPERATORS.length; i++) {
            if (Parsing.OPERATORS[i].indexOf(op1) != -1)
                a = i;
            if (Parsing.OPERATORS[i].indexOf(op2) != -1)
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
        else if (temp.isVariable()) {
            ret = variables.apply(temp.getVariable());

            if (ret == null) {
                throw new RuntimeException("Variable \"" + temp.getVariable() + "\" is undefined.");
            }
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
    private Quantity computeTerm(String function, Quantity x) {
        return switch (function) {
            case "sqrt" -> Quantity.sqrt(x);
            case "ln" -> Quantity.ln(x);
            case "log" -> Quantity.log(x);
            case "exp" -> Quantity.exp(x);
            case "sin" -> Quantity.sin(x);
            case "cos" -> Quantity.cos(x);
            case "tan" -> Quantity.tan(x);
            case "asin" -> Quantity.asin(x);
            case "acos" -> Quantity.acos(x);
            case "atan" -> Quantity.atan(x);
            case "sinh" -> Quantity.sinh(x);
            case "cosh" -> Quantity.cosh(x);
            case "tanh" -> Quantity.tanh(x);
            case "asinh" -> Quantity.asinh(x);
            case "acosh" -> Quantity.acosh(x);
            case "atanh" -> Quantity.atanh(x);
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
            case '*', Parsing.IMPLICIT_M -> left.multiply(right);
            case '/', Parsing.IMPLICIT_D -> left.divide(right);
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
     * Checks whether this equation defines a variable
     * @return Returns true if it defines a variable, false otherwise
     */
    public boolean isAssignment() {
        return variable != null;
    }

    /**
     * Gets the variable defined by this Equation, if it exists
     * @return Returns the string representation of the variable
     * @throws RuntimeException If this Equation does not define a variable
     */
    public String getVariable() {
        if (!isAssignment()) throw new RuntimeException("This Equation does not declare a variable");

        return variable;
    }

    /**
     * Returns a latex string representation of the equation
     * @return Returns a latex string representation of the equation
     */
    public String toLatexString(int sigFigs) {
        String prepend = isAssignment() ? variable + "=" : "";

        if (isAtom()) {
            Token token = root.getElement();

            if (token.isVariable())
                return prepend + token.getVariable();
            else
                return prepend + root.getElement().getValue().toLatexString(sigFigs);
        }

        String left = getLeft().toLatexString(sigFigs);
        // Unary function
        if (getRight().isEmpty()) {
            return prepend + "\\textrm{" + root.getElement() + "}\\left(" + left + "\\right)";
        }

        String right = getRight().toLatexString(sigFigs);
        TokenType rightType = getRight().getRootElement().type;
        // Binary operator
        return prepend + switch (root.getElement().getOperator()) {
            case Parsing.IMPLICIT_M -> left
                    + (rightType == UNIT ? "\\: " : "")
                    + (rightType == NUMBER || rightType == OPERATOR ? "\\left(" + right + "\\right)" : right);
            case Parsing.IMPLICIT_D -> left + "/" + right;
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
        String prepend = isAssignment() ? variable + "=" : "";
        if (isAtom()) return prepend + root.getElement().toString();

        String left = getLeft().toString();
        if (getRight().isEmpty()) return prepend + root.getElement() + "(" + left + ")";
        if (!getLeft().isAtom()) left = "(" + left + ")";
        String right = getRight().toString();
        if (!getRight().isAtom()) right = "(" + right + ")";

        return prepend + left + root.getElement() + right;
    }
}