package ui;

import javafx.scene.layout.VBox;
import physics.Equation;
import physics.Quantity;

import java.util.*;
import java.util.function.Function;

/**
 * Handles interactions with the equation boxes
 */
public class EquationSet extends VBox {
    private int sigFigs;
    private final HashSet<EquationGroup> validEquations;
    private final HashMap<String, Quantity> variables;
    private final HashMap<String, HashSet<EquationGroup>> dependencyGraph;

    public EquationSet(int sigFigs) {
        super();
        this.sigFigs = sigFigs;
        validEquations = new HashSet<>();
        variables = new HashMap<>();
        dependencyGraph = new HashMap<>();

        getChildren().add(new EquationGroup(this));
    }

    public int getSigFigs() {
        return sigFigs;
    }

    public void setSigFigs(int sigFigs) {
        this.sigFigs = sigFigs;
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        EquationGroup eg;

        for (int i = 0; i < children.size(); i++) {
            eg = (EquationGroup) children.get(i);
            eg.evaluate();
        }
    }

    /**
     * Checks whether there are too many or too few equation groups
     */
    private void manageEquationCount() {
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        int index = children.size() - 1;
        EquationGroup lastGroup = (EquationGroup) children.get(index);

        // Add additional group if necessary
        if (!lastGroup.isEmpty()) {
            children.add(new EquationGroup(this));
            return;
        }
        if (index == 0)
            return;

        // Remove extra groups if necessary
        EquationGroup secondLastGroup = (EquationGroup) children.get(--index);
        while (index > 0 && secondLastGroup.isEmpty()) {
            children.remove(lastGroup);
            lastGroup = secondLastGroup;
            secondLastGroup = (EquationGroup) children.get(--index);
        }
        if (index == 0 && secondLastGroup.isEmpty()) {
            children.remove(lastGroup);
        }
    }

    /**
     * Clears all Equation Groups and removes all but one
     */
    public void clear() {
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        children.clear();
        children.add(new EquationGroup(this));

        variables.clear();
        validEquations.clear();
        dependencyGraph.clear();
    }

    public Function<String, Quantity> getVariables() {
        return variables::get;
    }

    public Set<String> getVariableStrings() {
        return variables.keySet();
    }

    private void addValidEquation(EquationGroup eqGroup) {
        validEquations.add(eqGroup);

        Equation eq = eqGroup.getEquation();
        if (eq.isAssignment()) {
            String var = eq.getVariable();

            if (variables.containsKey(var))
                throw new RuntimeException("Conflicting definitions for variable \"" + var + "\"");
            else
                variables.put(var, null);
        }

        for (String dependency : eq.variableUsage) {
            dependencyGraph.putIfAbsent(dependency, new HashSet<>());
            dependencyGraph.get(dependency).add(eqGroup);
        }
    }

    private void removeValidEquation(EquationGroup eqGroup) {
        if (!validEquations.contains(eqGroup))
            return;

        validEquations.remove(eqGroup);

        Equation eq = eqGroup.getEquation();
        if (eq.isAssignment())
            variables.remove(eq.getVariable());

        for (String dependency : eq.variableUsage) {
            dependencyGraph.get(dependency).remove(eqGroup);
        }
    }

    /**
     * Sets the text fields of the Equation Groups to the given equations
     * @param equations The equations to be used
     */
    public void setEquations(List<String> equations) {
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        EquationGroup eg;

        clear();

        for (int i = 0; i < equations.size(); i++) {
            eg = (EquationGroup) children.get(i);
            eg.setEquation(equations.get(i));
            EquationGroupModified(eg);
        }
    }

    /**
     * Gets the list of equations currently in the Equation Groups
     * @return Returns the current list of equations as they appear in the input fields
     */
    public List<String> getEquationStrings() {
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();
        EquationGroup eg;
        ArrayList<String> equations = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            eg = (EquationGroup) children.get(i);
            equations.add(eg.getEquationString());
        }

        return equations;
    }

    /**
     * Handler for when any managed EquationGroup is modified
     * @param eg The EquationGroup that was modified
     */
    // Can I add the listener here instead of on each EquationGroup?
    public void EquationGroupModified(EquationGroup eg) {
        manageEquationCount();

        removeValidEquation(eg);
        invalidateEquations();

        if (!eg.parseEquation()) {
            return;
        }

        addValidEquation(eg);
        if (eg.isAssignment()) {
            validateEquations();
            evaluateValidEquations();
        }
        else
            eg.evaluate();
    }

    /**
     * Reevaluates all Equations in the event of a variable update.
     */
    private void evaluateValidEquations() {
        List<EquationGroup> equationGroups = topologicalOrdering(new ArrayList<>(validEquations));

        for (EquationGroup eg : equationGroups) {
            Quantity result = eg.evaluate();
            Equation eq = eg.getEquation();

            if (eq.isAssignment()) {
                variables.put(eq.getVariable(), result);
            }
        }
    }

    /**
     * Finds a topological ordering on a given list of Equations.
     * That is, an ordering of evaluation of Equations such that each variable assignment has been evaluated before use.
     * Implements a depth-first search.
     * @param equationGroups List of EquationGroups that hold the Equations to be ordered
     * @return Returns a reordering of equationGroups that dictates how their Equations should be ordered
     */
    private List<EquationGroup> topologicalOrdering(List<EquationGroup> equationGroups) {
        int num = equationGroups.size();
        List<EquationGroup> output = new ArrayList<>();
        boolean[] permanentMark = new boolean[num];
        boolean[] temporaryMark = new boolean[num];

        for (int i = 0; i < num; i++) {
            if (!permanentMark[i])
                visit(i, output, equationGroups, permanentMark, temporaryMark);
        }

        Collections.reverse(output);
        return output;
    }

    // Recursive function for finding a topological ordering
    private void visit(int i, List<EquationGroup> output, List<EquationGroup> equationGroups,
                       boolean[] permanentMark, boolean[] temporaryMark) {
        if (permanentMark[i])
            return;
        else if (temporaryMark[i])
            throw new RuntimeException("Circular dependency exists in variable assignment.");

        temporaryMark[i] = true;

        EquationGroup eg = equationGroups.get(i);
        Equation eq = eg.getEquation();
        if (eq.isAssignment() && dependencyGraph.containsKey(eq.getVariable())) {
            for (EquationGroup e : dependencyGraph.get(eq.getVariable())) {
                visit(equationGroups.indexOf(e), output, equationGroups, permanentMark, temporaryMark);
            }
        }

        permanentMark[i] = true;
        output.add(eg);
    }

    /**
     * Looks through all currently invalid Equations and checks if any of them successfully parse.
     * If they do, they are marked valid Equations and we check if their inclusion validated any others.
     */
    private void validateEquations() {
        javafx.collections.ObservableList<javafx.scene.Node> children = getChildren();

        for (int i = 0; i < children.size(); i++) {
            EquationGroup eg = (EquationGroup) children.get(i);

            if (!validEquations.contains(eg) && eg.parseEquation()) {
                addValidEquation(eg);

                if (eg.isAssignment())
                    i = 0;
            }
        }
    }

    /**
     * Looks through all currently valid Equations and checks if any of them unsuccessfully parse.
     * If they do, they are marked invalid Equations and we check if their exclusion invalidated any others.
     */
    // Would be useful to add a parameter to mark which variable disappearing causes us to invalidate
    private void invalidateEquations() {
        Iterator<EquationGroup> iter = validEquations.iterator();

        while (iter.hasNext()) {
            EquationGroup eg = iter.next();
            Equation equation = eg.getEquation();
            boolean isAssignment = eg.isAssignment();

            if (!eg.parseEquation()) {
                iter.remove();

                if (isAssignment) {
                    variables.remove(equation.getVariable());
                    iter = validEquations.iterator();
                }
            }
        }
    }
}