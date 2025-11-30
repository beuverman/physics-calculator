package physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static physics.TokenType.*;

/**
 * Handles the parsing of an equation string into a list of tokens
 */
public class Parsing {
    public static final char IMPLICIT_M = 9994;
    public static final char IMPLICIT_D = 9995;
    public static final String[] OPERATORS = {"=", "+-", "*/", new String(new char[]{IMPLICIT_M, IMPLICIT_D}), "^"};

    /**
     * Turns a string representing an equation into a series of tokens
     * @param equation The equation to be converted
     * @return Returns a list of tokens that represent the equation
     */
    public static List<Token> tokenizer(String equation, Set<String> variables) {
        ArrayList<Token> tokens = new ArrayList<>();
        Token token, prevToken;
        TokenType type, prevType, prevPrevType;
        String tokenString;
        int matchGroup;
        int expectedStart = 0;

        equation = identifyAssignment(equation, tokens);
        Matcher matcher = getMatcher(equation, variables);

        // Read out tokens from equation
        while (matcher.find()) {
            tokenString = matcher.group();
            matchGroup = matchedGroup(matcher);
            type = identifyType(matchGroup, tokenString);

            // Check for invalid token
            if (expectedStart != matcher.start()) {
                String unidentified = equation.substring(expectedStart, matcher.start());
                throw new RuntimeException("Unidentified token: \"" + unidentified + "\"");
            }

            // Replace with value as necessary
            if (matchGroup == 4) {
                // Regex won't catch expressions with nested brackets properly
                // So we manually find our input string to the function
                int index = findBracketPair(equation, matcher.start() + tokenString.indexOf('('));

                tokenString = equation.substring(matcher.start(), index + 1);
                matcher.region(index + 1, equation.length());

                tokens.add(new Token(replaceFunction(tokenString)));
            }
            else
                tokens.add(new Token(tokenString, type));

            expectedStart += tokenString.length();
        }

        // Ended with unidentified token
        if (expectedStart != equation.length()) {
            tokenString = equation.substring(expectedStart);
            throw new RuntimeException("Unidentified token: \"" + tokenString + "\"");
        }

        // Special rules at start of equation
        // Leading negation
        if (tokens.size() >= 2) {
            token = tokens.get(1);
            prevToken = tokens.get(0);

            if (prevToken.isOperator() && prevToken.getOperator() == '-' && token.type == NUMBER) {
                tokens.set(1, new Token(token.getValue().negate()));
                tokens.remove(0);
            }
        }
        // Leading implicit multiplication
        if (tokens.size() >= 2) {
            token = tokens.get(1);
            type = token.type;
            prevType = tokens.get(0).type;

            if ((prevType == NUMBER || prevType == UNIT || prevType == RBRACKET || prevType == VARIABLE)
                    && (type == NUMBER || type == UNIT || type == LBRACKET || type == FUNCTION || type == VARIABLE)) {
                tokens.add(1, new Token(IMPLICIT_M));
            }
        }

        // Apply additional rules
        for (int i = 2; i < tokens.size(); i++) {
            token = tokens.get(i);
            type = token.type;
            prevToken = tokens.get(i - 1);
            prevType = prevToken.type;
            prevPrevType = tokens.get(i - 2).type;

            //Implicit multiplication
            if ((prevType == NUMBER || prevType == UNIT || prevType == RBRACKET || prevType == VARIABLE)
                    && (type == NUMBER || type == UNIT || type == LBRACKET || type == FUNCTION || type == VARIABLE)) {
                tokens.add(i, new Token(IMPLICIT_M));
                i++;
            }
            //Implicit division
            else if (token.type == UNIT && prevToken.isOperator() && prevToken.getOperator() == '/' && prevPrevType == UNIT) {
                tokens.set(i - 1, new Token(IMPLICIT_D));
            }
            // Negation instead of subtraction
            else if (type == NUMBER && prevToken.isOperator() && prevToken.getOperator() == '-' &&
                    (prevPrevType == OPERATOR || prevPrevType == LBRACKET)) {
                tokens.set(i, new Token(token.getValue().negate()));
                tokens.remove(i - 1);
                i--;
            }
        }

        return tokens;
    }

    /**
     * Builds the Matcher that will tokenize an equation.
     * @param equation The equation to build the matcher on.
     * @param variables List of allowed variable strings in the equation
     * @return Returns a Matcher that will tokenize the equation.
     */
    private static Matcher getMatcher(String equation, Set<String> variables) {
        String group1 = "(\\d+\\.?\\d*(?:E[-+]?\\d+)?)|"; // Numbers
        String group2 = "([()^+/*-])|"; // Operators
        String group3 = "(sqrt|ln|log|exp|a?(?:sin|cos|tan|sec|csc|cot)h?)|"; // Functions
        String group4 = "((?:con|M|BE|HL|MMass)\\([^)]+\\))|"; // Replacement functions
        String group5 = "((?:[QRYZEPTGMkhadcmunpfzyrq]|da)?(?:s|mol|g|A|K|min|cd|Hz|N|Pa|J|Wb|C|V|F|O|S|W|T|H|lm|lx|Bq|Gy|Sv|m|h|d|au|ha|l|Da|amu|eV|pc|bar|atm|cal))"; // Units
        String group6 = variables.isEmpty() ? "" : "|(" + String.join("|", variables) + ")";
        Pattern pattern = Pattern.compile(group1 + group2 + group3 + group4 + group5 + group6);
        return pattern.matcher(equation);
    }

    /**
     * Checks whether there is an assignment operation in the equation, and extracts it
     * @param equation The equation to be considered.
     * @param tokens The list into which the equation will be tokenized.
     * @return Returns the equation with the assignment removed, if it existed. Adds tokens to the token list that
     * capture the assignment.
     */
    private static String identifyAssignment(String equation, List<Token> tokens) {
        int eqIndex = equation.indexOf('=');

        if (eqIndex == -1)
            return equation;

        String variable = equation.substring(0, eqIndex);
        Pattern pattern = Pattern.compile("[a-zA-Z]"); // Can allow for more complex variable naming
        Matcher matcher = pattern.matcher(variable);

        if (!matcher.matches()) {
            throw new RuntimeException("Invalid variable string: \"" + variable + "\"");
        }

        tokens.add(new Token(variable, VARIABLE));
        tokens.add(new Token("=", OPERATOR));
        return equation.substring(eqIndex + 1);
    }

    /**
     * Given the index of a left bracket within a string, finds the index of its associated right bracket.
     * @param str String to be searched.
     * @param index Index at which left bracket is found.
     * @return Returns the index of the associated right bracket. Returns -1 if no such bracket exists in the string.
     */
    private static int findBracketPair(String str, int index) {
        int count = 1;
        int len = str.length();
        index++;

        while (index < len && count > 0) {
            char c =  str.charAt(index);
            switch (c) {
                case '(' -> count++;
                case ')' -> count--;
            }
            index++;
        }

        if (index > len)
            return -1;
        return index - 1;
    }

    /**
     * Identifies the group that matched the most recently matched token.
     * Assumes that only one group matched.
     * @param matcher The Matcher to check
     * @return The first group id that matched on the previous token
     */
    private static int matchedGroup(Matcher matcher) {
        int groupCount = matcher.groupCount();

        for (int i = 1; i <= groupCount; i++) {
            if (matcher.start(i) != -1)
                return i;
        }

        return -1;
    }

    /**
     * Computes the quantity corresponding to a given lookup function
     * @param str The function and argument, given as func(arg)
     * @return Returns the corresponding quantity
     */
    private static Quantity replaceFunction(String str) {
        if (str.contains("con(")) {
            Quantity constant = Units.getConstant(str.substring(4, str.length() - 1));
            if (constant == null)
                throw new RuntimeException("Unrecognized constant " + str.substring(4, str.length() - 1));

            return constant;
        }
        else if (str.contains("M(")) {
            return Nuclides.getMass(str.substring(2, str.length() - 1));
        }
        else if (str.contains("BE(")) {
            return Nuclides.getBindingEnergy(str.substring(3, str.length() - 1));
        }
        else if (str.contains("HL(")) {
            return Nuclides.getHalfLife(str.substring(3, str.length() - 1));
        }
        else if (str.contains("MMass(")) {
            ChemicalFormula formula = new ChemicalFormula(parseChemicalFormula(str.substring(6, str.length() - 1)));
            return formula.getMolarMass();
        }

        return null;
    }

    /**
     * Identifies what type of token the given string is
     * @param group The regex group that matched it
     * @param str String to be identified
     * @return Returns the TokenType of the argument
     */
    private static TokenType identifyType(int group, String str) {
        return switch (group) {
            case 1,4 -> NUMBER;
            case 2 -> switch (str.charAt(0)) {
                case '(' -> LBRACKET;
                case ')' -> RBRACKET;
                default -> OPERATOR;
            };
            case 3 -> FUNCTION;
            case 5 -> UNIT;
            case 6 -> VARIABLE;
            default -> null;
        };
    }

    /**
     * Parses a string that represents a chemical formula into a series of tokens. Assumes that the formula is valid
     * and contains only numbers, elements, and brackets.
     * @param formula String that represents the formula
     * @return Returns a list of ChemicalTokens, the formula having been broken into numbers, elements, and brackets.
     */
    public static List<ChemicalToken> parseChemicalFormula(String formula) {
        String elements = String.join("|", Chemistry.getElementStrings());
        Pattern pattern = Pattern.compile("(\\d+)|(\\()|(\\))|(" + elements + ")");
        Matcher matcher = pattern.matcher(formula);
        String token;
        TokenType type;
        int matchedGroup;
        List<ChemicalToken> output = new ArrayList<>();

        while (matcher.find()) {
            token = matcher.group();
            matchedGroup = matchedGroup(matcher);

            type = switch(matchedGroup) {
                case 1 -> NUMBER;
                case 2 -> LBRACKET;
                case 3 -> RBRACKET;
                case 4 -> CHEMICAL;
                default -> throw new IllegalStateException("Unexpected value: " + matchedGroup);
            };

            output.add(new ChemicalToken(token, type));
        }

        return output;
    }
}