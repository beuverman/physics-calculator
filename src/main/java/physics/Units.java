package physics;

import java.math.BigDecimal;

public class Units extends Quantity {
    private static final String[] smallPrefixes = {"d", "c", "m", "u", "n", "p", "f", "a", "z", "y", "r", "q"};
    private static final String[] bigPrefixes = {"da", "h", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};

    private static final Units[] UNITS = new Units[]{
        new Units("1.602176634e-19",   new Dimension(-2, 2, 1, 0, 0, 0, 0), "eV"),  // electron volt
        new Units("60",                new Dimension(1, 0, 0, 0, 0, 0, 0),  "min"), // minute
        new Units("3600",              new Dimension(1, 0, 0, 0, 0, 0, 0),  "h"),   // hour
        new Units("149597870700",      new Dimension(0, 1, 0, 0, 0, 0, 0),  "au"),  // astronomical unit
        new Units("1.66053906660e-27", new Dimension(0, 0, 1, 0, 0, 0, 0),  "Da"),  // dalton
        new Units("3.0857e16",         new Dimension(0, 1, 0, 0, 0, 0, 0),  "pc"),  // parsec
        new Units("100000",            new Dimension(-2, -1, 1, 0, 0, 0, 0),"bar"), // bar
        new Units("101325",            new Dimension(-2, -1, 1, 0, 0, 0, 0),"atm"), // atmosphere
        new Units("4184",              new Dimension(-2, 2, 1, 0, 0, 0, 0), "cal")  // calorie
    };

    private static final Units[] CONSTANTS = new Units[]{
        new Units("299792458",         new Dimension(-1, 1, 0, 0, 0, 0, 0), "c"),      // speed of light
        new Units("6.62607015e-34",    new Dimension(-1, 2, 1, 0, 0, 0, 0), "h"),      // planck's constant
        new Units("1.054571817e-34",   new Dimension(-1, 2, 1, 0, 0, 0, 0), "h-"),     // reduced planck's constant
        new Units("8.8541878128e-12",  new Dimension(4, -3, -1, 2, 0, 0, 0), "e0"),    // electric constant
        new Units("1.380649e-23",      new Dimension(2, 2, 1, 0, 1, 0, 0), "kb"),      // boltzmann constant
        new Units("6.67430e-11",       new Dimension(-2, 3, -1, 0, 0, 0, 0), "G"),     // gravitational constant
        new Units("8.9875517923e9",    new Dimension(-4, 3, 1, -2, 0, 0, 0), "ke"),    // coulomb's constant
        new Units("5.670374419e-8",    new Dimension(-3, 0, 1, 0, -4, 0, 0), "sigma"), // stefan-boltzmann constant
        new Units("1.60217663e-19",    new Dimension(1, 0, 0, 1, 0, 0, 0), "e"),       // elementary charge
        new Units("7.2973525693e-3",   new Dimension(0, 0, 0, 0, 0, 0, 0), "alpha"),   // fine-structure constant
        new Units("9.2740100783e-24",  new Dimension(0, 2, 0, 1, 0, 0, 0), "uB"),      // bohr magneton
        new Units("5.050783699e-27",   new Dimension(0, 2, 0, 1, 0, 0, 0), "uN"),      // nuclear magneton
        new Units("5.29177210903e-11", new Dimension(0, 1, 0, 0, 0, 0, 0), "a0"),      // bohr radius
        new Units("10973731.6",        new Dimension(0, -1, 0, 0, 0, 0, 0), "R"),      // rydberg constant
        new Units("6.02214e23",        new Dimension(0, 0, 0, 0, 0, 1, 0), "NA"),      // avogadro's number
        new Units("1.67262192e-27",    new Dimension(0, 0, 1, 0, 0, 0, 0), "mp"),      // proton mass
        new Units("9.1093837e-37",     new Dimension(0, 0, 1, 0, 0, 0, 0), "me"),      // electron mass
        new Units("1.67492749804e-27", new Dimension(0, 0, 1, 0, 0, 0, 0), "mn")       // neutron mass
    };

    private String alias;

    public Units(String quantity, String alias) {
        super(quantity);
        this.alias = alias;
    }

    private Units(String value, Dimension dimensions, String alias) {
        super(value, dimensions);
        this.alias = alias;
    }

    public static Quantity getUnit(String str) {
        if (UNITS == null)
            return null;

        for (Units unit : UNITS) {
            if (unit.alias.equals(str))
                return unit;
        }

        try {
            return new Quantity("1", new Dimension(str));
        }
        catch (Exception ignored) {}

        return null;
    }

    public static Quantity getConstant(String str) {
        for (Units unit : CONSTANTS) {
            if (unit.alias.equals(str))
                return unit;
        }

        return null;
    }

    public static Quantity getPrefix(String str) {
        int pow;

        for (int i = 0; i < smallPrefixes.length; i++) {
            if (str.equals(smallPrefixes[i]) || str.equals(bigPrefixes[i])) {
                pow = switch (i) {
                    case 0 -> 1;
                    case 1 -> 2;
                    default -> (i - 1) * 3;
                };

                if (str.equals(smallPrefixes[i]))
                    pow *= -1;
                return new Quantity("1e" + pow);
            }
        }

        return null;
    }
}