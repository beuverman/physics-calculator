package physics;

import org.jscience.mathematics.number.Rational;

import java.util.*;

/**
 * Represents the dimensions of a quantity in terms of the SI fundamental units
 */
public class Dimension {
    private static final HashMap<String, Dimension> baseUnits1;
    private static final HashMap<Dimension, String> baseUnits2;

    private static final Dimension DIMENSIONLESS = new Dimension(0, 0, 0, 0, 0, 0,0);

    private Rational[] dimensions = new Rational[7];

    /**
     * Creates a dimensionless Dimension
     */
    public Dimension() {
        for (int i = 0; i < 7; i++) {
            dimensions[i] = Rational.ZERO;
        }
    }

    /**
     * Creates a Dimension with the specified value in each SI base quantity.
     * Each dimension is treated as having a value equal to the appropriate SI fundamental unit
     * @param T Time
     * @param L Length
     * @param M Mass
     * @param I Electric current
     * @param theta Thermodynamic temperature
     * @param N Amount of substance
     * @param J Luminous intensity
     */
    public Dimension(int T, int L, int M, int I, int theta, int N, int J) {
        dimensions[0] = Rational.valueOf(T,1);
        dimensions[1] = Rational.valueOf(L,1);
        dimensions[2] = Rational.valueOf(M,1);
        dimensions[3] = Rational.valueOf(I,1);
        dimensions[4] = Rational.valueOf(theta,1);
        dimensions[5] = Rational.valueOf(N,1);
        dimensions[6] = Rational.valueOf(J,1);
    }

    /**
     * Creates a Dimension with its value in each SI quantity given by the array.
     */
    public Dimension(int[] dims) {
        this(dims[0], dims[1], dims[2], dims[3], dims[4], dims[5], dims[6]);

        if (dims.length != 7)
            throw new RuntimeException("Cannot create Dimension with anything but 7 integers.");
    }

    /**
     * Creates a Dimension with the specified values in each SI base quantity
     * @param dimensions Array representing the dimensions in the following order:
     *                   time, length, mass, current, temperature, amount, intensity
     */
    private Dimension(Rational[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Creates a Dimension with the same dimensions as the given SI unit
     * @param dimension Symbol of the SI unit (e.g., Hz for Hertz, W for Watt). Case-sensitive, official SI units only
     */
    public Dimension(String dimension) {
        Dimension dim = baseUnits1.get(dimension);
        if (dim == null)
            throw new RuntimeException("Unrecognized dimension: \"" + dimension + "\"");

        setDimensions(dim.getDimensions());
    }

    /**
     * Overwrites the dimensions of this Dimension with the given dimensions
     * @param dimensions Array representing the dimensions in the following order:
     *                   time, length, mass, current, temperature, amount, intensity
     */
    private void setDimensions(Rational[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Returns the dimensions of this Dimension
     * @return Returns an array with the dimensions in the following order:
     *         time, length, mass, current, temperature, amount, intensity
     */
    private Rational[] getDimensions() {
        return dimensions.clone();
    }

    /**
     * Compares whether the Dimensions have the same value for each of the dimensions
     * @param units The Dimension to be compared against
     * @return Returns true if they have the same value for each dimension, false otherwise
     */
    public boolean equals(Dimension units) {
        for (int i = 0; i < dimensions.length; i++) {
            if (!dimensions[i].equals(units.dimensions[i]))
                return false;
        }

        return true;
    }

    /**
     * Returns a new Dimension whose dimensions are the inverse of this object
     * @return Returns a new Dimension where each dimension is the negative of the respective dimension in this object
     */
    public Dimension invert() {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].opposite();
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension whose dimensions are the sum of two Dimensions
     * @param units The Dimension to be added to this Dimension
     * @return Returns a new Dimension whose dimensions are the sum of this Dimension and the argument
     */
    public Dimension add(Dimension units) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].plus(units.dimensions[i]);
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension whose dimensions are the difference of two Dimensions
     * @param units The Dimension to be subtracted from this Dimension
     * @return Returns a new Dimension whose dimensions are those of this Dimension minus those of the argument
     */
    public Dimension subtract(Dimension units) {
        return add(units.invert());
    }

    /**
     * Returns a new Dimension where each dimension has been multiplied by a constant
     * @param n The constant to multiply all dimensions by
     * @return Returns a new Dimension whose dimensions are the product of this Dimension and the argument
     */
    public Dimension multiply(int n) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].times(n);
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension where each dimension has been divided by a constant
     * @param n The constant to divide all dimensions by
     * @return Returns a new Dimension whose dimensions are the quotient of this Dimension and the argument
     */
    public Dimension divide(int n) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].divide(Rational.valueOf(n,1));
        }

        return new Dimension(ret);
    }

    /**
     * Checks whether this dimension is dimensionless
     * @return Returns true if all dimensions are 0, false otherwise
     */
    public boolean isDimensionless() {
        return this.equals(DIMENSIONLESS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return Objects.deepEquals(dimensions, dimension.dimensions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dimensions);
    }

    /**
     * Returns a LaTeX string representation of this Dimension
     * @return Returns the SI symbol of the unit if there is a direct match.
     *         If there is no match, gives each base unit and its respective power in a notation suitable for LaTeX
     */
    public String toLatexString() {
        return toString(true);
    }

    /**
     * Returns a string representation of this Dimension
     * @return Returns the SI symbol of the unit if there is a direct match.
     *         If there is no match, gives each base unit and its respective power
     */
    public String toString() {
        return toString(false);
    }

    /**
     * Identifies SI unit associated with this Dimension.
     * If there is no direct unit, gives each base unit and its respective power
     * @param isLatex If true, powers of base units are given in curly braces to aid LaTeX
     * @return Returns a string representation of this Dimension
     */
    private String toString(boolean isLatex) {
        String str = baseUnits2.get(this);
        if (str != null)
            return str;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensions.length; i++) {
            if (!dimensions[i].equals(Rational.ZERO)) {
                sb.append(switch (i) {
                    case 0 -> "s";
                    case 1 -> "m";
                    case 2 -> "kg";
                    case 3 -> "A";
                    case 4 -> "K";
                    case 5 -> "mol";
                    case 6 -> "cd";
                    default -> "";
                });

                sb.append("^");
                if (isLatex) sb.append("{");
                sb.append(dimensions[i].getDivisor().equals(1) ? dimensions[i].getDividend() : dimensions[i]);
                if (isLatex) sb.append("}");
            }
        }

        return sb.toString();
    }

    public static Set<String> getSIUnitStrings() {
        return baseUnits1.keySet();
    }

    static {
        baseUnits1 = new HashMap<>();
        baseUnits1.put("s",   new Dimension(1, 0, 0, 0, 0, 0, 0));   // Second
        baseUnits1.put("m",   new Dimension(0, 1, 0, 0, 0, 0, 0));   // Metre
        baseUnits1.put("kg",  new Dimension(0, 0, 1, 0, 0, 0, 0));   // Kilogram
        baseUnits1.put("A",   new Dimension(0, 0, 0, 1, 0, 0, 0));   // Ampere
        baseUnits1.put("K",   new Dimension(0, 0, 0, 0, 1, 0, 0));   // Kelvin
        baseUnits1.put("mol", new Dimension(0, 0, 0, 0, 0, 1, 0));   // Mole
        baseUnits1.put("cd",  new Dimension(0, 0, 0, 0, 0, 0, 1));   // Candela
        baseUnits1.put("Hz",  new Dimension(-1, 0, 0, 0, 0, 0, 0));  // Hertz
        baseUnits1.put("N",   new Dimension(-2, 1, 1, 0, 0, 0, 0));  // Newton
        baseUnits1.put("Pa",  new Dimension(-2, -1, 1, 0, 0, 0, 0)); // Pascal
        baseUnits1.put("J",   new Dimension(-2, 2, 1, 0, 0, 0, 0));  // Joule
        baseUnits1.put("W",   new Dimension(-3, 2, 1, 0, 0, 0, 0));  // Watt
        baseUnits1.put("C",   new Dimension(1, 0, 0, 1, 0, 0, 0));   // Coulomb
        baseUnits1.put("V",   new Dimension(-3, 2, 1, -1, 0, 0, 0)); // Volt
        baseUnits1.put("F",   new Dimension(4, -2, -1, 2, 0, 0, 0)); // Farad
        baseUnits1.put("O",   new Dimension(-3, 2, 1, -2, 0, 0, 0)); // Ohm
        baseUnits1.put("S",   new Dimension(3, -2, -1, 2, 0, 0, 0)); // Siemens
        baseUnits1.put("Wb",  new Dimension(-2, 2, 1, -1, 0, 0, 0)); // Weber
        baseUnits1.put("T",   new Dimension(-2, 0, 1, -1, 0, 0, 0)); // Tesla
        baseUnits1.put("H",   new Dimension(-2, 2, 1, -2, 0, 0, 0)); // Henry
        baseUnits1.put("Bq",  new Dimension(-1, 0, 0, 0, 0, 0, 0));  // Becquerel
        baseUnits1.put("Sv",  new Dimension(-2, 2, 0, 0, 0, 0, 0));  // Sievert

        baseUnits2 = new HashMap<>();
        for (Map.Entry<String, Dimension> entry : baseUnits1.entrySet()) {
            baseUnits2.put(entry.getValue(), entry.getKey());
        }
    }
}