package physics;

import java.math.BigDecimal;

public class Units extends Quantity {
    private static final Units[] UNITS = new Units[]{
        new Units("1.602176634e-19J",    "eV"),  // electron volt
        new Units("60s",                 "min"), // minute
        new Units("3600s",               "h"),   // hour
        new Units("149597870700m",       "au"),  // astronomical unit
        new Units("1.66053906660e-27kg", "Da"),  // dalton
        new Units("3.0857e16m",          "pc"),  // parsec
        new Units("100000Pa",            "bar"), // bar
        new Units("101325Pa",            "atm"), // atmosphere
        new Units("4184J",               "cal")  // calorie
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

    public static Quantity identify(String str) {
        if (UNITS == null)
            return null;

        for (Units unit : UNITS) {
            if (unit.alias.equals(str))
                return unit;
        }

        return null;
    }

    public static Quantity getConstant(String str) {
        for (Units unit : CONSTANTS) {
            if (unit.alias.equals(str))
                return unit;
        }

        return null;
    }
}