package physics;

import collections.BinaryTreeNode;

public class Equation extends collections.LinkedBinaryTree<EquationTreeOp>{
    String equation;

    public Equation() {
        super();
    }

    public Equation(EquationTreeOp element, Equation leftSubtree, Equation rightSubtree) {
        root = new BinaryTreeNode<>(element, leftSubtree, rightSubtree);
    }

    public Equation(String equation) {
        this.equation = equation;

        parseEquation();
    }

    public Quantity evaluate() {
        return evaluateNode(root);
    }

    private void parseEquation() {
        int space = equation.indexOf(' ');


        //base case
        if (space == -1)
            root = new BinaryTreeNode<>(new EquationTreeOp(new Quantity(equation)));
        //recursive
        else
            root = new BinaryTreeNode<>(
                new EquationTreeOp(equation.charAt(space + 1)),
                new Equation(equation.substring(0, space)),
                new Equation(equation.substring(space + 3)));
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
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }
}
