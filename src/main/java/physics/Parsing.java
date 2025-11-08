package physics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static physics.TokenType.*;

/**
 * Handles the parsing of an equation string into a list of tokens
 */
public class Parsing {
    public static final char IMPLICIT_M = 9994;
    public static final char IMPLICIT_D = 9995;
    public static final String[] OPERATORS = {"+-", "*/", new String(new char[]{IMPLICIT_M, IMPLICIT_D}), "^"};

    /**
     * Turns a string representing an equation into a series of tokens
     * @param equation The equation to be converted
     * @return Returns a list of tokens that represent the equation
     */
    public static List<Token> tokenizer(String equation) {
        String group1 = "(\\d+\\.?\\d*(?:E[-+]?\\d+)?)|"; // Numbers
        String group2 = "([()^+/*-])|"; // Operators
        String group3 = "(sqrt|ln|log|exp|a?(?:sin|cos|tan|sec|csc|cot)h?)|"; // Functions
        String group4 = "((?:con|M|BE|HL)\\([^)]+\\))|"; //Replacement functions
        String group5 = "((?:[QRYZEPTGMkhadcmµnpfzyrq]|da)?(?:s|mol|g|A|K|min|cd|Hz|N|Pa|J|Wb|C|V|F|O|S|W|T|H|lm|lx|Bq|Gy|Sv|m|h|d|au|ha|l|t|Da|amu|eV|pc|atm|cal))"; //Units
        Pattern pattern = Pattern.compile(group1 + group2 + group3 + group4 + group5);
        Matcher matcher = pattern.matcher(equation);
        ArrayList<Token> tokens = new ArrayList<>();
        Token token = null, prevToken;
        TokenType type = null, prevType = null, prevPrevType;
        String tokenString;
        int matchGroup;
        int expectedStart = 0;

        while (matcher.find()) {
            tokenString = matcher.group();

            // Missed characters that compose unidentified token
            if (expectedStart != matcher.start()) {
                tokenString = equation.substring(expectedStart, matcher.start());
                throw new RuntimeException("Unidentified token: \"" + tokenString + "\"");
            }

            expectedStart += tokenString.length();
            prevToken = token;
            matchGroup = matchedGroup(matcher);
            prevPrevType = prevType;
            prevType = type;
            type = identifyType(matchGroup, tokenString);
            if (matchGroup == 4)
                token = new Token(replaceFunction(tokenString));
            else
                token = new Token(tokenString, type);

            //Implicit multiplication
            if (!tokens.isEmpty() && (prevType == NUMBER || prevType == UNIT || prevType == RBRACKET)
                && (type == NUMBER || type == UNIT || type == LBRACKET || type == FUNCTION)) {
                tokens.add(new Token(IMPLICIT_M));
            }
            //Implicit division
            else if (tokens.size() > 1 && token.type == UNIT && prevToken.isOperator() && prevToken.getOperator() == '/' && prevPrevType == UNIT) {
                tokens.set(tokens.size() - 1, new Token(IMPLICIT_D));
            }
            //negation instead of subtraction
            else if ((!tokens.isEmpty() && type == NUMBER && prevToken.isOperator() && prevToken.getOperator() == '-') &&
                    (tokens.size() == 1 || prevPrevType == OPERATOR || prevPrevType == LBRACKET)) {
                token = new Token(token.getValue().negate());
                tokens.remove(tokens.size() - 1);
            }

            tokens.add(token);
        }

        // Ended with unidentified token
        if (expectedStart != equation.length()) {
            tokenString = equation.substring(expectedStart);
            throw new RuntimeException("Unidentified token: \"" + tokenString + "\"");
        }

        return tokens;
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
            default -> null;
        };
    }
}