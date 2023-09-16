package physics;

public class Equation {
    private Quantity value;
    private String equation;

    public Equation(String equation) {
        this.equation = equation;
    }

    public Quantity compute() {
        String[] split = equation.split(" ");
        Quantity quantity;
        char op = 0;

        for (int i = 0; i < split.length; i++) {
            //quantities
            if (i % 2 == 0) {
                quantity = new Quantity(split[i]);

                //evaluate
                if (value == null) {
                    value = quantity;
                }
                else if (op != 0) {
                    switch (op) {
                        case '+' -> value = value.add(quantity);
                        case '-' -> value = value.subtract(quantity);
                        case '*' -> value = value.multiply(quantity);
                        case '/' -> value = value.divide(quantity);
                    }

                    op = 0;
                }
            }
            //operations
            else {
                op = split[i].charAt(0);
            }
        }

        return value;
    }

    public static String evaluate(String str) {
        return null;
    }
}
