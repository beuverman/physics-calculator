package physics;

/**
 * Handles definitions of constants and non-SI units
 * Handles SI prefixes
 */
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
        new Units("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679",
                  new Dimension(0,0,0,0,0,0,0), "pi"),                                       // pi
        new Units("2.7182818284590",   new Dimension(0,0,0,0,0,0,0), "e"),             // euler's number
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
        new Units("6.02214e23",        new Dimension(0, 0, 0, 0, 0, -1, 0), "NA"),      // avogadro's number
        new Units("1.67262192e-27",    new Dimension(0, 0, 1, 0, 0, 0, 0), "mp"),      // proton mass
        new Units("9.1093837e-37",     new Dimension(0, 0, 1, 0, 0, 0, 0), "me"),      // electron mass
        new Units("1.67492749804e-27", new Dimension(0, 0, 1, 0, 0, 0, 0), "mn")       // neutron mass
    };

    private String alias;

    /**
     * Creates a Unit with a given Quantity, and alias to identify it by
     * @param quantity Quantity of the unit
     * @param alias Alias of the unit
     */
    public Units(String quantity, String alias) {
        super(quantity);
        this.alias = alias;
    }

    /**
     * Creates a Unit with a given value, dimension, and alias to identify it by
     * @param value Value of the unit
     * @param dimensions Dimensions of the unit
     * @param alias Alias of the unit
     */
    private Units(String value, Dimension dimensions, String alias) {
        super(value, dimensions);
        this.alias = alias;
    }

    /**
     * Returns the unit with the given alias.
     * Handles SI prefixes.
     * @param str Alias of the unit to be returned
     * @return Returns the unit with the given alias
     */
    public static Quantity getUnit(String str) {
        // No prefix
        if (str.equals("g")) {
            return new Quantity("0.001", new Dimension("kg"));
        }
        for (Units unit : UNITS) {
            if (unit.alias.equals(str))
                return unit;
        }
        try {
            Dimension dim = new Dimension(str);
            return new Quantity("1", dim);
        }
        catch (Exception ignored) {}

        // Prefix
        // For the only 2 character prefix: da
        if (str.length() == 1)
            throw new RuntimeException("Unrecognized unit " + str);
        Quantity prefix = Units.getPrefix(str.substring(0, 2));
        // 1 character prefix
        if (prefix == null) {
            prefix = Units.getPrefix(str.substring(0, 1));

            if (prefix == null)
                throw new RuntimeException("Unrecognized unit " + str);

            return getUnit(str.substring(1)).multiply(prefix);
        }
        else {
            return getUnit(str.substring(2)).multiply(prefix);
        }
    }

    /**
     * Returns the constants that has the given alias
     * @param str Alias of the constant to be returned
     * @return Returns the quantity that represents the constant
     */
    public static Quantity getConstant(String str) {
        for (Units unit : CONSTANTS) {
            if (unit.alias.equals(str))
                return unit;
        }

        return null;
    }

    /**
     * Determines the modifier associated with the given SI prefix
     * @param str The prefix to be used
     * @return Returns a dimensionless quantity with the value associated with the prefix, null if prefix not recognized
     */
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