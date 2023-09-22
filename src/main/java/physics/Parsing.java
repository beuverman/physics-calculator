package physics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static physics.TokenType.*;

public class Parsing {
    public static Token[] tokenizer(String equation) {
        Pattern pattern = Pattern.compile("([0-9.]+|[()^+/*-])|(sin|cos|tan)|[QRYZEPTGMkhadcmµnpfzyrq]?(?:s|m|g|A|K|mol|cd|Hz|N|Pa|J|W|C|V|F|O|S|Wb|T|H|lm|lx|Bq|Gy|Sv|min|h|d|au|ha|l|t|Da|amu|eV|pc|atm|cal)");
        Matcher matcher = pattern.matcher(equation);
        ArrayList<Token> tokens = new ArrayList<>();
        Token[] out;
        int index = 0;

        while (matcher.find()) {
            tokens.add(new Token(matcher.group()));

            if (index > 0 && tokens.get(index).getType() == UNIT && tokens.get(index - 1).getType() == NUMBER) {
                tokens.add(new Token("*"));
                index++;
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