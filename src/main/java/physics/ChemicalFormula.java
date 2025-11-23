package physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * A class representing a chemical formula
 */
public class ChemicalFormula {
    private final FormulaNode root;

    /**
     * Create a Chemical Formula given a list of chemical tokens that represent it.
     * Tokens are assumed to form a valid formula.
     */
    public ChemicalFormula(List<ChemicalToken> tokens) {
        Stack<FormulaNode> bracketMarkers = new Stack<>();
        Stack<FormulaNode> output = new Stack<>();
        int count = 1;

        Collections.reverse(tokens);

        for (ChemicalToken token : tokens) {
            switch (token.type()) {
                case NUMBER -> count = Integer.parseInt(token.string());
                case CHEMICAL -> {
                    output.add(new ElementNode(token.string(), count));
                    count = 1;
                }
                case RBRACKET -> {
                    output.add(new GroupNode(null, count));
                    bracketMarkers.add(output.peek());
                    count = 1;
                }
                case LBRACKET -> {
                    List<FormulaNode> group = new ArrayList<>();
                    while (output.peek() != bracketMarkers.peek()) {
                        group.add(output.pop());
                    }
                    GroupNode p = (GroupNode) bracketMarkers.pop();
                    p.components = group;
                }
            }
        }

        if (output.size() == 1) {
            root = output.pop();
        }
        else {
            Collections.reverse(output);
            root = new GroupNode(output, count);
        }
    }

    /**
     * Gets the molar mass of this Chemical Formula.
     */
    public Quantity getMolarMass() {
        return root.getMolarMass();
    }

    public String toString() {
        return root.toString();
    }

    private abstract class FormulaNode {
        protected int count;

        public FormulaNode(int count) {
            this.count = count;
        }

        public abstract Quantity getMolarMass();
    }

    private class ElementNode extends FormulaNode {
        private String element;

        public ElementNode(String element, int count) {
            super(count);
            this.element = element;
        }

        public Quantity getMolarMass() {
            Quantity molarMass = new Quantity(String.valueOf(Chemistry.getMolarMass(element)),
                                              new Dimension(0, 0, 1, 0, 0, -1, 0));
            return molarMass.multiply(new Quantity(count)).divide(new Quantity(1000));
        }

        @Override
        public String toString() {
            if (count == 1)
                return element;
            return element + count;
        }


    }

    private class GroupNode extends FormulaNode {
        private List<FormulaNode> components;

        public GroupNode(List<FormulaNode> components, int count) {
            super(count);
            this.components = components;
        }

        public Quantity getMolarMass() {
            Quantity result = new Quantity(0, new Dimension(0, 0, 1, 0, 0, -1, 0));

            for (FormulaNode component : components) {
                result = result.add(component.getMolarMass());
            }

            return result.multiply(new Quantity(count));
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();

            if (count != 1)
                buffer.append('(');

            for (FormulaNode node: components)
                buffer.append(node.toString());

            if (count != 1) {
                buffer.append(')');
                buffer.append(count);
            }

            return buffer.toString();
        }
    }
}