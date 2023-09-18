package physics;

import collections.BinaryTreeNode;
import collections.CircularArrayQueue;

import java.util.AbstractQueue;
import java.util.Stack;

public class Equation extends collections.LinkedBinaryTree<EquationTreeOp> {
    private static final int NUMBER = 0;
    private static final int OPERATOR = 1;
    private static final int FUNCTION = 2;
    private static final int LBRACKET = 3;
    private static final int RBRACKET = 4;

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
        int type;
        CircularArrayQueue<String> output = new CircularArrayQueue<>();
        Stack<String> operators = new Stack<>();


        for (String token : tokens) {
            type = identifyToken(token);

            if (type == NUMBER) {
                output.enqueue(token);
            }
            else if (type == OPERATOR) {
                while (!operators.isEmpty() && identifyToken(operators.peek()) != LBRACKET && precedence(operators.peek(), token) >= 0)
                    output.enqueue(operators.pop());
                operators.push(token);
            }
            else if (type == FUNCTION) {

            }
            else if (type == LBRACKET) {
                operators.push(token);
            }
            else if (type == RBRACKET) {
                while (!operators.isEmpty() && identifyToken(operators.peek()) != LBRACKET) {
                    output.enqueue(operators.pop());
                }
                operators.pop();

                if (!operators.isEmpty() && identifyToken(operators.peek()) == FUNCTION)
                    output.enqueue(operators.pop());
            }
        }

        while (!operators.isEmpty()) {
            output.enqueue(operators.pop());
        }

        buildTree(output);
    }

    //converts postfix notation to an expression tree
    private void buildTree(CircularArrayQueue<String> tokens) {
        Stack<Equation> stack = new Stack<>();
        Equation left, right;
        String token;
        int type;

        while (tokens.size() > 1) {
            token = tokens.dequeue();
            type = identifyToken(token);

            if (type == NUMBER) {
                stack.push(new Equation(new EquationTreeOp(new Quantity(token)), null, null));
            }
            else  {
                right = stack.pop();
                stack.push(new Equation(new EquationTreeOp(token), stack.pop(), right));
            }
        }

        right = stack.pop();
        root = new BinaryTreeNode<>(new EquationTreeOp(tokens.dequeue()), stack.pop(), right);
    }

    private static int identifyToken(String token) {
        if (token.length() == 1) {
            if ("+-*/^".contains(token))
                return OPERATOR;
            if ("(".contains(token))
                return LBRACKET;
            if (")".contains(token))
                return RBRACKET;
        }

        return NUMBER;
    }

    private static int precedence(String op1, String op2) {
        int a = 0, b = 0;
        String[] operators = {"+-", "*/", "^"};

        for (int i = 0; i < 2; i++) {
            if (operators[i].contains(op1))
                a = i;
            if (operators[i].contains(op2))
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
