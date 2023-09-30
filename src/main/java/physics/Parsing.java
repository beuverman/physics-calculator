package physics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static physics.TokenType.*;

public class Parsing {
    public static Token[] tokenizer(String equation) {
        Pattern pattern = Pattern.compile("([0-9.]+)|([()^+/*-])|([a]?(?:sin|cos|tan|sec|csc|cot)[h]?|con\\([a-zA-Z0-9]+\\))|" +
                "([QRYZEPTGMkhadcmµnpfzyrq]?(?:s|mol|g|A|K|min|cd|Hz|N|Pa|J|Wb|C|V|F|Ω|S|W|T|H|lm|lx|Bq|Gy|Sv|m|h|d|au|ha|l|t|Da|amu|eV|pc|atm|cal))");
        Matcher matcher = pattern.matcher(equation);
        ArrayList<Token> tokens = new ArrayList<>();
        Token[] out;
        TokenType type = null, prevType;
        String current;
        int index = 0;

        while (matcher.find()) {
            current = matcher.group();
            if (current.contains("con(")) {
                Quantity constant = Units.getConstant(current.substring(4, current.length() - 1));
                if (constant == null)
                    throw new RuntimeException("Unrecognized constant");

                tokens.add(new Token(constant));
                index++;
                type = NUMBER;
                continue;
            }

            tokens.add(new Token(current));
            prevType = type;
            type = tokens.get(index).type;

            //Implicit multiplication or division by units has higher precedence
            if (index > 0 && type == UNIT && prevType == NUMBER) {
                tokens.add(index, new Token(Token.IMPLICIT_M));
                index++;
            }
            else if (index > 1 && type == UNIT && prevType == OPERATOR && tokens.get(index - 1).getOperator() == '/' && tokens.get(index - 2).type == UNIT) {
                tokens.set(index - 1, new Token(Token.IMPLICIT_D));
            }
            //negation instead of subtraction
            else if ((index == 1 && type.equals(NUMBER) && prevType == OPERATOR && tokens.get(0).getOperator() == '-') ||
                    (index > 1 && type.equals(NUMBER) && prevType == OPERATOR && tokens.get(index - 1).getOperator() == '-' &&
                    (tokens.get(index - 2).type == OPERATOR || tokens.get(index - 2).type == LBRACKET))) {
                tokens.set(index, new Token(tokens.get(index).getValue().negate()));
                tokens.remove(index - 1);
                index--;
            }

            index++;
        }

        out = new Token[tokens.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = tokens.get(i);
        }

        return out;
    }
}