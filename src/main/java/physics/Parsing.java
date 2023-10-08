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
    /**
     * Turns a string representing an equation into a series of tokens
     * @param equation The equation to be converted
     * @return Returns a list of tokens that represent the equation
     */
    public static List<Token> tokenizer(String equation) {
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*(?:E[-+]?\\d+)?)|([()^+/*-])|([a]?(?:sin|cos|tan|sec|csc|cot)[h]?|(?:con|M|BE)\\([^)]+\\))|" +
            "((?:[QRYZEPTGMkhadcmµnpfzyrq]|da)?(?:s|mol|g|A|K|min|cd|Hz|N|Pa|J|Wb|C|V|F|O|S|W|T|H|lm|lx|Bq|Gy|Sv|m|h|d|au|ha|l|t|Da|amu|eV|pc|atm|cal))");
        Matcher matcher = pattern.matcher(equation);
        ArrayList<Token> tokens = new ArrayList<>();
        String current;
        Token token = null, prev = null, prevPrev;

        while (matcher.find()) {
            current = matcher.group();
            prevPrev = prev;
            prev = token;

            if (current.contains("con(")) {
                Quantity constant = Units.getConstant(current.substring(4, current.length() - 1));
                if (constant == null)
                    throw new RuntimeException("Unrecognized constant " + current.substring(4, current.length() - 1));

                token = new Token(constant);
            }
            else if (current.contains("M(")) {
                Quantity mass = Nuclides.getMass(current.substring(2, current.length() - 1));

                token = new Token(mass);
            }
            else if (current.contains("BE(")) {
                Quantity BE = Nuclides.getBindingEnergy(current.substring(3, current.length() - 1));

                token = new Token(BE);
            }
            else
                token = new Token(current);

            //Implicit multiplication
            if (tokens.size() > 0 && (((token.type == UNIT || token.type == LBRACKET) && (prev.type == NUMBER || prev.type == UNIT)) ||
                    (prev.type == RBRACKET && (token.type == NUMBER || token.type == UNIT)))) {
                tokens.add(new Token(Token.IMPLICIT_M));
            }
            //Implicit division
            else if (tokens.size() > 1 && token.type == UNIT && prev.isOperator() && prev.getOperator() == '/' && prevPrev.type == UNIT) {
                tokens.set(tokens.size() - 1, new Token(Token.IMPLICIT_D));
            }
            //negation instead of subtraction
            else if ((tokens.size() > 0 && token.type.equals(NUMBER) && prev.isOperator() && prev.getOperator() == '-') &&
                    (tokens.size() == 1 || prevPrev.isOperator() || prevPrev.type == LBRACKET)) {
                token = new Token(token.getValue().negate());
                tokens.remove(tokens.size() - 1);
            }

            tokens.add(token);
        }

        return tokens;
    }
}